package com.mrhan.db.entitydaos;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 实体类字段数据类型
 */
public enum DataType {
    /**
     * 数字类型
     */
    INTEGER("setInt","getInt"),
    /**
     * 字符类型
     */
    STRING("set","get"),
    /**
     * 日期类型
     */
    DATE("set","get"),
    /**
     * 单浮点型
     */
    FLOAT("setFloat","getFloat"),
    /**
     * 双浮点型
     */
    DOUBLE("setDouble","getDouble"),
    /**
     * 外键实体类型 外键有效
     */
    TABLE("set","get"),
    /**
     *外键有效
     * 数组
     */
    ARRAY("set","get"),
    /**实体类型
     * 集合
     */
    COLLECTION("set","get");

    /**
     * 设置数据时需要用到的方法名称
     */
    private String setMothed;
    /**
     * 获取数据时需要用到的方法名称
     */
    private String getMothed;

    /**
     * 构造函数
     * @param set 设置时调用的方法名称
     * @param get 获取是调用的方法名称
     */
     DataType(String set,String get){
        this.setMothed = set;
        this.getMothed = get;
    }

    /**
     * 设置指定数据类型的数据
     * @param f 字段对象
     * @param obj 设置数据的对象
     * @param value 需要设置的值
     * @throws InvocationTargetException {@link InvocationTargetException}
     * @throws IllegalAccessException {@link IllegalAccessException}
     * @return  boolean 返回设置数据的结果 true 设置成功
     */
     boolean setDate(Field f,Object obj,Object value) throws InvocationTargetException, IllegalAccessException {
        Class<?> fieldClass = f.getClass();//获取字段的Class模板对象
        Method method  = findNameMethod(fieldClass,setMothed);//找到指定设置数据的方法
        if(method!=null){
            method.setAccessible(true);
            f.setAccessible(true);
            if(f.getType() == String.class){
                value = value+"";
            }
           //\ System.out.println(method.getName()+"("+obj+","+value+")");
            if(obj!=null && value!=null)
                method.invoke(f,obj,value);
            return true;
        }
        return false;
    }

    /**
     * 获取指定对象中指定字段的数据！
     * @param f 字段对象
     * @param obj 获取数据的目标对象
     * @param <D> 数据类型D
     * @throws InvocationTargetException {@link InvocationTargetException}
     * @throws IllegalAccessException {@link IllegalAccessException}
     * @return 返回具体的数据结果！null 代表获取失败
     */
    public <D extends Object> D getDate(Field f,Object obj) throws InvocationTargetException, IllegalAccessException {
        Class<?> fieldClass = f.getClass();//获取字段的Class模板对象
        Method method  = findNameMethod(fieldClass,getMothed);//找到指定设置数据的方法
        method.setAccessible(true);
        f.setAccessible(true);
        if(method!=null){
           Object o =  method.invoke(f,obj);//制定并取得结果
            return (D)o;
        }

        return  null;
    }

    /**
     * 获取指定Class模板中指定名称的方法对象
     * @param methodClass class模板对象
     * @param methodName 需要找到方法的名称
     * @return 返回 null 代表未找到此名称的方法
     *
     */
    private Method findNameMethod(Class<?> methodClass,String methodName){
        Method [] methods = methodClass.getMethods();
        for(Method m : methods){
            if(m.getName().equals(methodName)){
                return m;
            }
        }
        return null;
    }

}
