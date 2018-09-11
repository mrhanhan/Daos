package com.mrhan.text.tatl.base;

import com.mrhan.text.tatl.EVarlableDataType;
import com.mrhan.text.tatl.ITextAction;
import com.mrhan.text.tatl.IVariable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mrhan.text.tatl.util.TextUtil.*;

public class Variable implements IVariable {
    private static final long serialVersionUID = 4452059493826983841L;
    private String name;/* 变量名 */
    private Object value;/* 变量值 */
    private EVarlableDataType type = null;/* 变量类型 */
    private ITextAction action;

    /**
     * 构造函数,创建变量
     *
     * @param action 变量所在的指令,如果为null，则数全局变量
     * @param name   变量名称
     * @param value  变量值
     */
    public Variable(ITextAction action, String name, Object value) {
        this.action = action;
        this.name = name;
        this.value = value;
        init();
    }

    /**
     * 设置类型
     *
     * @param type
     */
    public void setType(EVarlableDataType type) {
        this.type = type;
    }


    private void init() {
        if (value != null) {
            type = getValueType(value);
            value = converVariable(value);
        }

    }




    @Override
    public EVarlableDataType getType() {

        return type;
    }

    @Override
    public <T> T getValue() {

        if (value != null)
            return (T) value;

        return null;
    }

    @Override
    public <T> T getObjectValue() {
        return (T)convertObject(this);
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public ITextAction getAction() {

        return action;
    }

    @Override
    public void setTatlValue(String c) {
        if (isSet(c)) {
            value = convertSet(c);
            setType(EVarlableDataType.SET);
        } else if(isArray(c)){
            value = converArray(c);
            setType(EVarlableDataType.ARRAY);
        }else if (isNumber(c)) {
            value = convertInt(c);
            setType(EVarlableDataType.NUMBER);

        } else if (isDecimal(c)) {
            value = convertFloat(c);
            setType(EVarlableDataType.SIMPLENUMBER);
        } else {
            value = c;
            setType(EVarlableDataType.TEXT);
        }
    }

    @Override
    public String getTatlValue() {
        return converVariableValue(this);
    }

    @Override
    public void setValue(Object o) {
        value = o;
        init();
    }

    @Override
    public String toString() {
        return "Variable [name=" + name + ", type=" + type + ", action=" + action + ", value="
                + (type==EVarlableDataType.ARRAY? Arrays.toString((Object[])value):value) + "]";
    }


}
