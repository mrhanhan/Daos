package com.mrhan.db.allrounddaos.exception;

/**
 * 实体类错误
 */
public class EntityException  extends NullPointerException {
    public EntityException(String msg){
        super(msg);
    }
}
