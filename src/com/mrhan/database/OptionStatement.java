package com.mrhan.database;

/**
 * SQL自定义操作语句接口！
 * <br/>
 * 万能操作类所能做的基本操作可能并不能满足你的需求！不过他提供让使用则可以自定义操作语句<br/>
 * 万能操作类{@link UniversalDao}
 */
public interface OptionStatement {
    /**
     * 获取携带参数的SQL语句
     * @param table 需要操作表的信息
     * @return
     */
    String getSQLStatemenet(ITable table);

    /**
     * 获取参数数组
     * @return
     */
    Object[] getParams();

}
