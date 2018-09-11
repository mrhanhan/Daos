package com.mrhan.text.tatl.event;


import com.mrhan.text.tatl.ITextTemplate;

/**
 * 文本模板执行后接口<br/>
 * @author MrHanHao
 *
 */
public interface ITATLConstructionEvent {
	/**
	 * 当模板执行完成之后调用此方法
	 * @param text 转换后的文本
	 * @param tatl 模板对象
	 */
	void event(String text,ITextTemplate tatl);
}
