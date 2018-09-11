package com.mrhan.db;

import java.sql.ResultSet;
import java.util.Arrays;

public class SQLStatement {
    private String sql;
    private Object [] pro;
    public SQLStatement(String sql,Object ...pros){
        this.sql =sql;
        this.pro = pros;

    }

    public Object[] getPro() {
        return pro;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return "SQLStatement{" +
                "sql='" + sql + '\'' +
                ", pro=" + Arrays.toString(pro) +
                '}';
    }
}
