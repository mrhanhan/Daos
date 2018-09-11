package com.mrhan.database.statement;

import com.mrhan.database.ITable;
import com.mrhan.database.OptionStatement;
import com.mrhan.database.mrhanDaos.EntityTable;

public abstract class InsertStatement implements OptionStatement {
    /**
     * 需要操作的实体信息
     */
    protected final EntityTable tableContext;
    protected final Object entity;//操作实体

    /**
     * 插入语句
     *
     * @param tb
     */
    public InsertStatement(EntityTable tb, Object entity) {
        tableContext = tb;
        this.entity = entity;
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
