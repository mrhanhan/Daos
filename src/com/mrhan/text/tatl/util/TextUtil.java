package com.mrhan.text.tatl.util;

/**
 * 文本工具类
 *
 * @author MrHanHao
 */

import com.mrhan.text.tatl.EVarlableDataType;
import com.mrhan.text.tatl.IVariable;
import com.mrhan.text.tatl.base.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtil {
    /**
     * 数字类型
     */
    private static final Set<Class<?>> NUMBER_TYPE_SET;
    /**
     * 小鼠类型
     */
    private static final Set<Class<?>> SIMPLENUMBER_TYPE_SET;

    static {
        NUMBER_TYPE_SET = new HashSet<>();
        SIMPLENUMBER_TYPE_SET = new HashSet<>();
        NUMBER_TYPE_SET.add(int.class);
        NUMBER_TYPE_SET.add(Integer.class);
        NUMBER_TYPE_SET.add(short.class);
        NUMBER_TYPE_SET.add(Short.class);
        NUMBER_TYPE_SET.add(long.class);
        NUMBER_TYPE_SET.add(Long.class);
        NUMBER_TYPE_SET.add(byte.class);
        NUMBER_TYPE_SET.add(Byte.class);
        /*添加小数类型*/
        SIMPLENUMBER_TYPE_SET.add(float.class);
        SIMPLENUMBER_TYPE_SET.add(Float.class);
        SIMPLENUMBER_TYPE_SET.add(double.class);
        SIMPLENUMBER_TYPE_SET.add(Double.class);

    }
    /*数字的正则表达式*/
    private static final String NUMBER_REG = "[\\+-]?[0-9]{1,9}+";
    /*匹配小数的正则表达式*/
    private static final String DECIMAL_REG = "[\\+-]?[0-9]{1,9}+\\.[0-9]{1,5}";
    /*匹配数组或者集合的的子表达式的正则表达式*/
    private static final String SET_ITEM_REG = "(['\"][^\"']+['\"])|([\\+-]?[0-9]{1,9}+\\.?[0-9]{0,5})";
    /*数组字符的表达式*/
    private static final String ARRAY_REG = "[\\[\\],]";
    /*集合字符的表达式*/
    private static final String SET_REG = "[\\(\\),]";

    /**
     * 判断这class是否属于数字类型
     * @param cls
     * @return
     */
    public static boolean isNumberType(Class<?> cls) {
        return NUMBER_TYPE_SET.contains(cls);
    }

    /**
     * 判断这class是否属于小数类型
     * @param cls
     * @return
     */
    public static boolean isNDecimalType(Class<?> cls) {
        return SIMPLENUMBER_TYPE_SET.contains(cls);
    }
    /**
     * 判断是否数 数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return str.matches(NUMBER_REG);
    }

    /**
     * 字符串转换数字
     * @param str
     * @return
     */
    public static int convertInt(String str) {
        if (isNumber(str)) {
            str.replace("+", "");
            return Integer.parseInt(str);
        } else {
            return 0;
        }
    }

    /**
     * 判断是否是小数
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        return str.matches(DECIMAL_REG);
    }

    /**2
     * 字符串转换为float类型的小数
     * @param str
     * @return
     */
    public static float convertFloat(String str) {
        if (isDecimal(str)) {
            str.replace("+", "");
            return Float.parseFloat(str);
        } else {
            return 0;
        }
    }

    /**
     * 字符串转换为集合
     * @param str
     * @return
     */
    public static List<IVariable> convertSet(String str) {
        List<IVariable> list = new Vector<IVariable>();
        if (isSet(str)) {
            /*匹配集合项中的内容*/
            Matcher mc = Pattern.compile(SET_ITEM_REG).matcher(str);
            String c = null;
            Integer a = null;
            Float b = null;
            Variable vb = null;
            int i = -1;
            while (mc.find()) {
                i++;
                c = mc.group();//取得匹配结果
                /*判断数据类型，封装对象*/
                if (isNumber(c)) {
                    a = convertInt(c);
                    vb = new Variable(null, i + "", a);
                    vb.setType(EVarlableDataType.NUMBER);

                } else if (isDecimal(c)) {
                    b = convertFloat(c);
                    vb = new Variable(null, i + "", b);
                    vb.setType(EVarlableDataType.SIMPLENUMBER);
                } else {
                    vb = new Variable(null, i + "", c.replaceAll("[\"']", ""));
                    vb.setType(EVarlableDataType.TEXT);
                }
                list.add(vb);

            }
        }
        return list;
    }

    /**
     * 模板数组变量值，转换Java变量值
     * @param str
     * @return
     */
    public static IVariable[] converArray(String str){
         str=str.replace('[','(').replace(']',')');
         System.out.println(str);
        List<IVariable> list = convertSet(str);
        IVariable vars[] = new IVariable[list.size()];
        list.toArray(vars);
        return vars;
    }

    /**
     * 判断是否数集合字符<br/>
     * 集合字符串:(a,b,c,d,f);
     * @param str
     * @return
     */
    public static boolean isSet(String str) {
        if (!str.contains("(") || !str.contains(")"))
            return false;
        return str.replaceAll(SET_ITEM_REG, "").replaceAll(SET_REG, "").isEmpty();
    }

    /**
     * 获取指定对象的类型
     * @param value
     * @return
     */
    public static EVarlableDataType getValueType(Object value){
        Class<?> cls = value.getClass();
        /*判断数据类型*/
        if (cls.isArray()) {
           return EVarlableDataType.ARRAY;
        } else if (value instanceof List) {
            return EVarlableDataType.SET;

        } else if (isNumberType(cls)) {
            return EVarlableDataType.NUMBER;
        } else if (isNDecimalType(cls)) {
            return EVarlableDataType.SIMPLENUMBER;
        } else {

            return  EVarlableDataType.TEXT;
        }
    }

    /**
     * 对象之，转换为变量对象
     * @param o
     * @return
     */
    public static Object converVariable(Object o){

        /*最后的生成的对象*/
        Object obj = null;
        if(o.getClass().isArray()){
            Object []arrays = (Object[]) o;
            IVariable []vars = new IVariable[arrays.length];
            for(int i=0;i<arrays.length;i++){
                Object os = arrays[i];
                if(os instanceof Variable){
                    vars[i] = (Variable)os;
                }else
                    vars[i]=new Variable(null,"S"+i,os);
            }
            obj = vars;
        }else if(o instanceof List){
            /*集合*/
            List lists   = (List) o;
            List<IVariable> vars = new Vector<>();
            for(int i=0;i<lists.size();i++){
                Object os = lists.get(i);
                if(os instanceof Variable){
                    vars.add((Variable)os);
                }else
                vars.add(new Variable(null,"S"+i,os));
            }
            obj = vars;
        }else{
            obj = o;
        }
        return obj;
    }

    /**
     * 判断是否数组集合字符<br/>
     * 集合字符串:[a,b,c,d,f];
     * @param str
     * @return
     */
    public static boolean isArray(String str) {
        if (!str.contains("[") || !str.contains("]"))
            return false;
        return str.replaceAll(SET_ITEM_REG, "").replaceAll(ARRAY_REG, "").isEmpty();
    }

    /**
     * 去除IVariable封装，
     * @param var
     * @return
     */
    public static Object convertObject(IVariable var){
        Object obj = var.getValue();
        /*判断类型*/
        if(var.getType()==EVarlableDataType.ARRAY){
            IVariable[] vars = var.getValue();
            Object objs[] = new Object[vars.length];
            for(int i=0;i<vars.length;i++){
                objs[i] = vars[i].getValue();
            }
            return objs;
        }else if(var.getType() == EVarlableDataType.SET){
            List<IVariable> vars = var.getValue();
            List<Object> objs= new Vector<>();
            for(int i=0;i<vars.size();i++){
                objs.add(vars.get(i).getValue());
            }
            return objs;
        }else{
            return obj;
        }
    }


    /**
     * 将变量对象，转换成对于的字符串值
     * @return
     */
    public static String converVariableValue(IVariable var) {
        /*获取值和值得类型*/
        Object obj = var.getValue();
        EVarlableDataType type = var.getType();
        StringBuilder sb = new StringBuilder();
        /*根据类型进行转换*/
        switch (type) {
            case TEXT:
                sb.append("\"");
                sb.append(obj);
                sb.append("\"");
                break;
            case SET:
                List<IVariable> list = (List<IVariable>) obj;
                sb.append("(");
                for (int i = 0; i < list.size(); i++) {
                    sb.append(converVariableValue(list.get(i)));
                    if (i < list.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append(")");
                break;
            case NUMBER:
            case SIMPLENUMBER:
                sb.append(obj);
                break;
            case ARRAY:
                IVariable values[] = (IVariable[]) obj;
                sb.append("[");
                for (int i = 0; i < values.length; i++) {
                    sb.append(converVariableValue(values[i]));
                    if (i < values.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
                break;
        }
        return sb.toString();
    }
}
