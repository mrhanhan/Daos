package com.mrhan.util;

/**
 * 其他工具类
 */
public class OtherUtil {
    /**
     * 判断一个字符串是否是数字 +1 -1 -1.1 都可以识别数字
     *
     * @param str 检测字符串
     * @return Boolean true代表是数字
     */
    public static boolean isNumber(String str){
        int p= 0;//当前字符下标
        int l = str.length();//字符串长度
        if(str.isEmpty())//判断字符串是否为空
            return false;
        char c = str.charAt(p);//获取第一个字符
        if(str.indexOf(".") != str.lastIndexOf("."))//判断是否有多个小数点
            return false;
        if(c=='-' || c=='+'){//判断第一位是否有占位符 + -
            p++;
            while(p<l){
                c= str.charAt(p++);
                if(c<'0' || c>'9'){
                    return  false;
                }
            }
        }else if(c>='0' && c<='9'){//判断第一位是否是数字
            while(p<l){
                c= str.charAt(p++);
                if(c<'0' || c>'9'){
                    return  false;
                }
            }
        }else{
            return false;
        }
        return true;
    }
}
