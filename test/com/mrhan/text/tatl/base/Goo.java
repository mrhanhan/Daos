package com.mrhan.text.tatl.base;

import com.mrhan.database.Colum;
import com.mrhan.db.entitydaos.Colument;
import com.mrhan.db.entitydaos.Table;
import com.mrhan.db.entitydaos.keytype.ForginKey;
import com.mrhan.db.entitydaos.keytype.PrimaryKey;

@Table(table = "goods",entityClass = Goo.class)
@com.mrhan.database.Table(table = "goods",entity = Goo.class)
public class Goo {

    @PrimaryKey
    @Colument(col = "id")
    @com.mrhan.database.Colum
    String id;

    @Colument(col = "name")
    @com.mrhan.database.Colum
    String name;


    @Override
    public String toString() {
        return "id:"+id+" name:"+name;
    }
}

