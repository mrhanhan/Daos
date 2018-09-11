package com.mrhan.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接结果！提供接连功能
 */
public interface IConnect {
    /**
     * 获取数据库连接
     *
     * @return
     */
    Connection getConn() throws SQLException;

    /**
     * 关闭连接
     *
     * @param conn
     * @throws SQLException
     */
    boolean close(Connection conn);

    /**
     * 是否连接中
     *
     * @return
     */
    boolean isConn();

    /**
     * 开始事务
     *
     * @return
     */
    boolean beginTrasn();

    /**
     * 提交事务
     *
     * @return
     */
    boolean commitTrans();

    /**
     * 回滚事务
     *
     * @return
     */
    boolean rollbackTrans();

    /**
     * 按照当前配置返回新的连接对象
     *
     * @param isConn 如果为true则，返回来的连接对象，自动将连接打开
     * @return
     */
    IConnect newConn(boolean isConn);
}
