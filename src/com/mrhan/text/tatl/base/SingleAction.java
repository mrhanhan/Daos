package com.mrhan.text.tatl.base;

import com.mrhan.text.tatl.ActionType;
import com.mrhan.text.tatl.IBlockAction;
import com.mrhan.text.tatl.ISingleAction;
import com.mrhan.text.tatl.ITextTemplate;

/**
 * 单条语句操作实现类
 */
public class SingleAction implements ISingleAction {
    /*
     * 字符模板
     */
    private ITextTemplate textTemplate;
    /* 当前操作的父级操作！null 不存在父级，隶属于根操作*/
    private IBlockAction parentAction;
    /**
     *  构造函数
     * @param template 操作所在的模板对象
     * @param parentAction
     * @param actionStr 单条操作的字符串！操作=》{{&a=1}} 传递字符串=>&a=1
     */
    public SingleAction(ITextTemplate template,IBlockAction parentAction,String actionStr){

    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACTION__SINGLE;
    }

    @Override
    public String executeAction() {
        return null;
    }

    @Override
    public ITextTemplate getTemplate() {
        return null;
    }

    @Override
    public IBlockAction getBlockAction() {
        return null;
    }
}
