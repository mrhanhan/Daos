package com.mrhan.database;

import com.mrhan.database.keytype.ForginKey;
import com.mrhan.database.mrhanDaos.ColumType;
import com.mrhan.database.mrhanDaos.EntityTable;
import com.mrhan.util.CodeRuntimeTest;
import com.mrhan.util.MrhanUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * <p align="center">万能操作类</p>
 * <font>作者：Mrhan</font>
 *
 * @param <T> 需要操作实体类的类型 此实体类还需拥有无参数的构造函数
 * @see AbsDatabaseDao AbsDatabaseDao&lt;T&gt; 父类
 */
public class UniversalDao<T> extends BaseDao<T> implements IEntityDao<T> {
    /**
     * 外键数据
     */
    private Map<Object, Object> forginData = new HashMap<>();
    /**
     * 外键操作类
     */
    private Map<Class<?>, UniversalDao> forginDaos = new HashMap<>();//外键操作类集合
    /**
     * 需要操作的实体
     */
    private Class<?> entityClass;
    /**
     * 实体表与数据库的联系对象
     */
    private EntityTable entityTable;

    /**
     * 构造函数
     *
     * @param connect  连接管理对象
     * @param daoClass 实体类Class对象
     */
    public UniversalDao(IConnect connect, Class<?> daoClass) {
        super(connect);
        this.entityClass = daoClass;
        init();
    }

    /**
     * 解析数据集合
     *
     * @param rs 数据结果集合
     * @return 实体对象集合
     */
    private List<T> parseDatas(ResultSet rs) {
        List<T> entityList = new ArrayList<>();
        return entityList;
    }


    public ITable getTable() {
        return entityTable;
    }

    /**
     * 初始化方法
     */
    private void init() {
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("当前类未发现实体注解！Table");
        }
        /**
         * 根据注解创建关系对象
         */
        entityTable = (EntityTable) EntityTable.getTable(table);
    }

    @Override
    protected void execQuery(String sql, IEntity entity, Object... params) {
        super.execQuery(sql, entity, params);
        forginData.clear();
    }

    @Override
    protected <E> List<E> execQuery(String sql, IDao<E> entity, Object... params) {
        List<E> es = super.execQuery(sql, entity, params);
        forginData.clear();
        return es;
    }

    @Override
    protected List<T> executeQuery(String sql, Object... params) {
        List<T> ts =  super.executeQuery(sql, params);
        forginData.clear();
        return  ts;
    }

    @Override
    public final T getEntity(ResultSet rs) {
    //    CodeRuntimeTest crt = new CodeRuntimeTest();
      //  crt.lockTime();
        Set<IField> columents = entityTable.fields();
        ColumType columType = null;//字段类型
        T t = entityTable.createEntity();
        Object data = null;
        ForginKey fk = null;
        String forginName = "";
        Class forginClass = null;
        Object obj = null;
        //    crt.showTimeMsg("当天实体："+entityClass.getName()+" 字段个数："+columents.size());
        for (IField f : columents) {
            columType = f.columentType();
            fk = f.forginkey();
            data = getDataByType(rs, columType, f.columentName());
            // System.out.println("COL:"+f.columentName()+"DATA:" + data + " TYPE:" + f.columentType());
            /*判断字段是否是外键引用！并且和字段是外键实体对象*/
            if (f.isForginKey() && fk.isEntity()) {
          //      crt.showTimeMsg("当天实体："+entityClass.getName()+"构建外键");
                CodeRuntimeTest.hidden();
                forginClass = fk.entity();
                //获取外键引用字段名称
                forginName = fk.forginCol();
                obj = forginData.get(forginName);//去缓存取数据
                if (obj == null) {   /*空引用，则默认为当前名称*/
                    if (forginName.isEmpty()) {
                        forginName = f.columentName();
                    }
                    //根据外键类型获取操作类，避免重复创作相同的操作类
                    UniversalDao ud = forginDaos.get(forginClass);
                    if (ud == null) {
                        //如果不存在，则创建
                        ud = new UniversalDao(this.getConn(), forginClass);
                        //保存当前操作类
                        forginDaos.put(forginClass, ud);
                    }
                    data = ud._getForgineEntity(forginName, data);

                    forginData.put(forginName, data);//保存数据到缓存
                    CodeRuntimeTest.show();
                   // crt.showTimeMsg("当天实体："+entityClass.getName()+"构建外键:完成");
                } else {
                    data = MrhanUtil.cloneObject(forginClass,obj);
                }
            }
            /*设置数据*/
            f.setData(t, data);

        }
        return t;

    }

    /**
     * 根据提供 数据的结果集合，数据键 和 数据类型 进行获取数据
     *
     * @param rs
     * @param columType
     * @return
     */
    private Object getDataByType(ResultSet rs, ColumType columType, String name) {
        Object obj = null;
        try {
            switch (columType) {
                case STRING:
                    obj = rs.getString(name);
                    break;
                case INT:
                    obj = new Integer(rs.getInt(name));
                    break;
                case DATE:
                    obj = rs.getDate(name);
                    break;
                case LONG:
                    obj = new Long(rs.getLong(name));
                    break;
                case FLOAT:
                    obj = new Float(rs.getFloat(name));
                    break;
                case DOUBLE:
                    obj = new Double(rs.getDouble(name));
                    break;
                case BINARY://流对象
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    byte[] bs = new byte[1024];
                    InputStream is = rs.getBinaryStream(name);
                    int a = 0;
                    try {
                        while (true) {

                            a = is.read(bs);

                            if (a < 1024) {
                                break;
                            }
                            bao.write(bs, 0, a);
                        }
                        obj = bao.toByteArray();
                        is.close();
                        bao.close();
                    } catch (IOException e) {

                    }
                    break;
                case DECIMAL:
                    obj = rs.getBigDecimal(name);
                    break;
                case DATETIME:
                    obj = rs.getTimestamp(name);
                    break;
            }
        } catch (Exception e) {
            System.err.println("类型错误！实体表字段(" + name + ")不存在类型：" + columType);
        }
        return obj;
    }

    @Override
    public boolean addEntity(T t) {
        Map<String, Object> datas = parseFiledData(this.entityTable, t);
        List params = new ArrayList();
        if (datas.isEmpty())
            return false;
        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(entityTable.getTableName()).append("(");

        StringBuilder vs = new StringBuilder(" values(");

        Set<String> keys = datas.keySet();
        int i = 0;
        for (String col : keys) {
            i++;
            sb.append(col);
            params.add(datas.get(col));
            vs.append("?");
            if (i < keys.size()) {
                sb.append(",");
                vs.append(",");
            }
        }

        sb.append(") ").append(vs).append(") ");
        return executeUpdate(sb.toString(), params.toArray()) > 0;
    }

    @Override
    public int updateEntityByIdentifi(T t) {
        IField idenField = entityTable.getIdent();
        if (idenField == null) {
            throw new RuntimeException("无法使用，当前方法进行修改下！无法找到此表的标识（自动列，主键列，唯一键）字段!");
        } else {
            StringBuilder sql = new StringBuilder("update ").append(entityTable.getTableName()).append(" set ");
            Map<String, Object> datas = parseFiledData(this.entityTable, t);
            Set<String> keys = datas.keySet();
            List params = new ArrayList();
            int i = 0;
            for (String col : keys) {
                i++;
                if(col.equals(idenField.columentName())){
                    continue;
                }
                sql.append(col);
                sql.append("=");
                sql.append("?");
                params.add(datas.get(col));
                if (i < keys.size()) {
                    sql.append(",");
                }
            }
            sql.append(" where ").append(idenField.columentName()).append(" = ?");
           params.add(idenField.getdate(t));
            return executeUpdate(sql.toString(), params.toArray());

        }
    }


    @Override
    public int updateEntity(T newEntity, T olderEntity) {


        StringBuilder sql = new StringBuilder("update ").append(entityTable.getTableName()).append(" set ");
        Map<String, Object> datas = parseFiledData(this.entityTable, newEntity);
        Set<String> keys = datas.keySet();
        List params = new ArrayList();
        int i = 0;
        for (String col : keys) {
            i++;
            sql.append(col);
            sql.append("=");
            sql.append("?");
            params.add(datas.get(col));
            if (i < keys.size()) {
                sql.append(",");
            }
        }
        sql.append(" where 1=1 ");
        /*旧的值*/
        datas = parseFiledData(this.entityTable, newEntity);
        keys = datas.keySet();

        for (String col : keys) {
            i++;
            sql.append(" and ");
            sql.append(col);
            sql.append(" = ");
            sql.append("? ");
            params.add(datas.get(col));
        }
        return executeUpdate(sql.toString(), params.toArray());
    }

    @Override
    public boolean delete(T t) {

        return deleteEntity(t) > 0;
    }

    @Override
    public int deleteEntity(T entity) {
        StringBuilder sql = new StringBuilder("delete from ").append(entityTable.getTableName()).append(" where 1=1  ");
        Map<String, Object> datas = parseFiledData(this.entityTable, entity);
        Set<String> keys = datas.keySet();
        List params = new ArrayList();

        for (String col : keys) {
            sql.append(" and ");
            sql.append(col);
            sql.append(" = ");
            sql.append(" ? ");
            params.add(datas.get(col));

        }
        return executeUpdate(sql.toString(), params.toArray());
    }

    /**
     * 当前方法设计之初，主要提过给外表查询使用
     * 根据实体信息对象，和字段名称，和字段的值查询单条数据！
     * <br/>
     * 原理！如果是外键！那么所引用主表的此条记录，一定是唯一的
     *
     * @param name
     * @param value
     * @return
     */
    private T _getForgineEntity(String name, Object value) {
        IField fo = entityTable.getField(name);//获取实体指定字段的字段信息
        if (fo == null) {
            throw new RuntimeException("外键引用错误：" + name);
        }
        StringBuilder sb = new StringBuilder("select * from ").append(asName()).append(" where ");
        sb.append(name).append(" = ?");
        List<T> entitys = executeQuery(sb.toString(), value);
        if (entitys.isEmpty())
            return null;
        return entitys.get(0);
    }

    /**
     * 关闭资源方法
     *
     * @param rs
     * @param statement
     */
    protected void close(ResultSet rs, Statement statement) {
        try {
            if (rs != null)
                rs.close();
            if (statement != null)
                statement.close();
        } catch (Exception e) {

        }
    }


    private String asName(){
        String as = entityTable.getTableAsName();
        String tan = entityTable.getTableName();

        if(as.isEmpty())
            return tan;
        else{
            return tan+" as "+as;
        }
    }

    @Override
    public List<T> selectAll() {
        StringBuilder sb = new StringBuilder("select * from ").append(asName());
        return executeQuery(sb.toString());
    }

    @Override
    public List<T> selectByEntity(T entity) {
        StringBuilder sql = new StringBuilder("select * from ").append(asName()).append(" where 1=1 ");
        Map<String, Object> ps = parseFiledData(entityTable, entity);
        List params = new ArrayList();
        Set<String> keys = ps.keySet();
        for (String col : keys) {
            sql.append(" and ");
            sql.append(col);
            sql.append(" = ");
            sql.append(" ? ");
            params.add(ps.get(col));

        }

        return executeQuery(sql.toString(), params.toArray());
    }

    /**
     * 解析不为空的字段数据
     *
     * @param entityTable 实体信息
     * @param t           实体对象
     * @param filterType  过滤检测
     * @return
     */
    private Map<String, Object> parseFiledData(EntityTable entityTable, T t, Class<?>... filterType) {
        Map<String, Object> notNullFileds = new HashMap<>();
        Object obj = null;
        boolean isContinue = false;
        for (IField f : entityTable.fields()) {
//            isContinue = false;
//            for(Class<?> cs : filterType){
//                if(!entityTable.findTypeField(cs).isEmpty()){
//                    isContinue = true;
//                    break;
//                }
//            }
//
            obj = f.getdate(t);
            if (!MrhanUtil.isEmpty(obj)) {
                notNullFileds.put(f.columentName(), obj);
            }
        }
        return notNullFileds;
    }

    @Override
    public int insert(OptionStatement statement) {
        String sql = statement.getSQLStatemenet(entityTable);
        return executeUpdate(sql, statement.getParams());
    }

    @Override
    public int update(OptionStatement statement) {
        String sql = statement.getSQLStatemenet(entityTable);
        return executeUpdate(sql, statement.getParams());
    }

    @Override
    public int delete(OptionStatement statement) {
        String sql = statement.getSQLStatemenet(entityTable);
        return executeUpdate(sql, statement.getParams());
    }

    @Override
    public List<T> select(OptionStatement statement) {
        return executeQuery(statement.getSQLStatemenet(entityTable), statement.getParams());
    }

    @Override
    public List<T> executeSelect(ISelectStatement<T> selectStatement) {
        return execQuery(selectStatement.getSQLStatemenet(entityTable), (IDao<T>)selectStatement, selectStatement.getParams());
    }

    @Override
    public List<T> executeSelectM(ISelectStatement<T>... selectStatements) {
        List<T> ens = null;
        for(ISelectStatement iss : selectStatements){
            if(ens ==null){
                ens = executeSelect(iss);
            }else{
                ens.addAll(executeSelect(iss));
            }
        }
        return ens;
    }

    @Override
    public void select(ISelectStatement... selectStatements) {
        for(ISelectStatement iss : selectStatements){
            execQuery(iss.getSQLStatemenet(entityTable),(IEntity)iss,iss.getParams());
        }
    }

    @Override
    public int executeUpdate(OptionStatement... options) {
        int a= 0;
        for (OptionStatement os : options){
            a+=executeUpdate(os.getSQLStatemenet(entityTable),os.getParams());
        }
        return a;
    }
}
/**
 * 插入数据
 *
 * @param t 具体数据类型
 * @return 插入结果
 */
//    @Override
//    public boolean insert(T t) {
//        SQLStatement sqls = entityTable.createInset(t);
//        try {
//            int a = execUpdate(sqls.getSql(),sqls.getPro());
//          if(a>0){
//              List<IField> keys = getIdentification();//获取标识字段
//              removeFieldOfType(keys, com.mrhan.db.entitydaos.keytype.AutoKey.class);//移除自动增长
//              if(keys.size()>0){
//                SQLWhere [] wheres = new SQLWhere[keys.size()];
//                for(int i=0;i<wheres.length;i++){
//                    IField fi = keys.get(i);
//                    wheres[i]= new SQLWhere(fi.columentName(),fi.getdate(t));
//                }
//                 sqls = entityTable.createSelect(wheres);
//                SelectDataInterface sdif = execQuery(sqls.getSql(),sqls.getPro());
//                if(sdif.hasnext()){
//                    sdif.next();
//                    parseData(sdif,t);
//                }
//
//              }
//              return true;
//          }else{
//              return false;
//          }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new EntityException(e.getMessage());
//        }
//    }
//    /**
//     * 获取可作为标识字段的集合<br/>
//     * 只会返回一种类型的字段 主键-》唯一键-》自动增长
//     * @return 返回标识字段的集合分别是（主键，唯一键-，自动增长）
//     */
//    private List<IField> getIdentification(){
//        List<IField> keys= entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.PrimaryKey.class);
//        if(keys.size()<1){//如果不存在主键获取唯一键作为标识
//            keys = entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.UniqueKey.class);
//        }
//        if(keys.size()<1){//如果不存在唯一键获取自动增长列作为作为标识
//            keys = entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.AutoKey.class);
//        }
//
//        return keys;
//    }
//    /**
//     * 获取指定类型字段信息集合
//     * @param types 类型，当前类型代表这字段上的键，Class限定于 包下的注解类型 {@link com.mrhan.database.entitydaos.keytype}
//     * @return 返回字段集合
//     */
//    private List<IField> getFieldByTypes(Class<?> ...types){
//        List<IField> fields = new ArrayList<>();
//        for(Class<?> c : types){
//            fields.addAll(entityTable.findTypeField(c));
//        }
//        return fields;
//    }
//
//    /**
//     * 移除集合中指定类型的字段 并返回个数
//     * @param fs
//     * @param delTypes
//     * @return
//     */
//    private int removeFieldOfType(List<IField> fs,Class<?> delTypes){
//
//        int count = 0;
//
//            for(IField t : entityTable.findTypeField(delTypes)){
//                if(fs.contains(t)){
//                    count++;
//                    fs.remove(t);
//                }
//            }
//
//        return count;
//    }
//    /**
//     * 修改数据
//     *
//     * @param t 具体需要修改的类型
//     * @return 返回
//     */
//    @Override
//    public boolean update(T t) {
//
//        /**
//         * 获取这张表中的主键
//         */
//        List<IField> keys = getIdentification();
//        if(keys.size()<1){//如果还是空的话，不推荐此方法！无法找到可标识的字段
//            throw  new EntityException("不推荐此方法！无法找到可标识的字段(当前实体类对应的表["+entityTable.getTableName()+"] 无(主键、唯一键或自动增长字段))");
//        }
//        IField field = keys.get(0);//获取一个标识字段，作为标识
//        SQLStatement sqls = entityTable.createUpdate(t,new SQLWhere(field.columentName(),field.getdate(t)));
//        try {
//            int a = execUpdate(sqls.getSql(),sqls.getPro());
//            return a >0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new EntityException(e.getMessage());
//        }
//    }
//    /**
//     * 删除数据
//     *
//     * @param t 删除的数据
//     * @return 返回删除结果
//     */
//    @Override
//    public boolean delete(T t) {
//        /**
//         * 获取这张表中的主键
//         */
//        List<IField> keys = getIdentification();
//        if(keys.size()<1){//如果还是空的话，不推荐此方法！无法找到可标识的字段
//            throw  new EntityException("不推荐此方法！无法找到可标识的字段(当前实体类对应的表["+entityTable.getTableName()+"] 无(主键、唯一键或自动增长字段))");
//        }
//        IField field = keys.get(0);//获取一个标识字段，作为标识
//        SQLStatement sqls = entityTable.createDelete(t,new SQLWhere(field.columentName(),field.getdate(t)));
//        try {
//            int a = execUpdate(sqls.getSql(),sqls.getPro());
//            return a >0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new EntityException(e.getMessage());
//        }
//
//    }
//    /**
//     * 查询数据集合
//     *
//     * @return
//     */
//    @Override
//    public List<T> selectAll() {
//        String tbName = entityTable.getTableName();
//        return parseDatas(execQuery("SELECT * FROM "+tbName));
//    }
//
//    /**
//     * 查询数据集合
//     *
//     * @param key
//     * @param value
//     * @return
//     */
//    @Override
//    public List<T> selectFindAll(Object key, Object value) {
//        String tbName = entityTable.getTableName();
//        List<T> list =new ArrayList<T>();
//   //     CodeRuntimeTest crt = new CodeRuntimeTest();
//        try {
//       //     crt.lockTime();
//            String sql = "SELECT * FROM " + tbName + " where " + key + " = ?";
//        //    crt.showTimeMsg("String:");
//            SelectDataInterface sdif = execQuery(sql, value);
//       //     crt.showTimeMsg("selectFindAll:");
//            list =  parseDatas(sdif);
//        }catch(EntityException e){
//            System.out.println(e.getMessage());
//            System.out.println(key+" | "+value);
//        }
//        return list;
//    }
//
//    /**
//     * 获取指定的数据
//     *
//     * @param key   制定的Key
//     * @param value 制定过的值
//     * @return 返回指定数据的实体类
//     */
//    @Override
//    public T get(Object key, Object value) {
//      //  CodeRuntimeTest crt = new CodeRuntimeTest();
//       // crt.lockTime();
//        List<T> list  = selectFindAll(key,value);
//
//     //   crt.showTimeMsg("Get:");
//        if(list.size()>0)
//            return list.get(0);
//        return null;
//    }
//
//    /**
//     * 清除表中数据
//     *
//     * @return
//     */
//    @Override
//    public boolean clearTableDate() {
//        try {
//            int a = execUpdate("delete from "+entityTable.getTableName());
//            return a>0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new EntityException(e.getMessage());
//        }
//
//    }
//
//    /**
//     * 清除表中数据,新的表
//     *
//     * @return
//     *
//     */
//    @Override
//    public boolean newTableDate() {
//        try {
//            int a = execUpdate("truncate table "+entityTable.getTableName());
//            return a>0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new EntityException(e.getMessage());
//        }
//    }
//
//    @Override
//    protected T getEntity(ResultSet rs) {
//        return null;
//    }
//}