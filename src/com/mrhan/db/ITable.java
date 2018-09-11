package com.mrhan.db;

import com.mrhan.db.allrounddaos.IFeild;

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
     * 获取对应的实体类的类信息 类模板
     * @return 返回实体类的类模板 {@link Class}
     */
    Class<?> getEntityClass();

    /**
     * 获取指定类型的字段信息
     * @param type 注解类型的Class <br/>
     *         限定于此包下的类：{@link com.mrhan.database.entitydaos.keytype}
     * @return ArrayList&lt;IField&gt; {@link java.util.ArrayList} 返回字段对象集合
     */
    List<IField> findTypeField(Class<?> type);
    /**
     * 返回当前实体类中所有需要映射 的字段关系对象
     * @return  {@link IFeild}
     */
    Set<IField> fields();
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
    /**
     * 根据此实体类的模板，和指定获取数据源的对象来创建插入语句！
     * @param entityObj 模板需要获取数据的实体类具体对象
     * @return 返回 SQLStatement 返回具体的语句对象
     * @see SQLStatement
     */
    SQLStatement createInset(Object entityObj);
    /**
     * 根据此实体类的模板，和指定获取数据源的对象来创建修改语句！
     * @param entityObj 模板需要获取数据的实体类具体对象
     * @return 返回 SQLStatement 返回具体的语句对象
     * @see SQLStatement
     */
    SQLStatement createUpdate(Object entityObj, SQLWhere... where);
    /**
     * 根据此实体类的模板，和指定获取数据源的对象来创建插入语句！
     * @param entityObj 模板需要获取数据的实体类具体对象
     * @return 返回 SQLStatement 返回具体的语句对象
     * @see SQLStatement
     */
    SQLStatement createDelete(Object entityObj, SQLWhere... where);

    /**
     * 创建查询语句
     * @param where 条件
     * @return 返回 SQLStatement 返回具体的语句对象
     * @see SQLStatement
     */
    SQLStatement createSelect(SQLWhere... where);
}
