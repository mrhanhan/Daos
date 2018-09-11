package com.mrhan.text.tatl.base;
import com.mrhan.db.entitydaos.Colument;
import com.mrhan.db.entitydaos.DataType;
import com.mrhan.db.entitydaos.Table;
import com.mrhan.db.entitydaos.keytype.ForginKey;
import com.mrhan.db.entitydaos.keytype.PrimaryKey;

@Table(table = "goods",entityClass = Goods.class)
public class Goods {

    @PrimaryKey
    @Colument(col = "name")
    String id;

    @Colument(col = "id",type = DataType.TABLE)
    @ForginKey(forginClass = Goo.class,fotginCol = "id")
    Goo goo;


    @Override
    public String toString() {
        return "id:"+id+" name: {"+goo+"}\n";
    }
}
