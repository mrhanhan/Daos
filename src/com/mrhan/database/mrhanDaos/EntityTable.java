package com.mrhan.database.mrhanDaos;

import com.mrhan.database.Colum;
import com.mrhan.database.IField;
import com.mrhan.database.ITable;
import com.mrhan.database.Table;
import com.mrhan.database.keytype.PrimaryKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体表
 * 字段名称，
 * 表名，不区分大小写<br/>
 * <h1>作者：MrHan</h1>
 */
public class EntityTable implements ITable {

    /**
     * 存放加载过得实体类与v表信息
     * K 对应的是表名称
     * V 对应的是实体类
     */
    private static final Map<String, EntityTable> loadedEntity = new HashMap<>();

    /**
     * 根据注解信息来获取实体类与表的信息
     *
     * @param table 实体类的表注解
     * @return ITable 实体类与表的关系对象
     */
    public static ITable getTable(Table table) {
        EntityTable et = loadedEntity.get(table.entity().getName() + table.table());//根据实体类对应的表名来从历史加载记录中获取

        if (et == null)//判断是否为空！如果为空则代表从未加载过这个实体类
        {
            et = new EntityTable(table);
            loadedEntity.put((table.entity().getName() + table.table()).toLowerCase(), et);
        } else {
            et.load(table);
        }
        return et;
    }

    /**
     * 存放当前实体表中所有的映射字段信息
     * K 对应表中的字段名 不区分大小写
     * V 字段信息对象
     */
    private Map<String, EntityField> tableFields = new HashMap<>();

    private Map<Class<?>, List<IField>> allFields = new HashMap<>();

    /**
     * z
     * 自动增长字段
     */
    private IField autoFiled;

    /**
     * 对应的表的名称
     */
    private String tableName;
    /**
     * 对应的实体类 类对象
     */
    private Class<?> entityClass;
    /**
     * 实体类的注解
     */
    private Table table;

    /**
     * 只有构造函数
     */
    private EntityTable(Table table) {
        load(table);
    }

    /**
     * 加载方法
     *
     * @param table
     */
    private void load(Table table) {
        this.table = table;
        tableName = table.table();
        entityClass = table.entity();
        parseField(entityClass.getFields());
        parseField(entityClass.getDeclaredFields());
    }

    /**
     * 解析有用字段信息
     *
     * @param fs 实体类的字段集合
     */
    private void parseField(Field[] fs) {

        Annotation[] annos = null;
        String colName = "";
        EntityField ef = null;
        Colum colum = null;
        List<IField> fields = null;
        /*遍历字段集合*/
        for (Field f : fs) {
            f.setAccessible(true);
            /*获取字段注解*/
            colum = f.getAnnotation(Colum.class);
            if (colum != null) {
                /*获取字段所有注解*/
                annos = f.getAnnotations();
                ef = new EntityField(f, colum);
                if (colum.isAuto()) {
                    if (autoFiled == null) {
                        autoFiled = ef;
                    } else {
                    }
                }
                /*映射字段名称*/
                colName = colum.col();
                /*如果为空则，默认字段名就是表列名称*/
                if (colName.isEmpty()) {
                    colName = f.getName();
                }
                tableFields.put(colName.toLowerCase(), ef);
                if (annos != null) {
                    for (Annotation anno : annos) {
                        if (anno != null) {
                            /*获取字段所有注解信息，并将这些信息保存起来*/
                            fields = allFields.get(anno.annotationType());
                            if (fields == null) {
                                fields = new ArrayList<>();
                                allFields.put(anno.annotationType(), fields);
                            }
                            fields.add(ef);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取当前实体类对应的表名
     *
     * @return String 返回实体类上映射的表明
     */
    @Override
    public String getTableName() {
        return tableName;
    }


    @Override
    public Set<String> getColumNames() {
        return tableFields.keySet();
    }

    /**
     * 获取对应的实体类的类信息 类模板
     *
     * @return 返回实体类的类模板 {@link Class}
     */
    @Override
    public Class<?> getEntityClass() {
        return entityClass;
    }

    /**
     * 获取指定类型的字段信息
     *
     * @param type 注解类型的Class
     * @return
     */
    @Override
    public List<IField> findTypeField(Class<?> type) {
        return allFields.get(type);
    }

    /**
     * 返回当前实体类中所有需要映射 的字段关系对象
     *
     * @return {@link IField}
     */
    @Override
    public Set<IField> fields() {
        Set<IField> fields = new HashSet<>();
        fields.addAll(tableFields.values());
        return fields;
    }

    @Override
    public IField getIdent() {
        IField fi = autoFiled;
        List<IField> fs = null;
        if (fi == null) {
            fs = allFields.get(com.mrhan.database.keytype.PrimaryKey.class);
            if (fs != null && !fs.isEmpty()) {
                fi = fs.get(0);
            }
        }
        if (fi == null) {
            fs = allFields.get(com.mrhan.database.keytype.UniqueKey.class);
            if (fs != null && !fs.isEmpty()) {
                fi = fs.get(0);
            }
        }
        return fi;
    }

    @Override
    public String getTableAsName() {

        return table.as();
    }

    /**
     * 根据实体类对应的表中，获取指定列明（字段名）的字段关系对象
     *
     * @param columentName 数据表中字段（列）名称
     * @return IFeild 映射关系对象
     */
    @Override
    public IField getField(String columentName) {
        return tableFields.get(columentName.toLowerCase());
    }

    /**
     * 创建一个新的实体对象，创建前，请保证此实体类有无参数的构造函数
     *
     * @return 返回一个实体类型的对象
     */
    @Override
    public <E> E createEntity() {
        try {

            return (E) entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    public SQLStatement createInset(Object entityObj) {
//        return null;
//    }
//
//    @Override
//    public SQLStatement createUpdate(Object entityObj, SQLWhere... where) {
//        return null;
//    }
//
//    @Override
//    public SQLStatement createDelete(Object entityObj, SQLWhere... where) {
//        return null;
//    }
//
//    @Override
//    public SQLStatement createSelect(SQLWhere... where) {
//        return null;
//    }
}
//
//    /**
//     * 根据此实体类的模板，和指定获取数据源的对象来创建插入语句！
//     *
//     * @param entityObj 模板需要获取数据的实体类具体对象
//     * @return 返回 SQLStatement 返回具体的语句对象
//     * @see SQLStatement
//     */
//    @Override
//    public SQLStatement createInset(Object entityObj){
//        String  tableName = table.table();
//        StringBuilder sb = new StringBuilder("INSERT INTO ");
//        StringBuilder values  = new StringBuilder("VALUES(");
//        sb.append(tableName).append("(");
//        List<Object> params = new ArrayList<>();//参数集合
//        Iterator<IField>  tableColuments =fields().iterator();//获取实体类中所有映射的字段信息集合
//        while(tableColuments.hasNext()){
//
//            IField field = tableColuments.next();//遍历集合
//            Object value = field.getdate(entityObj);//获取对象里这个字段的值
//            sb.append(field.columentName());//最佳添加数据的字段名称
//            values.append("?");
//            if(tableColuments.hasNext()){
//                sb.append(",");
//                values.append(",");
//            }
//            /**
//             * 判断是否是外键，并且数据类型是表类型
//             */
//            if(field.isForginKey() && field.columentType()==DataType.TABLE){
//                ForginKey fk = field.forginkey();//外键信息
//                Table tb = fk.forginClass().getAnnotation(Table.class);//获取注解信息
//                String forginCol = fk.fotginCol();//获取外键字段名称
//
//                if(tb!=null){
//                    ITable forginTable = EntityTable.getTable(tb);//获取外键表
//                    IField forginField = forginTable.getField(forginCol.toLowerCase());//获取指定的外键信息
//                    params.add(forginField.getdate(value));//最佳参数值
//                }
//                else{
//                    throw new EntityException("实体外键设置有误！"+fk.forginClass().getName()+" 无法找到注解信息");
//                }
//            }else{
//                params.add(value);//最佳参数值
//            }
//        }
//        sb.append(") ");
//        values.append(") ");
//        return new SQLStatement(sb.toString()+values,params.toArray());
//
//
//    }
//
//    /**
//     * 根据此实体类的模板，和指定获取数据源的对象来创建修改语句！
//     *
//     * @param entityObj 模板需要获取数据的实体类具体对象
//     * @return 返回 SQLStatement 返回具体的语句对象
//     * @see SQLStatement
//     */
//    @Override
//    public SQLStatement createUpdate(Object entityObj ,SQLWhere ...where) {
//        StringBuilder sb = new StringBuilder(" UPDATE ");
//        sb.append(tableName).append(" set ");
//        Set<IField> fields = fields();
//        List params = new ArrayList();//参数数组
//        int i=0;
//        for(IField fi : fields){
//            i++;
//            if(!fi.isAutoKey()) {
//                sb.append(fi.columentName()).append(" = ?");
//                params.add(fi.getdate(entityObj));
//                if(i<fields.size())
//                    sb.append(",");
//            }
//
//        }
//
//        for( i = 0;i<where.length;i++){
//            SQLWhere sw = where[i];
//            if(i==0){
//                sb.append(" WHERE ");
//                sb.append(sw);
//                params.add(sw.value);
//            }else{
//                sb.append(" ").append(sw.join).append(" ");
//                sb.append(sw);
//                params.add(sw.value);
//            }
//            if(i<where.length-1){
//                sb.append(" , ");
//            }
//        }
//
//        return new SQLStatement(sb.toString(),params.toArray());
//    }
//
//    /**
//     * 根据此实体类的模板，和指定获取数据源的对象来创建插入语句！
//     *
//     * @param entityObj 模板需要获取数据的实体类具体对象
//     * @return 返回 SQLStatement 返回具体的语句对象
//     * @see SQLStatement
//     */
//    @Override
//    public SQLStatement createDelete(Object entityObj ,SQLWhere ...where)  {
//        StringBuilder sb = new StringBuilder(" DELETE FROM ");
//        sb.append(tableName);
//        List params = new ArrayList();//参数数组
//        for( int i = 0;i<where.length;i++){
//            SQLWhere sw = where[i];
//            if(i==0){
//                sb.append(" WHERE ");
//                sb.append(sw);
//                params.add(sw.value);
//            }else{
//                sb.append(" ").append(sw.join).append(" ");
//                sb.append(sw);
//                params.add(sw.value);
//            }
//            if(i<where.length-1){
//                sb.append(" , ");
//            }
//        }
//        return new SQLStatement(sb.toString(),params.toArray());
//    }
//
//    /**
//     * 创建查询语句
//     *
//     * @param where 条件
//     * @return 返回 SQLStatement 返回具体的语句对象
//     * @see SQLStatement
//     */
//    @Override
//    public SQLStatement createSelect(SQLWhere... where) {
//        StringBuilder sb = new StringBuilder("SELECT * FROM").append(tableName);
//        sb.append(" ");
//        List params = new ArrayList();//参数数组
//        for( int i = 0;i<where.length;i++){
//            SQLWhere sw = where[i];
//            if(i==0){
//                sb.append(" WHERE ");
//                sb.append(sw);
//                params.add(sw.value);
//            }else{
//                sb.append(" ").append(sw.join).append(" ");
//                sb.append(sw);
//                params.add(sw.value);
//            }
//            if(i<where.length-1){
//                sb.append(" , ");
//            }
//        }
//        return new SQLStatement(sb.toString(),params.toArray());
//    }
//}