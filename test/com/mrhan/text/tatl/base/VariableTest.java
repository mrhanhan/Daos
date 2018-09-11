package com.mrhan.text.tatl.base;

import com.mrhan.database.connection.MySQLConn;
import com.mrhan.db.connection.MySQLDBConnnect;
import com.mrhan.db.entitydaos.UniversalDao;

import com.mrhan.database.mrhanDaos.ColumType;
import org.junit.Test;

import java.sql.SQLException;

public class VariableTest {
    @Test
    public void Test() throws SQLException, ClassNotFoundException {
        UniversalDao<City> goodDao = new UniversalDao<>(new MySQLDBConnnect("root","123456"),"world",City.class);
                //System.out.println(
                        goodDao.selectAll();
        //);
                goodDao.closeConn();
    }
    @Test
 public void main() throws ClassNotFoundException, SQLException {
        new MySQLConn("localhost:3306","root","123456","world").getConn().close();
        int news=0,olds=0;
        long dx1 = 0,dx2=0;
        {
            long t1 = System.currentTimeMillis();
            Test();
            t1 = System.currentTimeMillis() - t1;
            System.out.println("old:"+t1);
            long t2 = System.currentTimeMillis();
            Test1();
            t2 = System.currentTimeMillis() - t2;
            System.out.println("new:"+t2);
            dx1 = t1-t2;
        }
        {
            long t1 = System.currentTimeMillis();
            Test1();
            t1 = System.currentTimeMillis() - t1;
            System.out.println("new:"+t1);
            long t2 = System.currentTimeMillis();
            Test();
            t2 = System.currentTimeMillis() - t2;
            System.out.println("old:"+t2);
            dx2 = t1-t2;
        }
        System.out.println(dx1+" : "+dx2);
 }

    @Test
    public void Test1() throws ClassNotFoundException, SQLException {
        MySQLConn conn = new MySQLConn("localhost:3306","root","123456","world");
        com.mrhan.database.UniversalDao<City> goodDao = new com.mrhan.database.UniversalDao<>(conn,City.class);
             System.out.println(
        goodDao.selectAll());
             conn.close();
    }

    @Test
    public void typeTest(){
        System.out.println(ColumType.DECIMAL.isType(short.class));
    }

}
