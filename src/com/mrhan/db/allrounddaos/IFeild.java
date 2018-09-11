package com.mrhan.db.allrounddaos;

/**
 * 实体类字段信息接口！
 */
public interface IFeild {
    /**
     * 获取字段类型
     * @return
     */
    ColumentType getType();
    /**
     * 是否是外键约束
     * @return
     */
    boolean isForginKey();
    /**
     * 是否是主键
     * @return
     */
    boolean isPirmaryKey();
    /**
     * 如果是外键，获取外键对应的实体类的信息
     * @return
     */
    ITable getForginKey();

    /**
     * 获取外键中对应的字段名称
     * @return
     */
    String getForginKeyCol();

    /**
     * 设置值到具体实体类的字段
     * @param entityObj
     * @param value
     * @return
     */
    boolean setValue(Object entityObj,Object value);
    /**
     * 获取指定实体类的当前字段值
     * @param entityObj
     * @return
     */
    Object getValue(Object entityObj);
    /**
     * 获取当前字段对应表中的字段名称
     * @return
     */
    String getColument();

    /**
     * 获取外键对于的实体类信息
     * @return
     */
    Class getFoginClass();

    /**
     * 获取字段信息
     * @return
     */
    DaoColument getDaoColument();
}
