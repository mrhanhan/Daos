package com.mrhan.text.tatl;

/**
 * 模板操作的类型<br/>
 * 主要包含单条操作，和多条操作<br/>
 * 那个，为什么需要定义两种呢？
 * <br/>
 * 对于不同操作，提供接口方法是不同的
 *<br/>
 * 单条操作是不会嵌套操作的，而多条，则会嵌套单条/多条的操作
 */
public enum ActionType {
    /**
     * 单条操作
    * */
    ACTION__SINGLE,
    /**
     * 操作集，多条操作
     */
    ACTION_BLOCK

}
