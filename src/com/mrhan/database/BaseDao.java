package com.mrhan.database;

import com.mrhan.util.CodeRuntimeTest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * 基础操作类，所有操作实体操作类的基类<br/>
 * 提供操作sql语句执行的操作
 *
 * @param <T>
 * @author MrHanHao
 */

public abstract class BaseDao<T> implements IDao<T>{

    /*连接对象*/
    private IConnect conn;
    /**
     * 无参构造函数*
     *
     * @param iConnect 连接对象
     */
    public BaseDao(IConnect iConnect) {
        conn = iConnect;
    }
    public IConnect getConn(){
        return conn;
    }
    /**
     * 执行增删改的sql语句，并返回受影响的行数
     *
     * @param sql    sql语句
     * @param params 参数值
     * @return 受影响的行数
     */
    protected int executeUpdate(String sql, Object... params) {
        int a = 0;
        java.sql.PreparedStatement ps = null;
        try {
            /* 创建语句并执行 */
            ps = createStatement(sql, params);
      //      System.out.println(ps.toString());
            a = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close(null,ps);
        }
        return a;
    }
    /**
     * 根据携带参数的SQL语句和提供的值，创建预编译操作对象
     *
     * @param sql    携带参数的sql语句
     * @param params 参数值
     * @return 预编译操作对象
     * @throws SQLException
     */
    private java.sql.PreparedStatement createStatement(String sql, Object... params) throws SQLException {
        /* 创建对象，并设置参数值 */
        java.sql.PreparedStatement ps = conn.getConn().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }
    /**
     * 执行查询的sql语句，并返回实体结果集
     *
     * @param sql    sql语句
     * @param params 参数值
     * @return 返回List结果集合，空集合代表无结果
     */
    protected java.util.List<T> executeQuery(String sql, Object... params) {
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        java.util.Vector<T> entitys = new Vector<>();
        try {
            /*打开链接创建语句并执行*/
            ps = createStatement(sql, params);
            rs = ps.executeQuery();
            /*根据结果集创建实体集合*/
            while (rs.next()) {
                entitys.add(getEntity(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs,ps);
        }

        return entitys;
    }

    private void close(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null)
                rs.close();
            if (ps != null) {

                ps.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected <E> List<E> execQuery(String sql, IDao<E> entity, Object... params) {
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        java.util.Vector<E> entitys = new Vector<>();
        try {
            /*打开链接创建语句并执行*/
            ps = createStatement(sql, params);
            rs = ps.executeQuery();
            /*根据结果集创建实体集合*/
            while(rs.next()){
                E e =entity.getEntity(rs);
                entitys.add(e);
            }

        } catch (Exception e) {

        } finally {
            close(rs,ps);
        }
        return entitys;
    }


    /**
     * 查询方法，根据制定对象，来处理结果集合
     * @param sql
     * @param entity
     * @param params
     */
    protected void execQuery(String sql, IEntity entity, Object... params) {
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;

        try {
            /*打开链接创建语句并执行*/
            ps = createStatement(sql, params);
            rs = ps.executeQuery();
            /*根据结果集创建实体集合*/
           entity.entitys(rs);

        } catch (Exception e) {

        } finally {
            close(rs,ps);
        }
    }
}
