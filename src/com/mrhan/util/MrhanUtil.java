package com.mrhan.util;

import java.io.*;
import java.lang.reflect.Field;

/**
 *
 */
public  class MrhanUtil {
    private MrhanUtil(){

    }

    /**
     * 复制对象
     * @param tcls 类型模板
     * @param from 数据源对象
     * @param to 接受数据对象
     * @param <T>
     * @return
     */
    public static <T> boolean  copyObject(Class<T> tcls , T from,T to){

        Field[] fields = tcls.getFields();
        for(Field f : fields){
            try {
                f.set(to,f.get(from));
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        fields = tcls.getDeclaredFields();
        for(Field f : fields){
            try {
                f.set(to,f.get(from));
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 克隆对象,对象必须包含无参构造函数！
     * @param tcls
     * @param obj
     * @param <T>
     * @return
     */
    public static  <T> T cloneObject(Class<T> tcls,T obj){
        try {
            T t = tcls.newInstance();

            if(!copyObject(tcls,obj,t)){
                return null;
            }
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 克隆对象！通过序列化克隆
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T cloneObject(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(obj);
        oos.close();
        byte[] bs = outputStream.toByteArray();
        outputStream.close();
        ByteArrayInputStream bai = new ByteArrayInputStream(bs);
        ObjectInputStream osi = new ObjectInputStream(bai);
        Object o = osi.readObject();
        osi.close();
        bai.close();
        return (T)o;
    }

    /**
     * 判断制定对象是否为空
     * @param obj
     * @return
     */
    public  static boolean isEmpty(Object obj){
        return obj==null;
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static  boolean isEmpty(String s){
        return s==null || s.isEmpty();
    }
}
