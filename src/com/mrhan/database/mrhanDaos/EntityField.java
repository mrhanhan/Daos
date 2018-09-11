package com.mrhan.database.mrhanDaos;


import com.mrhan.database.Colum;
import com.mrhan.database.IField;
import com.mrhan.database.Table;
import com.mrhan.database.keytype.AutoKey;
import com.mrhan.database.keytype.ForginKey;
import com.mrhan.database.keytype.PrimaryKey;
import com.mrhan.database.keytype.UniqueKey;

import java.lang.reflect.Field;

/**
 * 作者：MrHan
 */
public class EntityField implements IField {

    /**
     * 字段注解
     */
    private Colum colument;
    /***
     * 外键注解
     */
    private ForginKey forginKey;


    private AutoKey auto;
    /**
     * 主键注解
     */
    private PrimaryKey primaryKey;
    /**
     * 唯一键注解
     */
    private UniqueKey uniqueKey;

    /**
     * 字段信息
     */
    private Field field;
    /**
     * 数据类型信息
     */
    private ColumType dataType;

    /**
     * 字段信息构造函数
     *
     * @param f        字段对象
     * @param colument 表字段注解对象
     */
    public EntityField(Field f, Colum colument) {
        this.field = f;
        this.colument = colument;

        init();
    }

    private void init() {
        this.dataType = this.colument.type();
        this.primaryKey = field.getAnnotation(PrimaryKey.class);
        this.uniqueKey = field.getAnnotation(UniqueKey.class);
        this.forginKey = field.getAnnotation(ForginKey.class);
        this.auto = field.getAnnotation(AutoKey.class);
        //判断不是外键，并且也不是实体类型，则进行类型校验
        if (!isForginKey() || !forginKey.isEntity()) {

            Class c = field.getType();
            //判断类型，是否是自动识别
            if (dataType == ColumType.AUTO) {
                for (ColumType ct : ColumType.values()) {
                    //如果当前字段类型，不能存放type类型数据
                    if (ct.isConvert(c)) {
                        dataType = ct;
                        break;
                    }

                }
                if (dataType == ColumType.AUTO) {
                    throw new RuntimeException("无法自动识别类型字段类型：" + c.getPackage() + c.getName());
                }
            } else {
                //如果当前字段类型，不能存放type类型数据
                if (!dataType.isConvert(c)) {
                    throw new RuntimeException("类型无法匹配：" + dataType.name() + " != " + c.getPackage() + c.getName());
                }
            }
        }else{
            /*如果是外键，当前字段取值类型，去找外键实体类，查询*/

                EntityTable et = (EntityTable) EntityTable.getTable(forginKey.entity().getAnnotation(Table.class));
                String forCol = forginKey.forginCol();
                forCol = forCol.isEmpty() ? columentName() : forCol;
                EntityField f = (EntityField) et.getField(forCol);//外键字段信息
                ColumType ty = f.columentType();//获取外键类型
                /*如果当前字段类型为自动检测，则，去查下主表外键字段类型*/
                if(dataType == ColumType.AUTO) {
                    dataType = ty;
                }else {
                    /*如果不是自动类型，则进行当前类型去匹配玩家类型*/
                    if(!dataType.isConvert(f.field.getType())) {
                        throw  new RuntimeException("从表字段("+columentName()+")和主表字段("+f.columentName()+")，配置类型不同！无法兼容！("+dataType.name()+"!="+f.dataType.name()+")");
                    }
                }

            }
        }


    /**
     * 获取当前字段映射的列名称
     *
     * @return
     */
    @Override
    public String columentName() {
        String name = colument.col();
        String tab = colument.table();//所属表
        if (name == null || name.isEmpty()) {
            if(tab.isEmpty())
                  name = field.getName();
            else
                name = tab+"."+name;
        }
        return name.toLowerCase();
    }

    /**
     * 表示此字段是否是自动增长字段
     *
     * @return boolean true 是自动增长
     */
    @Override
    public boolean isAutoKey() {
        return colument.isAuto() || auto != null;
    }

    /**
     * 表示此字段是否是主键字段
     *
     * @return boolean true 是主键字段
     */
    @Override
    public boolean isPirmaryKey() {
        return this.primaryKey != null;
    }

    /**
     * 表示此字段是否是外键字段
     *
     * @return
     */
    @Override
    public boolean isForginKey() {
        return this.forginKey != null;
    }

    /**
     * 表示此字段是否是唯一键键字段
     *
     * @return boolean true 是唯一键字段
     */
    @Override
    public boolean isUniqueKey() {
        return this.uniqueKey != null;
    }

    /**
     * 返回当前字段对应表中的数据类型
     *
     * @return DataType
     * @see ColumType
     */
    @Override
    public ColumType columentType() {
        return this.dataType;
    }

    /**
     * 给制定对象的这个字段设置制定数据
     *
     * @param object 所被设置设置数据的对象
     * @param data   需要设置的数据
     * @return 返回设置结果 true 表示设置成功
     */
    @Override
    public boolean setData(Object object, Object data) {
        if (data == null){
            return false;
        }
            if (!isForginKey() || !forginKey.isEntity()) {
                field.setAccessible(true);
                /*判断字段类型，是否可以存放注解类型的值*/
                if (!dataType.isConvert(field.getType())) {
                    throw new RuntimeException("类型配置错误！" + field.getType() + " 不属于 " + dataType.name());
                }
                try {
                    field.set(object, data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Class dataClass = data.getClass();

                try {
                    /*判断字段类型，是否可以存放注解类型的值*/
                    dataClass.asSubclass(field.getType());

                    field.set(object, data);


                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        return false;
    }

    @Override
    public Object getForginData(Object entity) {
        if (isForginKey() && forginKey.isEntity()) {
            field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(entity);
                return o;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取当前字段的外键信息！
     *
     * @return 如果当前字段不是外键，则返回null！如果是则返回 {@link ForginKey} ForginKey 对象
     */
    @Override
    public ForginKey forginkey() {
        return forginKey;
    }

    /**
     * 获取指定对象的当前字段的数据
     *
     * @param obj 所需要获取数据的对象
     * @return 返回获取到的具体数据！null代表获取失败或者空
     */
    @Override
    public Object getdate(Object obj) {
        field.setAccessible(true);
        Object o = null;
        try {
            o = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (o == null) {
            return o;
        }
        /*获取！并且字段类型是实体类型*/
        if (isForginKey() && forginKey.isEntity()) {
            Class<?> forC = forginKey.entity();//外键实体
            com.mrhan.database.Table t = forC.getAnnotation(com.mrhan.database.Table.class);
            EntityTable et = (EntityTable) EntityTable.getTable(t);//外键实体信息
            String forCol = forginKey.forginCol();//外键映射字段
            if (forCol.isEmpty()) forCol = columentName();
            IField fi = et.getField(forCol);//获取字段信息
            //o = 实体对象
            o = fi.getdate(o);//获取实体对象
            //o = 基本数据对象
        }

        return o;
    }
}
