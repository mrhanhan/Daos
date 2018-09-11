package com.mrhan.db.entitydaos;

import com.mrhan.db.AbsDatabaseDao;
import com.mrhan.db.IDatabaseConnnect;
import com.mrhan.db.SQLStatement;
import com.mrhan.db.SelectDataInterface;
import com.mrhan.db.allrounddaos.exception.EntityException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p align="center">万能操作类</p>
 * <font>作者：Mrhan</font>
 * @param <T> 需要操作实体类的类型 此实体类还需拥有无参数的构造函数
 * @see AbsDatabaseDao AbsDatabaseDao&lt;T&gt; 父类
 */
public class UniversalDao<T> extends AbsDatabaseDao<T> {




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
     * @param idc    数据库连接类
     * @param dbName 操作的数据库名称
     */
    public UniversalDao(IDatabaseConnnect idc, String dbName,Class<?> daoClass) {
        super(idc, dbName);
       this.entityClass = daoClass;

        init();
    }

    public UniversalDao(AbsDatabaseDao add,Class<?> daoClass){
        super(add);
        this.entityClass = daoClass;
        init();
    }

    /**
     * 解析数据集合
     * @param sdif 数据结果集合
     * @return 实体对象集合
     */
    private List<T> parseDatas(SelectDataInterface sdif){
        List<T>  entityList = new ArrayList<>();
        while(sdif.hasnext()){
            sdif.next();
            T t = entityTable.createEntity();
            parseData(sdif,t);
            if(t!=null);
                entityList.add(t);
          //  System.out.println(entityClass.getName()+" "+t);
        }
        return entityList;
    }
    /**
     * 根据数据查询集解析成对象
     * @param sdif 数据结果集合
     * @return 解析后的对象
     */
    private T parseData(SelectDataInterface sdif,T t){
        String[] cols = sdif.getColNames();//获取列名
        if(t==null){
            throw new EntityException("实体创建失败！请确保实体类拥有无参数构造函数");
        }


            for(int i=0;i<cols.length;i++) {
                String col = cols[i];
                IField field = entityTable.getField(col.toLowerCase());//获取指定列的字段信息

                if (field != null) {

                    if (field.isForginKey()) {

                            Object obj = sdif.getValue(col);
                     //   System.out.println(col+":"+obj);
                            com.mrhan.db.entitydaos.keytype.ForginKey kf = field.forginkey();//获取当前字段的外键信息

                       // CodeRuntimeTest crt = new CodeRuntimeTest();

                            if(kf!=null){
                                String key = kf.fotginCol();
                                Object val = obj;
                            //    crt.lockTime();
                              //  System.out.println("外键："+kf.forginClass().getName() +" COL:"+field.columentName()+" FCOL:"+key+" VALUE :"+val);
                                UniversalDao ud = new UniversalDao(this,kf.forginClass());
                                if(field.columentType()== DataType.TABLE) {
                                    if (kf.forginType() == DataType.COLLECTION) {
                                            val= ud.selectFindAll(key,val);//判断对方集合
                                    } else if (kf.forginType() == DataType.ARRAY) {
                                        val= ud.selectFindAll(key,val).toArray();//判断对方是否是数组
                                    } else {
                                        //System.out.println(entityClass.getName()+"外键花费时间1："+(System.currentTimeMillis()-time));
                                     //   crt.showTimeMsg(entityClass.getName()+"开始获取：");
                                        val = ud.get(key,val);
                                   //     crt.showTimeMsg(entityClass.getName()+"开始Get获取后：");
                                      //  System.out.println(entityClass.getName()+"外键花费时间2："+(System.currentTimeMillis()-time));

                                    }


                                    if(val!=null){
                                        //System.out.println("外键类型字段值"+entityTable.getTableName()+"."+col+"："+sdif.getValue(col));
                                        field.setData(t, val);
                                    }

                                    continue;

                                }
                            }
                           if(obj!=null){
                                //System.out.println("外键类型字段值"+entityTable.getTableName()+"."+col+"："+sdif.getValue(col));
                                  field.setData(t, obj);
                            }

                    } else {
                      //  System.out.println("其他类型字段值"+entityTable.getTableName()+"."+col+"："+sdif.getValue(col));
                        field.setData(t, sdif.getValue(col));
                    }
                }

            }

            return t;

    }
    public  ITable getTable(){
        return entityTable;
    }
    /**
     * 初始化方法
     */
    private void init(){
        Table table = entityClass.getAnnotation(Table.class);
        if(table==null){
            throw new EntityException("当前类未发现实体注解！Table");
        }
        /**
         * 根据注解创建关系对象
         */
        entityTable = (EntityTable) EntityTable.getTable(table);

    }
    /**
     * 插入数据
     *
     * @param t 具体数据类型
     * @return 插入结果
     */
    @Override
    public boolean insert(T t) {
        SQLStatement sqls = entityTable.createInset(t);
        try {
            int a = execUpdate(sqls.getSql(),sqls.getPro());
          if(a>0){
              List<IField> keys = getIdentification();//获取标识字段
              removeFieldOfType(keys, com.mrhan.db.entitydaos.keytype.AutoKey.class);//移除自动增长
              if(keys.size()>0){
                SQLWhere [] wheres = new SQLWhere[keys.size()];
                for(int i=0;i<wheres.length;i++){
                    IField fi = keys.get(i);
                    wheres[i]= new SQLWhere(fi.columentName(),fi.getdate(t));
                }
                 sqls = entityTable.createSelect(wheres);
                SelectDataInterface sdif = execQuery(sqls.getSql(),sqls.getPro());
                if(sdif.hasnext()){
                    sdif.next();
                    parseData(sdif,t);
                }

              }
              return true;
          }else{
              return false;
          }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityException(e.getMessage());
        }
    }
    /**
     * 获取可作为标识字段的集合<br/>
     * 只会返回一种类型的字段 主键-》唯一键-》自动增长
     * @return 返回标识字段的集合分别是（主键，唯一键-，自动增长）
     */
    private List<IField> getIdentification(){
        List<IField> keys= entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.PrimaryKey.class);
        if(keys.size()<1){//如果不存在主键获取唯一键作为标识
            keys = entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.UniqueKey.class);
        }
        if(keys.size()<1){//如果不存在唯一键获取自动增长列作为作为标识
            keys = entityTable.findTypeField(com.mrhan.db.entitydaos.keytype.AutoKey.class);
        }

        return keys;
    }
    /**
     * 获取指定类型字段信息集合
     * @param types 类型，当前类型代表这字段上的键，Class限定于 包下的注解类型 {@link com.mrhan.database.entitydaos.keytype}
     * @return 返回字段集合
     */
    private List<IField> getFieldByTypes(Class<?> ...types){
        List<IField> fields = new ArrayList<>();
        for(Class<?> c : types){
            fields.addAll(entityTable.findTypeField(c));
        }
        return fields;
    }

    /**
     * 移除集合中指定类型的字段 并返回个数
     * @param fs
     * @param delTypes
     * @return
     */
    private int removeFieldOfType(List<IField> fs,Class<?> delTypes){

        int count = 0;

            for(IField t : entityTable.findTypeField(delTypes)){
                if(fs.contains(t)){
                    count++;
                    fs.remove(t);
                }
            }

        return count;
    }
    /**
     * 修改数据
     *
     * @param t 具体需要修改的类型
     * @return 返回
     */
    @Override
    public boolean update(T t) {

        /**
         * 获取这张表中的主键
         */
        List<IField> keys = getIdentification();
        if(keys.size()<1){//如果还是空的话，不推荐此方法！无法找到可标识的字段
            throw  new EntityException("不推荐此方法！无法找到可标识的字段(当前实体类对应的表["+entityTable.getTableName()+"] 无(主键、唯一键或自动增长字段))");
        }
        IField field = keys.get(0);//获取一个标识字段，作为标识
        SQLStatement sqls = entityTable.createUpdate(t,new SQLWhere(field.columentName(),field.getdate(t)));
        try {
            int a = execUpdate(sqls.getSql(),sqls.getPro());
            return a >0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityException(e.getMessage());
        }
    }
    /**
     * 删除数据
     *
     * @param t 删除的数据
     * @return 返回删除结果
     */
    @Override
    public boolean delete(T t) {
        /**
         * 获取这张表中的主键
         */
        List<IField> keys = getIdentification();
        if(keys.size()<1){//如果还是空的话，不推荐此方法！无法找到可标识的字段
            throw  new EntityException("不推荐此方法！无法找到可标识的字段(当前实体类对应的表["+entityTable.getTableName()+"] 无(主键、唯一键或自动增长字段))");
        }
        IField field = keys.get(0);//获取一个标识字段，作为标识
        SQLStatement sqls = entityTable.createDelete(t,new SQLWhere(field.columentName(),field.getdate(t)));
        try {
            int a = execUpdate(sqls.getSql(),sqls.getPro());
            return a >0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityException(e.getMessage());
        }

    }
    /**
     * 查询数据集合
     *
     * @return
     */
    @Override
    public List<T> selectAll() {
        String tbName = entityTable.getTableName();
        return parseDatas(execQuery("SELECT * FROM "+tbName));
    }

    /**
     * 查询数据集合
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public List<T> selectFindAll(Object key, Object value) {
        String tbName = entityTable.getTableName();
        List<T> list =new ArrayList<T>();
   //     CodeRuntimeTest crt = new CodeRuntimeTest();
        try {
       //     crt.lockTime();
            String sql = "SELECT * FROM " + tbName + " where " + key + " = ?";
        //    crt.showTimeMsg("String:");
            SelectDataInterface sdif = execQuery(sql, value);
       //     crt.showTimeMsg("selectFindAll:");
            list =  parseDatas(sdif);
        }catch(EntityException e){
            System.out.println(e.getMessage());
            System.out.println(key+" | "+value);
        }
        return list;
    }

    /**
     * 获取指定的数据
     *
     * @param key   制定的Key
     * @param value 制定过的值
     * @return 返回指定数据的实体类
     */
    @Override
    public T get(Object key, Object value) {
      //  CodeRuntimeTest crt = new CodeRuntimeTest();
       // crt.lockTime();
        List<T> list  = selectFindAll(key,value);

     //   crt.showTimeMsg("Get:");
        if(list.size()>0)
            return list.get(0);
        return null;
    }

    /**
     * 清除表中数据
     *
     * @return
     */
    @Override
    public boolean clearTableDate() {
        try {
            int a = execUpdate("delete from "+entityTable.getTableName());
            return a>0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityException(e.getMessage());
        }

    }

    /**
     * 清除表中数据,新的表
     *
     * @return
     *
     */
    @Override
    public boolean newTableDate() {
        try {
            int a = execUpdate("truncate table "+entityTable.getTableName());
            return a>0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new EntityException(e.getMessage());
        }
    }
}