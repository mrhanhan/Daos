package com.mrhan.db.allrounddaos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 实体类操作类
 * @param <T>
 */
public class EntityDaos<T> extends com.mrhan.db.AbsDatabaseDao<T> {

    private Class<T> daoClass;//操作的Class
    private com.mrhan.db.IDatabaseConnnect idc;
    private  String dbName;
    private  DaoTable dt;
    private ITable nowDaoTable;//当前操作的

    /**
     * 构造函数
     *
     * @param idc    数据库连接类
     * @param dbName 操作的数据库名称
     */
    public EntityDaos(com.mrhan.db.IDatabaseConnnect idc, String dbName, Class<T> daoClass) {
        super(idc, dbName);
        this.daoClass = daoClass;
        this.idc = idc;
        this.dbName = dbName;
        init();
    }

    /**
     * 初始化方法
     */
    private void  init(){
       dt = daoClass.getAnnotation(DaoTable.class);
        nowDaoTable = com.mrhan.db.allrounddaos.baseentity.EntityTable.getTable(dt);
    }
    /**
     * 插入数据
     *
     * @param t 具体数据类型
     * @return 插入结果
     */
    @Override
    public boolean insert(T t) {
        int a= 0;
        try{
            com.mrhan.db.SQLStatement st = nowDaoTable.createInsertSql(t);
            a= execUpdate(st.getSql(),st.getPro());

            Set<IFeild> ifs = nowDaoTable.getFeild();
            String autoCol=null;//自动增长字段
            for(IFeild fi :ifs){
                if(fi.getDaoColument().isGrowth()){
                    autoCol = fi.getColument();//获取列明
                    break;
                }
            }
            if(autoCol ==null)
                 return a>0;
            com.mrhan.db.SelectDataInterface sdif = execQuery("select max("+autoCol+") '"+autoCol+"' from "+nowDaoTable.getTableName());
           if(sdif.hasnext()) {
               sdif.next();
               setDate(sdif, t, autoCol);
           }
        }catch(Exception sql){
            System.out.println(a);
            throw new com.mrhan.db.allrounddaos.exception.EntityException("无法插入重复此纪录！此字段有约束!\n"+sql.getMessage());
        }
        return a>0;
    }
    /**
     * 修改数据
     *
     * @param t 具体需要修改的类型
     * @return 返回
     */
    @Override
    public boolean update(T t) {
        IFeild pkFs[] = nowDaoTable.getPirmaryKey();//获取当前表的主键
        if(pkFs==null || pkFs.length ==0) {//判断是否有主键
            pkFs = nowDaoTable.getUnqiueKey();//获取当前表的唯一键
        }

        if(pkFs==null || pkFs.length ==0){
            throw  new com.mrhan.db.allrounddaos.exception.EntityException("数据修改异常！当前实体中！未找到可以依赖的主键字段！请尝试用update(T t,String key,Object value)方法！");
        }
        IFeild fi = pkFs[0];

        return   update(t,fi.getColument(),fi.getValue(t));
    }
    /**
     * 修改数据
     *
     * @param t 具体需要修改的类型
     * @return 返回
     */
    @Deprecated
    public boolean update(T t,String key,Object value) {
        int a= 0;
      try{


        com.mrhan.db.SQLStatement st = nowDaoTable.createUpdateSql(t,key,value);
        a= execUpdate(st.getSql(),st.getPro());
      }catch(Exception sql){
          throw new com.mrhan.db.allrounddaos.exception.EntityException("无法修改此纪录！此条数据存在外键引用！"+sql.getMessage());
      }
      return a>0;
    }
    /**
     * 删除数据
     *
     * @param t 删除的数据
     * @return 返回删除结果
     */
    @Override
    public boolean delete(T t) {

        IFeild pkFs[] = nowDaoTable.getPirmaryKey();//获取当前表的主键
        if(pkFs==null || pkFs.length ==0) {//判断是否有主键
            pkFs = nowDaoTable.getUnqiueKey();//获取当前表的唯一键
        }

        if(pkFs==null || pkFs.length ==0){
            throw  new com.mrhan.db.allrounddaos.exception.EntityException("数据修改异常！当前实体中！未找到可以依赖的主键字段！请尝试用delete(T t,String key,Object value)方法！");
        }
        IFeild fi = pkFs[0];


        return delete(t,fi.getColument(),fi.getValue(t));
    }
    @Deprecated
    public boolean delete(T t,String key,Object value){
        int a= 0;
        try{
            com.mrhan.db.SQLStatement st = nowDaoTable.createDeleteSql(t,key,value);
            a= execUpdate(st.getSql(),st.getPro());
        }catch(Exception sql){
            throw new com.mrhan.db.allrounddaos.exception.EntityException("无法删除此纪录！此条数据存在外键引用！"+sql.getMessage());
        }
        return a>0;
    }
    /**
     * 查询数据集合
     *
     * @return
     */
    @Override
    public List<T> selectAll() {
        String table = nowDaoTable.getTableName();//获取当前操作实体类绑定的数据表名
        String sql = "SELECT * FROM  "+table;
        com.mrhan.db.SelectDataInterface sdif = execQuery(sql);
        List<T> allData = new ArrayList<T>();
        getDate(sdif,allData);
        return allData;
    }
    /**
     * 获取数据
     * @param sdif
     * @param allDate
     */
    private void getDate(com.mrhan.db.SelectDataInterface sdif, List<T> allDate){
        String cols[] = sdif.getColNames();
        while(sdif.hasnext()){
            sdif.next();
            T t = (T)nowDaoTable.createEntityObject();//创建实体
            setDate(sdif,t,cols);
            allDate.add(t);
        }
    }

    /**
     *
     * 封装数据
     * @param sdif
     * @param t
     * @param cols
     */
    private void setDate(com.mrhan.db.SelectDataInterface sdif, T t, String ...cols){
        for(int i=0;i<cols.length;i++){
            String colName = cols[i];//获取列名称
            IFeild fi = nowDaoTable.getFeild(colName.toLowerCase());//获取指定的实体字段信息

            if(fi!=null){
                Object value =sdif.getValue(colName);//获取数据集合中的数据
                if(fi.isForginKey()) {//判断字段是否是外键
                    Class forginClass = fi.getFoginClass();//获取外键信息类
                    EntityDaos<Object> en = new EntityDaos<>(idc,dbName,forginClass);//创建外键的操作类
                    String forginCol = fi.getForginKeyCol();//获取外键对于的列明
                    List<Object> alldate = en.selectFindAll(forginCol,value);//查询
                    if(alldate!=null && alldate.size()>0)
                        fi.setValue(t, alldate);

                }else{
                    fi.setValue(t, value);
                }
            }

        }
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
        String table = nowDaoTable.getTableName();//获取当前操作实体类绑定的数据表名
        String sql = "SELECT * FROM  "+table;
        if(key!=null){
            sql+=" where "+key+" = ?";
        }
        com.mrhan.db.SelectDataInterface sdif = execQuery(sql,value);
        List<T> allData = new ArrayList<T>();
        getDate(sdif,allData);
        return allData;

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
        List<T> all = selectFindAll(key,value);
        if(all != null && all.size()>0)
             return all.get(0);
        return null;
    }
    /**
     * 清除表中数据
     *
     * @return
     */
    @Override
    public boolean clearTableDate() {
        int a= 0;
        try{
            a= execUpdate("delete from "+nowDaoTable.getTableName());
        }catch(Exception sql){
            throw new com.mrhan.db.allrounddaos.exception.EntityException("无法删除此纪录！此条数据存在外键引用！"+sql.getMessage());
        }
        return a>0;
    }
    /**
     * 清除表中数据,新的表
     *
     * @return
     */
    @Override
    public boolean newTableDate() {
        int a= 0;
        try{
            a= execUpdate("truncate table  "+nowDaoTable.getTableName());
        }catch(Exception sql){
            throw new com.mrhan.db.allrounddaos.exception.EntityException("无法删除此纪录！此条数据存在外键引用！"+sql.getMessage());
        }
        return a>0;
    }
    /**
     * 判断从表是否存在数据
     * @param tableClass
     * @param value 当前表的一条记录
     * @return
     */
    private boolean isExistsOfFromTable(Class<?> tableClass,T value){
        EntityDaos<?> eds = new EntityDaos<>(idc,dbName,tableClass);
        DaoTable  fdt = tableClass.getAnnotation(DaoTable.class);
        if(fdt!=null) {
            com.mrhan.db.allrounddaos.baseentity.EntityTable table = new com.mrhan.db.allrounddaos.baseentity.EntityTable(fdt);
            //获取所有的外键
            IFeild forginFeilds [] =table.getFeildByType(ColumentType.FORGINKEY);
            forginFeilds = getTableForginFeildByClass(forginFeilds,daoClass);//获取指定此操作实体类的外键
            if(forginFeilds.length>0){
                IFeild fi = forginFeilds[0];
                DaoColument dc = fi.getDaoColument();
                IFeild nowDateFeild = nowDaoTable.getFeild(fi.getDaoColument().col());//获取对应字段表的数据
                if(eds.get(dc.col(),nowDateFeild.getValue(value))!=null){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 检测从表h数据
     * @param value 当前表的一条记录
     * @return
     */
    protected boolean isExistsOfFromTable(T value){
        Class<?> [] allForClass = dt.forginClass();
        for(int i=0;i<allForClass.length;i++){
            if(isExistsOfFromTable(allForClass[i],value)){
                return true;
            }
        }
        return false;
    }
    /**
     * 是否存在从表
     * @return
     */
    public boolean isHasFromTable(){
        Class<?> [] allForClass = dt.forginClass();
        return allForClass.length>0;
    }
    /**
     * 获取指定制定实体类的 外键信息
     * @param forks
     * @param fClass
     * @return
     */
    private IFeild[] getTableForginFeildByClass(IFeild[] forks,Class<?> fClass){
        List<IFeild> allF = new ArrayList<>();
        for(IFeild fi : forks){
            if(fi.getDaoColument().forginClass().equals(fClass)){
                allF.add(fi);
            }
        }
        IFeild []ifs = new IFeild[allF.size()];
        for(int i=0;i<allF.size();i++){
            ifs[i] = allF.get(i);
        }
        return ifs;
    }
    /**
     * 按照指定条件获取值
     * @param sws
     * @return
     */
    public List<T> selectDate(SQLWhere ...sws){
        String table = nowDaoTable.getTableName();//获取当前操作实体类绑定的数据表名

        StringBuilder sb =new StringBuilder("SELECT * FROM ");
        sb.append(table);
        Object os[] = new Object[sws.length];
        if(sws !=null && sws.length>0){
            sb.append(" WHERE ");
            for(int i=0;i<sws.length;i++){
                sb.append(sws[i]);
                if(i<sws.length-1) {
                    sb.append(" ");    sb.append(sws[i].getWhere());  sb.append(" ");

                }
                os[i] = sws[i].getValue();//获取值
            }
        }
        com.mrhan.db.SelectDataInterface sdif = execQuery(sb.toString(),os);
        List<T> allData = new ArrayList<T>();
        getDate(sdif,allData);
        return allData;
    }
}
