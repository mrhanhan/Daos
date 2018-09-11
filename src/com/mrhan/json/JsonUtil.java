package com.mrhan.json;

import java.lang.reflect.Field;

public class JsonUtil {
    /**
     * 把对象转换为JSON格式化字符串<br/>
     * 深解析！
     * @param obj 对象参数
     * @return
     */
    public static String toJson(Object obj){
        StringBuilder sb = new StringBuilder("{");
        Class<?> objClass = obj.getClass();
        Field [] fs = objClass.getFields();
        int max = fs.length;
        for(int i=0;i<max;i++){
            fzJson(sb,fs[i],obj);
            if(i<fs.length-1){
                sb.append(",");
            }
        }
        fs = objClass.getDeclaredFields();
         int max1 = fs.length;
         if(max>0 && max1>0){
             sb.append(",");
         }
        for(int i=0;i<max1;i++){
            fzJson(sb,fs[i],obj);
            if(i<fs.length-1){
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    /*对象，转String*/
    private static void fzJson(StringBuilder sb , Field f ,Object obj){
        try {

            f.setAccessible(true);
            String name = f.getName();
            Object val = f.get(obj);
            if(val==null)
                return;
            Class c = val.getClass();
            if(!c.isPrimitive()){//判断是否是复合类型
                if(c.equals(String.class) || c.equals(Integer.class) || c.equals(Double.class) || c.equals(Float.class) || c.equals(Short.class) ||
                        c.equals(Character.class) ||c.equals(Long.class) ) {
                    sb.append("\"").append(f.getName()).append("\"")
                            .append(":").append("\"").append(f.get(obj)).append("\"");
                }else{
                    sb.append("\"").append(name).append("\"").append(":");
                    String js = toJson(val);
                    sb.append(js);

                }

            }else {
                sb.append("\"").append(f.getName()).append("\"")
                        .append(":").append("\"").append(f.get(obj)).append("\"");
            }
            f.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
