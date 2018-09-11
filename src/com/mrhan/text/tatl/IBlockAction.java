package com.mrhan.text.tatl;

/**
 * 操作块接口<br/>
 * 主要面向多条语句的操作<br/>
 * 拥有自己的变量和自己的包含内容部分
 */
public interface IBlockAction extends ITextAction {
    /**
     * 添加变量<br/>
     * 如果存在，则会覆盖
     *
     * @param iVariable
     */
    void addVariable(IVariable iVariable);

    /**
     * 获取当前变量<br/>
     *
     * @param name 如果不存在则会返回null
     * @return
     */
    IVariable getVariable(String name);

    /**
     * 判断是否存在制定名称的变量
     *
     * @param name 变量名称
     * @return
     */
    boolean isExistsVariable(String name);

    /**
     * 返回当前块下拥有的自操作<br/>
     * 不包含自操作块里的操作
     * @return
     */
    int childActionCount();

    /**
     * 根据id/index 来获取子操作句子
     * @param id
     * @return
     */
    ITextAction getAction(int id);

    /**
     * 获取所包含的文本
     * @return
     */
    String getInclueText();
}
