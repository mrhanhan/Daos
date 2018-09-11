package com.mrhan.text.tatl;

import java.io.Serializable;

import java.io.Serializable;

/**
 * 变量的接口
 * @author MrHanHao
 *
 */
public interface IVariable extends Serializable{
	/**
	 * 获取变量的类型
	 * @return
	 */
	EVarlableDataType getType();
	/**
	 * 获取当前变量的值，如果值是集合，集合中的值是被IVariable封装过的，如果不需要封装，则使用getObjectValue()
	 * @return 值
	 */
	<T> T getValue();

	/**
	 * 获取当前变量的值，纯净的，没有被风筝果的
	 * @return
	 */
	<T> T getObjectValue();
	/**
	 * 获取变量名称
	 * @return
	 */
	String getName();
	/**
	 * 获取当前变量所在的操作
	 * @return {@link ITextAction}
	 */
	ITextAction getAction();
	/**
	 * 设置字符值，如果是数字，则自动转换为数字
	 *
	 * @param value
	 */
	void setTatlValue(String value);
	/**
	 * 获取值，模板语言中的值
	 * @return
	 */
	String getTatlValue();
	/**
	 * 设置值
	 * @param o
	 */
	void setValue(Object o);

}
