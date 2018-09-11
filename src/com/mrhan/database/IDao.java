package com.mrhan.database;

import java.sql.ResultSet;

/**
 * 通过结果集数据操作实体
 *
 * @param <T>
 */
public interface IDao<T> {
    /**
     * 根据结果集合获取指定实体对象
     * ，此方法是多次回调方阿飞<br/>
     * 调用此方法结构:
     * <br/>
     * <code>
     * while(rs.next()){
     * T = getEntity(rs);
     * }
     *
     * </code><br/>
     * <code><br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;while(rs.next()){<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;T = getEntity(rs);<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
     *
     *  </code><br/>
     * 所以无需再此方法中继续next下一行数据
     *
     * @param rs 结果集合对象
     * @return
     */
    T getEntity(ResultSet rs);
}
