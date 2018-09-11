package com.mrhan.text.tatl.base;

import com.mrhan.database.ISelectStatement;
import com.mrhan.database.ITable;
import com.mrhan.database.UniversalDao;
import com.mrhan.database.connection.MySQLConn;
import com.mrhan.database.mrhanDaos.GenericSelectStatement;
import com.mrhan.util.CodeRuntimeTest;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseTest {
    @Test
    public void Test() throws ClassNotFoundException, FileNotFoundException, SQLException {
        System.setErr(new PrintStream(new FileOutputStream(new File("out.txt"), false)));


        MySQLConn conn = new MySQLConn("localhost:3306", "root", "123456", "world");
        conn.getConn();

        long t = System.currentTimeMillis();
        final UniversalDao<City> goodDao = new UniversalDao<>(conn, City.class);
        ISelectStatement<City> a = new GenericSelectStatement<City>() {

            @Override
            public City getEntity(ResultSet rs) {

                City c = goodDao.getEntity(rs);
                return c;
            }
            @Override
            public String getSQLStatemenet(ITable table) {
                return "select * from city ";
            }

            @Override
            public Object[] getParams() {
                return new Object[]{};
            }
        };

      //  System.out.println(goodDao.updateEntityByIdentifi(cc));
        goodDao.executeSelect(a);
        //}
        System.out.println("消耗时间 :" + (System.currentTimeMillis() - t));
        // System.out.println(c);
        conn.close();

    }
}

