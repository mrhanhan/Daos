package com.mrhan.db.connection;

import com.mrhan.db.IDatabaseConnnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SQL Server 连接数据库类
 */
public class SQLServerDBConnnect implements IDatabaseConnnect {

    private String name = "sa";//连接用户名
    private String pwd = "123456"; //连接密码
    private int port = 1433;
    private String host = "localhost";

    public SQLServerDBConnnect(String user,String pwd){
        this.name = user;
        this.pwd = pwd;
        loadClass();
    }

    public SQLServerDBConnnect(){ loadClass();}


    /**
     * 加载驱动
     */
    private void loadClass(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据路连接方法
     *
     * @param db   连接的数据库
     * @param user 用户名
     * @param pwd  密码
     * @return
     */
    @Override
    public Connection getDBConn(String db, String user, String pwd) {
        String url = "jdbc:sqlserver://"+host+":"+port+";database="+db;

        try {

          return  DriverManager.getConnection(url,user,pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 数据路连接方法
     *
     * @param db 连接的数据库
     * @return
     */
    @Override
    public Connection getDBConn(String db) {
        return getDBConn(db,name,pwd);
    }
}
