package com.mrhan.db.allrounddaos.baseentity;

import com.mrhan.db.allrounddaos.IFeild;
import com.mrhan.db.allrounddaos.exception.EntityException;
import com.mrhan.db.allrounddaos.exception.NotEntityClassException;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体类字段名称
 */
public class EntityField implements IFeild {

    private com.mrhan.db.allrounddaos.DaoColument dc;//字段注解信息
    private Field field;//字段信息
    private Class<?> forginClass;//外键实体 Class对象
    private com.mrhan.db.allrounddaos.ITable table;
    public EntityField(com.mrhan.db.allrounddaos.DaoColument dc, Field field){
        this.dc = dc;
        this.field = field;
        _init();
    }

    /**
     * 初始化方法
     */
    private void _init() {

        if(isForginKey()){//判断是否是外键
            forginClass = dc.forginClass();//获取注解中配置的信息
            if(forginClass == null){
                throw new NotEntityClassException("没有配置Class类型 entityClass = null");
            }
            //获取外键实体类的信息
            com.mrhan.db.allrounddaos.DaoTable dt =  forginClass.getAnnotation(com.mrhan.db.allrounddaos.DaoTable.class);
            if(dt==null){
                throw new EntityException("实体类中没有配置 DaoTable  无法找到外键实体");

            }

                this.table = EntityTable.getTable(dt);


        }
    }

    /**
     * 获取字段类型
     *
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.ColumentType getType() {
        return dc.colType();
    }

    /**
     * 是否是外键约束
     *
     * @return
     */
    @Override
    public boolean isForginKey() {
        return dc.colType() == com.mrhan.db.allrounddaos.ColumentType.FORGINKEY;
    }

    /**
     * 是否是主键
     *
     * @return
     */
    @Override
    public boolean isPirmaryKey() {
        return dc.colType() == com.mrhan.db.allrounddaos.ColumentType.PIRMARYKEY;
    }

    /**
     * 如果是外键，获取外键对应的实体类的信息
     *
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.ITable getForginKey() {
        return table;
    }

    /**
     * 获取外键中对应的字段名称
     *
     * @return
     */
    @Override
    public String getForginKeyCol() {
        return dc.forginKey();
    }

    /**
     * 设置值到具体实体类的字段
     *
     * @param
     * @param value
     * @return
     */
    @Override
    public boolean setValue(Object c, Object value) {
        try {
            Class type =  field.getType();

            field.setAccessible(true);
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

                if(value instanceof List)
                {
                    List list = ((List) value);
                    if(list!=null && list.size()>0) {
                        if (type.isArray()) {
                            field.set(c, ((List) value).toArray());
                        } else {
                            field.set(c, list.get(0));
                        }
                    }

                }else {
                    if(field.getType()==String.class)
                        value = value+"";
                    field.set(c, value);
                }

        }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(false);
        return false;
    }

    /**
     * 获取指定实体类的当前字段值
     *
     * @param entityObj
     * @return
     */
    @Override
    public Object getValue(Object entityObj) {
        Object obj= null;
        if(field!=null){
            field.setAccessible(true);
            try {
                obj = field.get(entityObj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        return obj;
    }

    /**
     * 获取当前字段对应表中的字段名称
     *
     * @return
     */
    @Override
    public String getColument() {
        return dc.col();
    }

    /**
     * 获取外键对于的实体类信息
     *
     * @return
     */
    @Override
    public Class getFoginClass() {
        return forginClass;
    }

    /**
     * 获取字段信息
     *
     * @return
     */
    @Override
    public com.mrhan.db.allrounddaos.DaoColument getDaoColument() {
        return  dc;
    }
}
