package com.mrhan.database;

import java.sql.ResultSet;

/**
 * 实体操作结果集合方法！
 * <br/>
 * {@link IEntity} 和 {@link IDao}的本质区别，在于:<br/>
 * {@link IDao} 在处理结果集合是！无需考虑遍历集合,{@linkplain IDao}但其有类型限制，类似于一个辅助类<br/>
 * {@link IEntity} 在处理结果集合时，需要考虑遍历集合,对于结果集合的处理！完全独立
 */
public interface IEntity {
    /**
     * 处理结果集合方法
     * @param rs
     */
    void entitys(ResultSet rs);
}
