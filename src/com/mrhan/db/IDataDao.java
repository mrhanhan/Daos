package com.mrhan.db;

import java.util.Collection;
import java.util.List;

/**
 * 数据库操作接口
 * @param <T> 具体操作的数据类型
 */
public interface IDataDao<T> {
    /**
     * 插入数据
     * @param t 具体数据类型
     * @return 插入结果
     */
    boolean insert(T t);

    /**
     * 修改数据
     * @param t 具体需要修改的类型
     * @return 返回
     */
    boolean update(T t);

    /**
     * 删除数据
     * @param t 删除的数据
     * @return 返回删除结果
     */
    boolean delete(T t);
    /**
     * 查询数据集合
     * @param t
     * @return
     */
    List<T> selectAll();
    /**
     * 查询数据集合
     * @param t
     * @return
     */
    List<T> selectFindAll(Object key,Object value);

    /**
     * 获取指定的数据
     * @param key 制定的Key
     * @param value 制定过的值
     * @return 返回指定数据的实体类
     */
    T get(Object key,Object value);

    /**
     * 清除表中数据
     * @return
     */
    boolean clearTableDate();
    /**
     * 清除表中数据,新的表
     * @return
     */
    boolean newTableDate();

}
