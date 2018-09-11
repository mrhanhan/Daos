package com.mrhan.db.allrounddaos;

import java.util.Set;

public interface ITable {

    /**
     * 获取实体类对于的表名称
     * @return
     */
    String getTableName();

    /**
     * 获取实体类对于的Class
     * @return
     */
    Class<?> getEntityClass();

    /**
     * 获取指定表中字段所绑定 字段信息
     * @param colName
     * @return
     */
    com.mrhan.db.allrounddaos.IFeild getFeild(String colName);

    /**
     * 获取当前 实体下所有的字段信息
     * @return
     */
    Set<com.mrhan.db.allrounddaos.IFeild> getFeild();

    /**
     * 创建实体
     * @return
     */
    Object createEntityObject();

    /**
     * 创建查询语句
     * @return
     */
    com.mrhan.db.SQLStatement createInsertSql(Object obj);

    /**
     * 创建修改语句
     * @return
     */
    com.mrhan.db.SQLStatement createUpdateSql(Object obj, String key, Object value);

    /**
     * 创建修改语句
     * @return
     */
    com.mrhan.db.SQLStatement createDeleteSql(Object obj, String key, Object value);


    /**
     * 获取主键
     * @return
     */
    com.mrhan.db.allrounddaos.IFeild[] getPirmaryKey();
    /**
     * 获取外键键
     * @return
     */
    com.mrhan.db.allrounddaos.IFeild[] getUnqiueKey();

    /**
     * 获取指定类型的建
     * @param type
     * @return
     */
    com.mrhan.db.allrounddaos.IFeild[] getFeildByType(com.mrhan.db.allrounddaos.ColumentType type);
}
