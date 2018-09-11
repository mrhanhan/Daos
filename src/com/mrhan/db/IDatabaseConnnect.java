package com.mrhan.db;

import java.sql.Connection;

/**
 * 数据库连接接口
 */
public interface IDatabaseConnnect {
    /**
     * 数据路连接方法
     * @param db 连接的数据库
     * @param user 用户名
     * @param pwd 密码
     * @return
     */
     Connection getDBConn(String db,String user,String pwd);
    /**
     * 数据路连接方法
     * @param db 连接的数据库
     *
     * @return
     */
    Connection getDBConn(String db);


}