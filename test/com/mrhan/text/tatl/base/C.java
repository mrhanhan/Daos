package com.mrhan.text.tatl.base;

import com.mrhan.database.Colum;
import com.mrhan.database.Table;
import com.mrhan.database.mrhanDaos.ColumType;
import com.mrhan.db.entitydaos.Colument;

@Table(table = "city", entity = C.class)
public class C {

    @Colum
    private String ID;

   @Colum
    private String Name;

   @Colum
    private String CountryCode;

   @Colum
    private String District;

    @Colum
    private String Population;


    @Override
    public String toString() {
        return "City{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", CountryCode='" + CountryCode + '\'' +
                ", District='" + District + '\'' +
                ", Population='" + Population + '\'' +
                "}\n";
    }
}
