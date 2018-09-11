package com.mrhan.database.mrhanDaos;

import com.mrhan.database.ISelectStatement;
import com.mrhan.database.ITable;

import java.sql.ResultSet;

/**
 * 基本封装操作类
 * @param <T>
 */
public abstract class GenericSelectStatement<T> implements ISelectStatement<T> {

    @Override
    public void entitys(ResultSet rs) {

    }

    @Override
    public String getSQLStatemenet(ITable table) {
        return null;
    }

    @Override
    public Object[] getParams() {
        return new Object[0];
    }
}
