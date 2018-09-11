package com.mrhan.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 *作者：MrHan
 */
public class EntityField implements IField {


    /**
     * 字段注解
     */
    private com.mrhan.db.entitydaos.Colument colument;
    /***
     * 外键注解
     */
    private com.mrhan.db.entitydaos.keytype.ForginKey forginKey;
    /**
     * 主键注解
     */
    private com.mrhan.db.entitydaos.keytype.PrimaryKey primaryKey;
    /**
     * 唯一键注解
     */
    private com.mrhan.db.entitydaos.keytype.UniqueKey uniqueKey;
    /**
     * 自动增长注解
     */
    private com.mrhan.db.entitydaos.keytype.AutoKey autoKey;
    /**
     * 字段信息
     */
    private Field field;
    /**
     * 数据类型信息
     */
    private com.mrhan.db.entitydaos.DataType dataType;
    /**
     * 字段信息构造函数
     * @param f 字段对象
     * @param colument 表字段注解对象
     */
    public EntityField(Field f , com.mrhan.db.entitydaos.Colument colument){
        this.field  = f;
        this.colument = colument;

        init();
    }

    private void init() {
        this.dataType = this.colument.type();
        this.autoKey = field.getAnnotation(com.mrhan.db.entitydaos.keytype.AutoKey.class);
        this.primaryKey = field.getAnnotation(com.mrhan.db.entitydaos.keytype.PrimaryKey.class);
        this.uniqueKey = field.getAnnotation(com.mrhan.db.entitydaos.keytype.UniqueKey.class);
        this.forginKey = field.getAnnotation(com.mrhan.db.entitydaos.keytype.ForginKey.class);
    }

    /**
     * 获取当前字段映射的列名称
     *
     * @return
     */
    @Override
    public String columentName() {
        return colument.col();
    }

    /**
     * 表示此字段是否是自动增长字段
     *
     * @return boolean true 是自动增长
     */
    @Override
    public boolean isAutoKey() {
        return this.autoKey!=null;
    }
    /**
     * 表示此字段是否是主键字段
     *
     * @return boolean true 是主键字段
     */
    @Override
    public boolean isPirmaryKey() {
        return this.primaryKey!=null;
    }
    /**
     * 表示此字段是否是外键字段
     *
     * @return
     */
    @Override
    public boolean isForginKey() {
        return this.forginKey!=null;
    }
    /**
     * 表示此字段是否是唯一键键字段
     *
     * @return boolean true 是唯一键字段
     */
    @Override
    public boolean isUniqueKey() {
        return this.uniqueKey!=null;
    }
    /**
     * 返回当前字段对应表中的数据类型
     *
     * @return DataType
     * @see com.mrhan.db.entitydaos.DataType
     */
    @Override
    public com.mrhan.db.entitydaos.DataType columentType() {
        return this.colument.type();
    }
    /**
     * 给制定对象的这个字段设置制定数据
     *
     * @param object 所被设置设置数据的对象
     * @param data   需要设置的数据
     * @return 返回设置结果 true 表示设置成功
     */
    @Override
    public boolean setData(Object object, Object data) {


        return false;
    }

    /**
     * 获取当前字段的外键信息！
     *
     * @return 如果当前字段不是外键，则返回null！如果是则返回 {@link com.mrhan.db.entitydaos.keytype.ForginKey} ForginKey 对象
     */
    @Override
    public com.mrhan.db.entitydaos.keytype.ForginKey forginkey() {
        return forginKey;
    }

    /**
     * 获取指定对象的当前字段的数据
     *
     * @param obj 所需要获取数据的对象
     * @return 返回获取到的具体数据！null代表获取失败或者空
     */
    @Override
    public <D> D getdate(Object obj) {
        D d = null;
        try {
            d = this.dataType.getDate(field,obj);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return d;
    }
}
