package com.mrhan.text.tatl;

/**
 * <h1 align="center">文本操作模板指令</h1>
 *
 * @author MrHanHao
 */
public interface ITextAction {
    /**
     * 获取当前指令类型
     * @return
     */
    ActionType getActionType();

    /**
     * 执行当前操作
     */
    String executeAction();

    /**
     * 获取当前模板所在的模板里
     * @return
     */
    ITextTemplate getTemplate();

    /**
     * 获取当前操作所在的操作集里
     * @return
     */
    IBlockAction getBlockAction();
}
