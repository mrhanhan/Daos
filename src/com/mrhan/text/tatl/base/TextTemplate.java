package com.mrhan.text.tatl.base;

import com.mrhan.text.tatl.ITextAction;
import com.mrhan.text.tatl.ITextTemplate;
import com.mrhan.text.tatl.IVariable;
import com.mrhan.text.tatl.event.ITATLConstructionEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link ITextTemplate} 的实现类
 *
 * @author MrHanHao
 */
public class TextTemplate implements ITextTemplate {
    //匹配指令的正则表达式
    /**
     * 指令匹配
     * {{ () }}
     */
    static final Pattern ACTION_PATT = Pattern.compile("\\{\\{([^\\{\\}]+)}}");
    /**
     * (%)(if)()
     * 指令信息匹配
     */
    static final Pattern ACTION_TYPEP_PATT = Pattern.compile("([&%$])([a-zA-Z]+)\\b(.{0,})");
    /**
     * 字符缓冲区
     */
    private volatile StringBuilder textBuffer = null;
    /* 模板全局变量*/
    private volatile Map<String, IVariable> golbalVaribale;

    private volatile List<ITextAction> actions;

    /**
     * 够着函数
     *
     * @param templateText 携带指令的模板
     */
    public TextTemplate(String templateText) {
        textBuffer = new StringBuilder(templateText);
        golbalVaribale = new HashMap<>();
        actions = new Vector<>();
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        /*拷贝字符串模板*/
        StringBuilder text = new StringBuilder(textBuffer);
        /*正则表达式进行匹配字符*/
        Matcher acMat = ACTION_PATT.matcher(text);
        Matcher cm = null;
        while (acMat.find()) {
            String action = acMat.group(1);
            System.out.println("找到指定：" + action);
            /*匹配具体操作*/
            cm = ACTION_TYPEP_PATT.matcher(action);
            if (cm.find()) {
                System.out.println("具体操作：类型：" + cm.group(1) + " 指令" + cm.group(2) + " 参数" + cm.group(3));
            } else {
                System.out.println("语法错误");
            }
        }
    }

    @Override
    public void addGolbalObject(String objName, Object value) {

    }

    @Override
    public String[] getGolbalObjectNames() {
        /*获取key集合，再转换数组*/
        Set<String> nameSet = golbalVaribale.keySet();
        String[] names = new String[nameSet.size()];
        golbalVaribale.keySet().toArray(names);
        return names;
    }

    @Override
    public void removeGolbalObject(String objName) {
        golbalVaribale.remove(objName);
    }

    @Override
    public IVariable getGolbalObjectVal(String name) {
        return golbalVaribale.get(name);
    }

    @Override
    public String construction() {
        return null;
    }

    @Override
    public void asynConstruction(ITATLConstructionEvent event) {

    }

    @Override
    public List<ITextAction> getActions() {
        return actions;
    }

    @Override
    public boolean isExistsAction() {
        return !actions.isEmpty();
    }

    @Override
    public ITextAction getAction(int id) {
        if (id >= actions.size())
            return null;
        return actions.get(id);
    }

    @Override
    public int getActionCount() {
        return actions.size();
    }

}
