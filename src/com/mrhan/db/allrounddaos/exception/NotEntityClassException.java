package com.mrhan.db.allrounddaos.exception;

/**
 * 实体类配置错误
 */
public class NotEntityClassException extends EntityException{
    public NotEntityClassException(String msg){
        super(msg);
    }
}
