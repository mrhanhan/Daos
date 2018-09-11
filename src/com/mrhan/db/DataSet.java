package com.mrhan.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询结果数据集合
 */
public class DataSet implements SelectDataInterface
  {

        /**
         * 当前行
         */
        private int nowRow =-1;
        /**
         *
         */
        private String cols[];

        private int colnumber;//列数

        private List<Map<String,Object>> allRow;


        public DataSet(ResultSet rs ){
            allRow=new ArrayList<Map<String,Object>>();
            try{
                ResultSetMetaData rsmd=rs.getMetaData();

                int colnum=rsmd.getColumnCount();//获取列数
                colnumber=colnum;
                cols = new String[colnum];
                for(int i=0;i<colnum;i++){

                    String s=rsmd.getColumnLabel(i+1);
                    if(s==null || s=="null"){
                        s=rsmd.getColumnName(i+1);
                    }

                    cols[i]=s;

                }

                while(rs.next()){
                    Map<String,Object> r =new HashMap<String, Object>();
                    for(int i=0;i<colnum;i++){

                        r.put(cols[i],rs.getObject(i+1));//获取值
                    }
                    allRow.add(r);
                }

            }catch(SQLException se){
                se.printStackTrace();
            }
        }


        @Override
        public boolean hasnext() {

            return (nowRow+1)<allRow.size();
        }

        @Override
        public String[] getColNames() {

            return cols;
        }

        @Override
        public Object[] getNowRowValues() {

            Object [] s=null;
            if(nowRow>=0 && nowRow<allRow.size()){
                s=new String[colnumber];
                Map<String, Object> row=allRow.get(nowRow);

                /**
                 * 获取值
                 */

                for(int i=0;i<colnumber;i++){

                    s[i]=row.get(cols[i]);
                }

            }
            return s;
        }

        @Override
        public Object getValue(int key) {
            if(nowRow>=0 && nowRow<allRow.size() && key>=0 && key<colnumber){

                Map<String, Object> row=allRow.get(nowRow);
                return row.get(cols[key]);

            }
            return null;
        }

        @Override
        public Object getValue(String key) {
            if(nowRow>=0 && nowRow<allRow.size()){

                Map<String, Object> row=allRow.get(nowRow);
                return row.get(key);

            }
            return null;
        }

        @Override
        public void next() {
            nowRow++;

        }

        @Override
        public int row() {
            // TODO Auto-generated method stub
            return allRow.size();
        }
        @Override
        public int col() {
            return colnumber;
        }



}
