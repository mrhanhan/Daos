package com.mrhan.db;

import com.mrhan.db.allrounddaos.exception.EntityException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作数据库基类
 */
public abstract class AbsDatabaseDao<T> implements com.mrhan.db.IDataDao<T> {
    private class DBConnection{
        /**
         * 数据库连接对象
         */
        public Connection connection;
        /**
         * 是否正在连接
         */
        private boolean isConnection;

    }

    /**
     * 存放制定语句对应的操作对象
     */
    private volatile Map<String,PreparedStatement> preparedStatements = new HashMap<>();

//    private boolean isConnection = false;//是否连接
    private IDatabaseConnnect databaseConnnect;//数据库连接实现对象
//    protected Connection databaseConn;//数据库连接对象
    private String dbName;//操作的数据库名称
    /**
     * 连接管理对象
     */
    private DBConnection connection = new DBConnection();
    /**
     * 构造函数
     * @param idc 数据库连接类
     * @param dbName 操作的数据库名称
     */
    public AbsDatabaseDao(IDatabaseConnnect idc,String dbName){
        if(idc!=null){
            this.databaseConnnect = idc;

            this.dbName = dbName;
            __init();
        }
    }
    public AbsDatabaseDao(AbsDatabaseDao add){
        if(add!=null){
            this.databaseConnnect = add.databaseConnnect;

            this.dbName = add.dbName;
            if(add.connection!=null){
                this.connection = add.connection;
            }

            __init();
        }
    }

    protected  void __init(){

    }

    protected IDatabaseConnnect getDatabaseConnnect(){
        return databaseConnnect;
    }
    protected String getDBName(){
        return dbName;
    }

    /**
     * 数据库的指令执行 增 删 改 操作
     * @param sql sql语句
     * @param pros 参数数组
     * @return 返回受影响行数
     */
    protected int execUpdate(String sql,Object ... pros) throws SQLException {
        openConn();
        int a= 0;

        PreparedStatement ps =  null;//获取之前使用过的操作对象

        ps =  connection.connection.prepareStatement(sql);//创建预编译操作对象
            for(int i=0;i<pros.length;i++){//设置参数
                ps.setObject(i+1,pros[i]);
            }
            String p = ps.toString();
            a = ps.executeUpdate();//执行并返回受影响的行数

            ps.close();

        return a;
    }

    /**
     * 判断指定表中是否存在制定字段和值的数据
     * @param tbName
     * @param colument
     * @param value
     * @return
     */
    protected boolean isExists(String tbName ,String colument,String value){
        String sql = "select * from "+tbName+" where "+colument+" = ?";
        com.mrhan.db.SelectDataInterface sdif = execQuery(sql,value);
        if(sdif==null)
            return false;
        return sdif.hasnext();
    }

    protected com.mrhan.db.SelectDataInterface execQuery(String sql, Object ...pros){
        openConn();
        com.mrhan.db.SelectDataInterface sdif = null;

     try {

         PreparedStatement ps =  null;//获取之前使用过的操作对象

         ps =  connection.connection.prepareStatement(sql);//创建预编译操作对象


         for(int i=0;i<pros.length;i++){//设置参数
             ps.setObject(i+1,pros[i]);
         }


         ResultSet rs = ps.executeQuery();//执行并返回受结果集合

             sdif = new com.mrhan.db.DataSet(rs);

            rs.close();


     } catch (SQLException e) {
         throw new EntityException(e.getMessage());
     }

       // crt.showTimeMsg("close：");
     return sdif;
     }
     /**
     * 打开连接
     */
    protected void openConn(){


        Connection databaseConn = connection.connection;
        boolean isConnection = connection.isConnection;
            if( databaseConn==null || !isConnection) {

                createNewConn();

            }

    }
    /**
     * 关闭连接
     */
    public void closeConn(){
        try {
            Connection databaseConn = connection.connection;
            boolean isConnection = connection.isConnection;
            if(databaseConn!=null && isConnection){
                databaseConn.close();
                connection.isConnection = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建新的连接
     */
    public void createNewConn(){
        this.connection.connection = this.databaseConnnect.getDBConn(dbName);
        this.connection.isConnection = true;
    }
    /**
     * 获取操作数据库名称
     * @return
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 设置需要操作数据库的名称
     * @param dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public void setDatabaseConnnect(IDatabaseConnnect databaseConnnect) {
        this.databaseConnnect = databaseConnnect;
    }

    /**
     * 关闭资源方法
     */
    public void closeResource(){
       closeConn();
    }

    /**
     * 开始事务
     * @return 如果开始成功 返回true
     */
    protected boolean beginTransaction(){
        openConn();//打开连接
        try {
            connection.connection.setAutoCommit(false);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;

    }
    /**
     * 提交事务
     * @return 提交成功返回true
     */
    protected boolean commitTranscation(){
        try {
            connection.connection.commit();
            endTranscation();
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    /**
     * 回滚事务
     * @return 成功返回true
     */
    protected boolean rollbackTranscation(){
        try {
            connection.connection.rollback();
            endTranscation();
            return true;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }
    /**
     * 结束事务
     * @throws SQLException
     */
    private void endTranscation() throws SQLException {
        connection.connection.setAutoCommit(true);

    }

}
