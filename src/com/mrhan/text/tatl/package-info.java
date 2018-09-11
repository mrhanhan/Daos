/**
 *
 *
 *
 */
/**
 * <h1 align="center">文本操作模板（Text action template） TATL
 * <h1/>主要采取字符串指令，做一些基本的操作！例如，字符串动态替换！<br/>
 * 主要使用的指令符号：%:指令 &:变量 $:使用变量 {str}：普通字符<br/>
 * <hr/>
 * 支持的指令：
 * <ul>
 * <li>%for 循环指令 <br/>
 * 用法：<br/>
 * 1.{{%for &i 0 %to 100 1}} {{%end %for}}: 循环指令 表示自增循环，从0 -100 每次递增 1 %end %for
 * 表示结束循环</li>
 * <li>%end 结束指令： %end [指令]</li>
 * <li>%include filePath:包含文本文件指令</li>
 * <li>%if 指令: %if $i == 10</li>
 * <li>%in %to,遍历指令</li>
 * <li>%逻辑指令:%and %or %not 与或非</li>
 * </ul>
 * 支持的运算符：
 * <ol>
 * <li>= 赋值运算符</li>
 * <li>逻辑运算符
 * <ul>
 * <li>>小于</li>
 * <li><大于</li>
 * <li>==等于</li>
 * <li>!=不等于</li>
 * <li>>=小于等于</li>
 * <li><=大等于</li>
 * </ul>
 * </li>
 * </ol>
 *
 * @author MrHanHao
 *
 */
package com.mrhan.text.tatl;