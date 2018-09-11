package com.mrhan.database;

import java.util.List;

/**
 * 万能操作类接口
 */
public interface IEntityDao<T> {
    /**
     * 插入实体
     *
     * @param t
     * @return
     */
    boolean addEntity(T t);

    /**
     * 根据标识列进行修改<br/>
     * 标识顺序：
     * <ul>
     * <li>自动增长</li>
     * <li>主键</li>
     * <li>唯一键</li>
     * </ul>
     *
     * @param t
     * @return 如果标识列或者也有做改动，则无法使用此方法进行修改！
     */
    int updateEntityByIdentifi(T t);

    /**
     * 根据旧模板数据，修改新模板数据
     *
     * @param newEntity
     * @param olderEntity
     * @return
     */
    int updateEntity(T newEntity, T olderEntity);

    /**
     * 删除记录
     *
     * @param t
     * @return
     */
    boolean delete(T t);

    /**
     * 根据实体模板删除数据
     *
     * @param entity
     * @return
     */
    int deleteEntity(T entity);

    /***
     * 查询表里所有记录
     * @return
     */
    List<T> selectAll();

    /**
     * 根据模板查询对象
     *
     * @param entity
     * @return
     */
    List<T> selectByEntity(T entity);

    /**
     * 执行插入语句并返回受影响的行数
     *
     * @param statement 插入语句
     * @return
     */
    int insert(OptionStatement statement);

    /**
     * 修改语句，并返回搜影响的行数
     *
     * @param statement
     * @return
     */
    int update(OptionStatement statement);

    /**
     * 删除语句，返回受影响的防暑
     *
     * @param statement
     * @return
     */
    int delete(OptionStatement statement);


    /**
     * 查询语句，返回查询结果集合
     *
     * @param statement
     * @return
     */
    List<T> select(OptionStatement statement);

    /***
     * 执行查询语句
     * @param selectStatement
     */
    List<T> executeSelect(ISelectStatement<T> selectStatement);
    /**
     * 执行多条查询语句，并得到结果集合
     * @param selectStatements
     * @return
     */
    List<T> executeSelectM(ISelectStatement<T> ...selectStatements);

    /**
     * 多次查询，结果集合，自己处理
     * @param selectStatements
     */
    void select(ISelectStatement ...selectStatements);

    /**
     * 执行多条操作语句!非查询！，并返回受影响的所有行数
     * @param options
     * @return
     */
    int executeUpdate(OptionStatement ...options);
}
