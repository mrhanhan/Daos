package com.mrhan.database;


import com.mrhan.database.keytype.ForginKey;
import com.mrhan.database.mrhanDaos.ColumType;

/**
 *描述实体类的字段和数据库表中字段的信息接口
 *
 */
public interface IField {
    /**
     * 获取当前字段映射的列名称
     * @return
     */
    String columentName();
    /**
     * 表示此字段是否是自动增长字段
     * @return boolean true 是自动增长
     */
    boolean isAutoKey();

    /**
     * 表示此字段是否是主键字段
     * @return boolean true 是主键字段
     */
    boolean isPirmaryKey();

    /**
     * 表示此字段是否是外键字段
     * @return boolean true 是外键字段
     * @return
     */
    boolean isForginKey();

    /**
     * 表示此字段是否是唯一键键字段
     * @return boolean true 是唯一键字段
     */
    boolean isUniqueKey();

    /**
     * 返回当前字段对应表中的数据类型
     * @see ColumType
     * @return DataType
     */
    ColumType columentType();

    /**
     * 给制定对象的这个字段设置制定数据
     * @param object 所被设置设置数据的对象
     * @param data  需要设置的数据
     * @return 返回设置结果 true 表示设置成功
     */
    boolean setData(Object object, Object data);

    /**
     * 获取外键数据
     * @param entity
     * @return 如果字段类型是实体类型，则获取的是实体对象！如果不是实体，则返回null
     */
    Object getForginData(Object entity);

    /**
     * 获取当前字段的外键信息！
     * @return 如果当前字段不是外键，则返回null！如果是则返回 {@link ForginKey} ForginKey 对象
     */
    com.mrhan.database.keytype.ForginKey forginkey();
    /**
     * 获取指定对象的当前字段的数据（如果是外键，并且字段类型为外键实体类型，请使用getForginDate)
     * @param obj 所需要获取数据的对象
     * @return 返回获取到的具体数据！null代表获取失败或者空
     */
    Object getdate(Object obj);
}
