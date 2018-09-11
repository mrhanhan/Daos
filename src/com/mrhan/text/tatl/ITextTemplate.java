package com.mrhan.text.tatl;

import com.mrhan.text.tatl.event.ITATLConstructionEvent;

import java.util.List;

/**
 * <h1 align="center">文本操作模板接口</h1>
 *
 * @author MrHanHao
 *
 */
public interface ITextTemplate {
	/**
	 * 添加全局引用对对象<br/>
	 * 如果存在此名称的全局对象，则会被替换掉
	 *
	 * @param objName 对象名称
	 * @param value   对象的值
	 */
	void addGolbalObject(String objName, Object value);

	/**
	 * 获取所有的全局对象名称数组
	 *
	 * @return 返回设置过的全局对象名称
	 */
	String[] getGolbalObjectNames();

	/**
	 * 移除指定名称的全局对象
	 *
	 * @param objName 全局对象的名称
	 */
	void removeGolbalObject(String objName);

	/**
	 * 根据对象的名称，获取全局对象值
	 *
	 * @param name 对象名称
	 * @return 对象值
	 */
	IVariable getGolbalObjectVal(String name);

	/**·
	 * 解析执行字符串模板中的指令，并返回执行后的结果
	 *
	 * @return 返回执行结果
	 */
	String construction();

	/**
	 * 异步执行模板指令，执行完成之后调用其事件方法
	 *
	 * @param event 事件对象
	 */
	void asynConstruction(ITATLConstructionEvent event);

	/**
	 * 获取当前全局的操作集合
	 *
	 * @return
	 */
	List<ITextAction> getActions();

	/**
	 * 判断是否存在操作
	 *
	 * @return true/false
	 */
	boolean isExistsAction();

	/**
	 * 根据操作编号获取操作指令对象
	 *
	 * @param id
	 * @return
	 */
	ITextAction getAction(int id);

	/**
	 * 获取当前全局操作的数
	 * @return
	 */
	int getActionCount();

}
