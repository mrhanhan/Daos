package com.mrhan.db.datamapping;

import com.mrhan.db.SelectDataInterface;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据转化为实体<br/>
 * 实体类必须有无参的构造函数<br/>
 * 实体类的需要映射的字段必须有MappingColName注解<br/>
 * @param <C>
 */
public class DataConvertEntity<C> {
    private SelectDataInterface sdif;//数据查询结果类
    private Class<C> entityClass;//实体类的类信息
    private Map<String,Field> allCols ;//实体类需要映射的字段信息
    /**
     * 构造函数
     * @param sdif 数据搜集
     * @param entityClass 类的信息
     */
    public DataConvertEntity(SelectDataInterface sdif,Class entityClass) throws ConvertException {
        this.sdif = sdif;
        this.entityClass = entityClass;
        _init();//调用初始化航发
    }
    private void _init() throws ConvertException {
        if(sdif ==null || entityClass ==null){

            throw new ConvertException("数据转换实体异常！数据为空/未知实体类型");
        }
        allCols = new HashMap<>();

        getMappingField(entityClass.getFields());//获取实体类中所有需要映射的字段信息
        getMappingField(entityClass.getDeclaredFields());


    }
    /**
     * 获取实体类中需要映射的字段
     * @param fields
     */
    private void getMappingField(Field fields[]){
        for(int i=0;i<fields.length;i++){
            Field field = fields[i];//遍历
            MappingColName mcn =  field.getAnnotation(MappingColName.class);//获取此字段上的注解
            if(mcn==null){//如果为空则跳过
                continue;
            }
            String colName = mcn.colName().toLowerCase();//获取字段名称
            allCols.put(colName,field);

        }
    }
    /**
     * 获取当前数据集合下的所有实体
     * @return
     */
    public List<C> getAllEntity(){
        List<C> list = new ArrayList<>(10);
        String cols[] = sdif.getColNames();//获取所有的列明

        while(sdif.hasnext()){
            sdif.next();
            C c = getEntityObject();//获取对象信息
            if(c!=null){
                for(int i=0;i<cols.length;i++){
                    String col  = cols[i];
                    Object value = sdif.getValue(col);//获取指定列对应的值
                    Field field = allCols.get(col.toLowerCase());
                    if(!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    if(field!=null){
                        try {
                            Class type =  field.getType();
                            if(type.equals(int.class)){
                                field.setInt(c,Integer.parseInt(value.toString()));
                            }else if(type.equals(float.class) ){
                                field.setFloat(c,Float.parseFloat(value.toString()));
                            }else if(type.equals(double.class)){
                                field.setDouble(c,Double.parseDouble(value.toString()));
                            }else if(type.equals(short.class) ){

                                field.setShort(c,Short.parseShort(value.toString()));
                            }else if(type.equals(long.class) ){
                                field.setLong(c,Long.parseLong(value.toString()));
                            }else if(type.equals(char.class) ){
                                field.setChar(c,value.toString().charAt(0));
                            }else  if( type.equals(Integer.class)){
                                field.set(c,Integer.parseInt(value.toString()));
                            }else if( type.equals(Float.class)){
                                field.set(c,Float.parseFloat(value.toString()));
                            }else if( type.equals(Double.class)){
                                field.setDouble(c,Double.parseDouble(value.toString()));
                            }else if( type.equals(Short.class)){

                                field.set(c,Short.parseShort(value.toString()));
                            }else if( type.equals(Long.class)){
                                field.set(c,Long.parseLong(value.toString()));
                            }else if( type.equals(Character.class)){
                                field.set(c,value.toString().charAt(0));
                            }else{
                                field.set(c,value);
                            }

                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    field.setAccessible(false);
                }
                list.add(c);
            }
        }

        return list;
    }
    /**
     * 获取实体类的对象
     * @return
     */
    private C getEntityObject(){
        try {
            return  entityClass.getConstructor().newInstance();
        } catch (InstantiationException e) {
            System.err.println("确保实体类是public的");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("确保实体类是public的");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("确保实体类是public的");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("确保实体类是public的");
            e.printStackTrace();
        }
        return null;
    }
}
