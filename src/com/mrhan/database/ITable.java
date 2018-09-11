package com.mrhan.database;

import java.util.List;
import java.util.Set;

/**
 * 描述数据库中的表和实体类的关系接口
 *
 */
public interface ITable {
    /**
     * 获取当前实体类对应的表名
     * @return String 返回实体类上映射的表明
     */
    String getTableName();

    /**
     * 获取本实体所有映射的字段名称
     * @return
     */
    Set<String> getColumNames();

    /**
     * 获取对应的实体类的类信息 类模板
     * @return 返回实体类的类模板 {@link Class}
     */
    Class<?> getEntityClass();

    /**
     * 获取指定类型的字段信息
     * @param type 注解类型的Class <br/>
     *         限定于此包下的类：{@link com.mrhan.database.keytype}
     * @return ArrayList&lt;IField&gt; {@link java.util.ArrayList} 返回字段对象集合
     */
    List<IField> findTypeField(Class<?> type);
    /**
     * 返回当前实体类中所有需要映射 的字段关系对象
     * @return  {@link IField}
     */
    Set<IField> fields();

    /**
     * 获取表中标识字段！
     * <br/>
     * 自动增长->主键->唯一键
     * @return 如果不存在！返回null
     */
    IField getIdent();

    /**
     * 获取此表的别名
     * @return
     */
    String getTableAsName();

    /**
     * 根据实体类对应的表中，获取指定列明（字段名）的字段关系对象
     * @param columentName 数据表中字段（列）名称
     * @return IFeild 映射关系对象
     */
    IField getField(String columentName);
    /**
     * 创建一个新的实体对象，创建前，请保证此实体类有无参数的构造函数
     * @param <E> 实体类型
     * @return 返回一个实体类型的对象
     */
    <E extends Object> E createEntity();

}
