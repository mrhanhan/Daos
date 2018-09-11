package com.mrhan.text.tatl;

/**
 * 指令类型<bt/>
 * 定义
 */
public enum InstructionType {
    /**
     * 循环语句
     * %for
     */
    INSTRUCTION_FOR,
    /**
     * 结束指令 结束语句块的操作
     * %end %for结束for循环代码块
     * %end %if结束if代码块
     */
    INSTRUCTION_END,
    /**
     * 条件语句
     */
    INSTRUCTION_IF;


}
