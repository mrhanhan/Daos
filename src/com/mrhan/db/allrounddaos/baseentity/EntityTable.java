package com.mrhan.db.allrounddaos.baseentity;

import com.mrhan.db.allrounddaos.IFeild;
import com.mrhan.db.allrounddaos.exception.EntityException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 实体表,记录实体类的信息
 */
public class EntityTable implements com.mrhan.db.allrounddaos.ITable {

    private static Map<String, com.mrhan.db.allrounddaos.ITable> loadTableCLass = new HashMap<>();//保存加载过的ITable

    /**
     * 判断是否加载过制定的表
     * @param c
     * @return
     */
    private static boolean isLoad(Class c){
        return loadTableCLass.get(c.getPackage()+c.getName())!=null;
    }

    /**
     * 获取已经加载过的Table
     * @param dt
     * @return
     */
    public static com.mrhan.db.allrounddaos.ITable getTable(com.mrhan.db.allrounddaos.DaoTable dt){
        Class c =dt.entityClass();
        if(isLoad(dt.entityClass())){
            return loadTableCLass.get(c.getPackage()+c.getName());
        }
            EntityTable t   = new EntityTable(dt);
              t.loadInit();
              return t;
    }


    private String tableName;//实体类对应的表明
    private Class<?> entityClass;//实体类所对应的Class对象
    private Map<String, com.mrhan.db.allrounddaos.IFeild> allFeilds;
    private com.mrhan.db.allrounddaos.DaoTable nowDt;//注解

    public EntityTable(com.mrhan.db.allrounddaos.DaoTable dt){
        this.nowDt=dt;



    }
    /**
     * 初始化方法
     */
    public void loadInit(){
        allFeilds = new HashMap<>();
        //获取表明
        tableName = nowDt.table();
        entityClass = nowDt.entityClass();//获取实体类的Class对象
        if(entityClass==null){
            throw new EntityException("实体类配置有无！无法找打此实体类的Class信息");
        }
        parseFeild(entityClass.getDeclaredFields());
        parseFeild(entityClass.getFields());


    }

    /**
     * 字段信息对象转换为映射对象方法
     * @param fs
     */
    private void parseFeild(Field fs[]){
        for(int i=0;i<fs.length;i++){
            Field fi = fs[i];
            fi.setAccessible(true);//设置可以被访问

            com.mrhan.db.allrounddaos.DaoColument dc = fi.getAnnotation(com.mrhan.db.allrounddaos.DaoColument.class);//获取字段注解
            if(dc!=null){

                if(dc.colType()== com.mrhan.db.allrounddaos.ColumentType.FORGINKEY){//判读是否是外键
                    if(entityClass.equals(dc.forginClass())){
                        throw new EntityException("实体类重复引用！位置："+entityClass.getName()+"."+fi.getName());
                    }
                }
                com.mrhan.db.allrounddaos.baseentity.EntityField ef = new com.mrhan.db.allrounddaos.baseentity.EntityField(dc,fi);
                allFeilds.put(dc.col().toLowerCase(),ef);
            }
            fi.setAccessible(false);
        }
    }

    /**
     * 获取实体类对于的表名称
     *
     * @return
     */
    @Override
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取实体类对于的Class
     *
     * @return
     */
    @Override
    public Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * 获取指定表中字段所绑定 字段信息
     *
     * @param colName
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.IFeild getFeild(String colName) {
        return allFeilds.get(colName.toLowerCase());
    }

    /**
     * 获取当前 实体下所有的字段信息
     *
     * @return
     */
    @Override
    public Set<com.mrhan.db.allrounddaos.IFeild> getFeild() {
        Iterator<String> ks =  allFeilds.keySet().iterator();
        Set<com.mrhan.db.allrounddaos.IFeild> allF = new HashSet<>();
        while (ks.hasNext()){
            com.mrhan.db.allrounddaos.IFeild i = allFeilds.get(ks.next());
            if(!allF.contains(i)){
                allF.add(i);
            }
        }

        return allF;
    }

    /**
     * 创建实体对象
     *
     * @return
     */
    @Override
    public Object createEntityObject() {
        if(entityClass!=null){
            try {
                Constructor c  =  entityClass.getConstructor();
                c.setAccessible(true);

                return c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 创建查询语句
     *
     * @return
     */
    @Override
    public com.mrhan.db.SQLStatement createInsertSql(Object obj) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        StringBuilder values  = new StringBuilder("VALUES(");
        List<Object> allPro = new ArrayList<>();
        sb.append(tableName);
        sb.append("(");
        Iterator<String> its = allFeilds.keySet().iterator();
        while (its.hasNext()){
            com.mrhan.db.allrounddaos.IFeild fi = allFeilds.get(its.next());//获取每一项的值
            if(fi.getDaoColument().isGrowth()){
                continue;
            }
            sb.append(fi.getColument());//插入需要插入数据的字段
            values.append("?");
            if(fi.isForginKey()){
                Class<?> c = fi.getFoginClass();
                if(c==null){
                    throw new EntityException("外键配置有误"+getEntityClass().getPackage()+"."+entityClass.getName());
                }
                com.mrhan.db.allrounddaos.DaoTable dt =c.getAnnotation(com.mrhan.db.allrounddaos.DaoTable.class);
                if(dt==null){
                    throw new EntityException("外键配置有误"+getEntityClass().getPackage()+"."+entityClass.getName());
                }

                com.mrhan.db.allrounddaos.ITable et = EntityTable.getTable(dt);

                String forColName = fi.getForginKeyCol();//获取外键映射字段
                com.mrhan.db.allrounddaos.IFeild fii =  et.getFeild(forColName);//获取字段信息
                /**
                 * 外键是复合类型
                 */
                Object forObj = fi.getValue(obj);
                allPro.add(fii.getValue( forObj ));//获取参数
            }else {
                allPro.add(fi.getValue(obj));//获取参数
            }
            if(its.hasNext()) {
                sb.append(",");//如果有下一行插入,
                values.append(",");
            }
        }
        sb.append(") ");//如果有下一行插入,
        values.append(")");

        return new com.mrhan.db.SQLStatement(sb.toString()+values,allPro.toArray());
    }

    /**
     * 创建修改语句
     *
     * @return
     */
    @Override
    public com.mrhan.db.SQLStatement createUpdateSql(Object obj, String key, Object value) {
        StringBuilder sb = new StringBuilder("UPDATE  ");
        sb.append(tableName).append(" SET ");

        List<Object> allPro = new ArrayList<>();

        Iterator<String> its = allFeilds.keySet().iterator();
        while (its.hasNext()){
            com.mrhan.db.allrounddaos.IFeild fi = allFeilds.get(its.next());//获取每一项的值

            sb.append(fi.getColument()).append(" =? ");//插入需要插入数据的字段
            if(fi.isForginKey()){
                Class<?> c = fi.getFoginClass();
                if(c==null){
                    throw new EntityException("外键配置有误"+getEntityClass().getPackage()+"."+entityClass.getName());
                }
                com.mrhan.db.allrounddaos.DaoTable dt =c.getAnnotation(com.mrhan.db.allrounddaos.DaoTable.class);
                if(dt==null){
                    throw new EntityException("外键配置有误"+getEntityClass().getPackage()+"."+entityClass.getName());
                }

                com.mrhan.db.allrounddaos.ITable et = EntityTable.getTable(dt);

                String forColName = fi.getForginKeyCol();//获取外键映射字段
                com.mrhan.db.allrounddaos.IFeild fii =  et.getFeild(forColName);//获取字段信息
                /**
                 * 外键是复合类型
                 */
                Object forObj = fi.getValue(obj);
                allPro.add(fii.getValue( forObj ));//获取参数
            }else {
                allPro.add(fi.getValue(obj));//获取参数
            }
            if(its.hasNext()) {
                sb.append(",");//如果有下一行插入,
            }
        }
        //判断条件是否为空
        if(!key.isEmpty()){
            sb.append(" WHERE ").append(key).append(" = ?");
        }
        allPro.add(value);//获取参数
        return  new com.mrhan.db.SQLStatement(sb.toString(),allPro.toArray());
    }

    /**
     * 创建修改语句
     *
     * @param obj
     * @param key
     * @param value
     * @return
     */
    @Override
    public com.mrhan.db.SQLStatement createDeleteSql(Object obj, String key, Object value) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(tableName);

        List<Object> allPro = new ArrayList<>();


        //判断条件是否为空
        if(!key.isEmpty()){
            sb.append(" WHERE ").append(key).append(" =  ?");
        }
        allPro.add(value);//获取参数
        return  new com.mrhan.db.SQLStatement(sb.toString(),allPro.toArray());
    }

    /**
     * 获取主键
     *
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.IFeild[] getPirmaryKey() {

        List<com.mrhan.db.allrounddaos.IFeild> allP = new ArrayList<com.mrhan.db.allrounddaos.IFeild>();
        Iterator<String> its = allFeilds.keySet().iterator();
        while (its.hasNext()) {
            com.mrhan.db.allrounddaos.IFeild fi = allFeilds.get(its.next());//获取每一项的值
            if(fi.isPirmaryKey())
                allP.add(fi);
        }
        Object obs [] = allP.toArray();
        com.mrhan.db.allrounddaos.IFeild ifs[] = new com.mrhan.db.allrounddaos.IFeild[obs.length];
        for(int i=0;i<ifs.length;i++){
            ifs[i] = (com.mrhan.db.allrounddaos.IFeild)obs[i];
        }
        return  ifs;
    }

    /**
     * 获取外键键
     *
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.IFeild[] getUnqiueKey() {
        List<com.mrhan.db.allrounddaos.IFeild> allP = new ArrayList<>();
        Iterator<String> its = allFeilds.keySet().iterator();
        while (its.hasNext()) {
            com.mrhan.db.allrounddaos.IFeild fi = allFeilds.get(its.next());//获取每一项的值
            if(fi.getType()== com.mrhan.db.allrounddaos.ColumentType.UNIQUE)
                allP.add(fi);
        }
        Object obs [] = allP.toArray();
        com.mrhan.db.allrounddaos.IFeild ifs[] = new com.mrhan.db.allrounddaos.IFeild[obs.length];
        for(int i=0;i<ifs.length;i++){
            ifs[i] = (com.mrhan.db.allrounddaos.IFeild)obs[i];
        }
        return  ifs;
    }

    /**
     * 获取指定类型的建
     *
     * @param type
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.IFeild[] getFeildByType(com.mrhan.db.allrounddaos.ColumentType type) {
        List<com.mrhan.db.allrounddaos.IFeild> allP = new ArrayList<>();
        Iterator<String> its = allFeilds.keySet().iterator();
        while (its.hasNext()) {
            com.mrhan.db.allrounddaos.IFeild fi = allFeilds.get(its.next());//获取每一项的值
            if(fi.getType()==type)
                allP.add(fi);
        }
        Object obs [] = allP.toArray();
        com.mrhan.db.allrounddaos.IFeild ifs[] = new com.mrhan.db.allrounddaos.IFeild[obs.length];
        for(int i=0;i<ifs.length;i++){
            ifs[i] = (IFeild)obs[i];
        }
        return  ifs;
    }
}
