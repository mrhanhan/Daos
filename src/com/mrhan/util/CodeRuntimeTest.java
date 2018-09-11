package com.mrhan.util;

import java.util.ArrayList;
import java.util.List;

public class CodeRuntimeTest {
    private long time = System.currentTimeMillis();
    private static volatile boolean ISHIDDED = false;
    private static volatile List<String> createObjs = new ArrayList<>();
    private static boolean isShowOne = true;//是否显示单行信息

    public static boolean isIsShowOne() {
        return isShowOne;
    }

    public static void setIsShowOne(boolean isShowOne) {
        CodeRuntimeTest.isShowOne = isShowOne;
    }

    /**
     * 创建显示
     *
     * @return
     */
    private static String cratedShow() {
        Throwable throwable = new Throwable();
        StackTraceElement[] trace = throwable.getStackTrace();
        // 下标为0的元素是上一行语句的信息, 下标为1的才是调用printLine的地方的信息
        StackTraceElement tmp = trace[2];
        StringBuilder sb = new StringBuilder();

        long t = System.currentTimeMillis();
        for (int i = 2; i < trace.length; i++) {
            tmp = trace[i];
            sb.append(tmp.getFileName()).append(".").append(tmp.getMethodName()).append("(").append(tmp.getFileName()).append(":").append(tmp.getLineNumber()).append(") \n\t");
            if (isShowOne) {
                break;
            }
        }
        return sb.toString();
    }

    public CodeRuntimeTest() {
        createObjs.add(cratedShow());
    }

    // private static

    /**
     * 显示时间差,显示当前代码的行数
     *
     * @param msg
     */
    public void showTimeMsg(String msg) {
        if (ISHIDDED) {
            return;
        }
        Throwable throwable = new Throwable();
        StackTraceElement[] trace = throwable.getStackTrace();
        // 下标为0的元素是上一行语句的信息, 下标为1的才是调用printLine的地方的信息
        StackTraceElement tmp = trace[1];
        StringBuilder sb = new StringBuilder(msg);

        long t = System.currentTimeMillis();

        sb.append(cratedShow());
        sb.append(" [ ").append((t - time)).append("ms ] ");
        System.out.println(sb);


        time = t;
    }

    /**
     * 锁定时间
     *
     * @return
     */
    public long lockTime() {
        time = System.currentTimeMillis();
        return time;
    }

    /**
     * 显示创建当前对象的位置
     */
    public static void showCreateThisObjPoint() {
        for (String s : createObjs)
            System.out.println(s);
    }

    public static void show() {
        ISHIDDED = false;
    }

    public static void hidden() {
        ISHIDDED = true;
    }

    public static boolean isState() {
        return ISHIDDED;
    }
}
