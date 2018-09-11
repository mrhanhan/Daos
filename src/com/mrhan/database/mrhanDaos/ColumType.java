package com.mrhan.database.mrhanDaos;
import com.mrhan.database.Colum;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 列的数据类型
 */
public enum ColumType {
    /**
     * 字符串类型
     */
    STRING(String.class),
    /**
     * 32位数数字类型
     */
    INT(int.class, Integer.class, byte.class, Byte.class, Short.class, short.class, char.class),
    /**
     * 长整形数字类型
     */
    LONG(INT, long.class, Long.class),
    /**
     * 单精度浮点数
     */
    FLOAT(INT, float.class, Float.class),
    /**
     * 双进度浮点类型
     */
    DOUBLE(FLOAT, double.class, Double.class),
    /**
     * 时间类型
     */
    DATETIME(Date.class),
    /**
     * 日期类型
     */
    DATE(Date.class),
    /**
     * 数字类型
     */
    DECIMAL(LONG, DOUBLE),
    /**
     * 二进制类型
     */
    BINARY(byte[].class),
    /**
     * 自动识别类型
     */
    AUTO;
    private Set<Class> typeClassSet = new HashSet<>();
    ColumType(){}
    /**
     * @param typeClass 类型class对象
     */
    ColumType(Class... typeClass) {
        for (Class s : typeClass) {
            typeClassSet.add(s);
        }
    }

    /**
     * @param childType 子类型
     * @param typeClass
     */
    ColumType(ColumType childType, Class... typeClass) {
        typeClassSet.addAll(childType.typeClassSet);
        for (Class s : typeClass) {
            typeClassSet.add(s);
        }
    }

    /**
     * 子类型集合
     *
     * @param childs
     */
    ColumType(ColumType... childs) {
        for (ColumType t : childs) {
            typeClassSet.addAll(t.typeClassSet);
        }
    }

    /**
     * 当前集合类型，是否可以进行转化
     * @param cls
     * @return 返回当前类型，是否可以转换为cls类型
     */
    public boolean isConvert(Class cls){
        if(typeClassSet.contains(cls)){
            return true;
        }
        for(Class c : typeClassSet){
            try{
                if(c.asSubclass(cls)!=null){
                    return true;
                }
            }catch(Exception e){}
        }
        return false;
    }

    /**
     * 验证此类型是否属于
     *
     * @param cls
     * @return
     */
    public boolean isType(Class cls) {
        if (typeClassSet.contains(cls)) {
            return true;
        }
        /*查看是否是其子类型*/
        for (Class p : typeClassSet) {
            try {
                cls = cls.asSubclass(p);
                if(cls!=null){
                    return true;
                }
            }catch(Exception e){}
        }
        return false;
    }
}
