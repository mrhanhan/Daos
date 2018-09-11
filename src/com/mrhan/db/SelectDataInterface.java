package com.mrhan.db;
/**
 * 数据库查询接口
 * @author MrHanHao
 *
 */
public interface SelectDataInterface {
	/**
	 * 是否有下一行数据
	 * @return ?
	 */
	boolean hasnext();
	/**
	 * 
	 * 获取所有列明
	 * @return
	 */
	String[] getColNames();
	
	/**
	 * 获取当前行所有的值
	 */
	Object[] getNowRowValues();
	
	/**
	 * 获取当前行制定下表 的值
	 * @param key
	 * @return ?
	 */
	Object getValue(int i);
	/**
	 * 获取当前行指定列名的值
	 * @param key ????
	 * @return ?
	 */
	Object getValue(String key);
	/**
	 *指针移动到下一行
	 */
	void next();
	
	/**
	 * 返回行数
	 * @return ????????????
	 */
	int row();
	/**
	 * 返回列数
	 * @return ????????????
	 */
	int col();
	
}
