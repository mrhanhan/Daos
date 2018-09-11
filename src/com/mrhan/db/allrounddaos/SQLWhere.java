package com.mrhan.db.allrounddaos;

/**
 * sql条件
 */
public class SQLWhere {
    private String colName;
    private Object value;
    private String where = "AND";//条件

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public SQLWhere(String colName, Object value, String where) {

        this.colName = colName;
        this.value = value;
        this.where = where;
    }

    public SQLWhere(String colName, Object value) {
        this.colName = colName;
        this.value = value;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return colName+" = ?";
    }
}
