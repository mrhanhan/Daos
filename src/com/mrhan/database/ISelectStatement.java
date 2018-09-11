package com.mrhan.database;

/**
 * 查询语句接口
 * @param <T>
 */
public interface ISelectStatement<T> extends IDao<T> ,OptionStatement ,IEntity{
}
