package com.mrhan.db.allrounddaos;

/**
 * 实体类的字段信息
 * 默认，外键
 */
public enum ColumentType {
    /**
     * 默认类型
     */
    DEFAULT,
    /**
     * 字段为外键
     */
    FORGINKEY,
    /**
     * 字段为主键
     */
    PIRMARYKEY,
    /**
     * 字段为为一建
     */
    UNIQUE
}
