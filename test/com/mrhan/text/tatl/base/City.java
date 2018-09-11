package com.mrhan.text.tatl.base;

import com.mrhan.database.Colum;
import com.mrhan.database.Table;
import com.mrhan.database.keytype.PrimaryKey;
import com.mrhan.db.entitydaos.Colument;

import javax.xml.soap.Text;
import java.util.Date;
@com.mrhan.db.entitydaos.Table(table = "city", entityClass = City.class)
@Table(table = "city", entity = City.class)
public class City {
    @PrimaryKey
    @Colum
     String ID;
   //@ForginKey(entity = C.class,isEntity = true)
   @Colum
    String Name;

    @Colum
    String CountryCode;
    @Colument(col = "a")
    @Colum
    String District;

    @Colum
    String Population;


    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getPopulation() {
        return Population;
    }
































    public void setPopulation(String population) {
        Population = population;
    }

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
