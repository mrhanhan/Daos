package com.mrhan.database.mrhanDaos;

import com.mrhan.database.IConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public abstract class GeneralConnect implements IConnect {
    /*驱动加载集合*/
    private static final Set<String> SET_DRIVE = new HashSet<>();
    /**
     * 数据库连接对象
     */
    private volatile Connection connection;

    /**
     * 连接对象！
     *
     * @param driveClass 驱动名称
     */
    public GeneralConnect(String driveClass) throws ClassNotFoundException {
        /*如果不存在，说明未加载过驱动程序*/
        if (!SET_DRIVE.contains(driveClass)) {
            Class.forName(driveClass);
            SET_DRIVE.add(driveClass);
        } else {
        }
    }

    /**
     * 获取连接字符串
     *
     * @return
     */
    protected abstract String getConnectUrl();

    /**
     * 获取用户名
     *
     * @return
     */
    protected abstract String getUser();

    /**
     * 获取连接用户密码
     *
     * @return
     */
    protected abstract String getUserPwd();

    @Override
    public synchronized Connection getConn() throws SQLException {
        /*判断状态*/
        if (!isConn()) {
            connection = DriverManager.getConnection(getConnectUrl(), getUser(), getUserPwd());
        }
        return connection;
    }

    public boolean close() {
        return close(this.connection);
    }

    @Override
    public boolean close(Connection conn) {
        try {
            if(isConn())
                conn.close();
            return true;
        } catch (SQLException e) {

        }
        return false;

    }

    @Override
    public boolean isConn() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public boolean beginTrasn() {
        if (isConn()) {
            try {
                connection.setAutoCommit(false);
                return true;
            } catch (SQLException e) {
            }
        }
        return false;
    }

    @Override
    public boolean commitTrans() {
        if (isConn()) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.commit();
                    connection.setAutoCommit(true);//关闭事务
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean rollbackTrans() {
        if (isConn()) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    connection.setAutoCommit(true);//关闭事务
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
