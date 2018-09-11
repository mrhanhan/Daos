package com.mrhan.text.tatl.base;

import com.mrhan.util.CodeRuntimeTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.Assert.*;


public class TextTemplateTest {

    public double i ;

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        CodeRuntimeTest crt = new CodeRuntimeTest();
        CodeRuntimeTest.hidden();
        crt.lockTime();
        TextTemplateTest t = new TextTemplateTest();
       Field f=  TextTemplateTest.class.getField("i");
        crt.showTimeMsg("Create:");
        Object o = new Integer(1);
       f.set(t,o);
        crt.showTimeMsg("SET:");
         o = f.get(t);
        crt.showTimeMsg("GET:");
        System.out.println(o.getClass());
        crt.showTimeMsg("OUT:");

    }
    @Test
    public void typeTest() {
        System.out.println(int.class.asSubclass(int.class));
    }
}
