//package com.mrhan.database;
//
//public class SQLWhere {
//    String key;
//    String where="=";
//    Object value;
//    String join = "AND";
//
//
//
//
//
//    public String getJoin() {
//        return join;
//    }
//
//    public void setJoin(String join) {
//        this.join = join;
//    }
//
//    public SQLWhere(String key, String where, Object value) {
//        this.key = key;
//        this.where = where;
//        this.value = value;
//    }
//    public SQLWhere(String key,  Object value) {
//        this.key = key;
//
//        this.value = value;
//    }
//
//    public String getKey() {
//        return key;
//    }
//
//    @Override
//    public String toString() {
//        return " "+key+" "+where+"=?";
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public String getWhere() {
//        return where;
//    }
//
//    public void setWhere(String where) {
//        this.where = where;
//    }
//
//    public Object getValue() {
//        return value;
//    }
//
//    public void setValue(Object value) {
//        this.value = value;
//    }
//}
