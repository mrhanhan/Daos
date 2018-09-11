package com.mrhan.database.connection;

import com.mrhan.database.mrhanDaos.GeneralConnect;
import com.mrhan.database.IConnect;

import java.sql.SQLException;

public class MySQLConn extends GeneralConnect {

    private String user = "root";
    private String pwd = "123456";
    private String database;

    private String host = "localhost";

    /**
     * 构造函数
     *
     * @param db 操作数据库
     * @throws ClassNotFoundException
     */
    public MySQLConn(String db) throws ClassNotFoundException {
        super("com.mysql.jdbc.Driver");
        database = db;
    }

    /**
     * 构造函数
     *
     * @param host     连接主机
     * @param user     用户
     * @param pwd      密码
     * @param database 操作数据库
     * @throws ClassNotFoundException
     */
    public MySQLConn(String host, String user, String pwd, String database) throws ClassNotFoundException {
        this(database);
        this.host = host;
        this.user = user;
        this.pwd = pwd;
    }

    @Override
    protected String getConnectUrl() {
        StringBuilder sb = new StringBuilder("jdbc:mysql://");
        sb.append(host).append("/").append(database).append("?Character=UTF8&userSSL=true");
        return sb.toString();
    }

    @Override
    protected String getUser() {
        return user;
    }

    @Override
    protected String getUserPwd() {
        return pwd;
    }

    @Override
    public IConnect newConn(boolean isConn) {
        try {
            MySQLConn ms = new MySQLConn(host,user,pwd,database);
            if(isConn){
                ms.getConn();
            }
            return ms;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
