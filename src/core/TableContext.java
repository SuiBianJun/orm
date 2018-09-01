package core;

import javabean.ColumnInfo;
import javabean.TableInfo;
import utils.CreatePoClass;
import utils.JavaSrcCreator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableContext {

	// all table info in connected db
	// K->table name
	// V->table info
    public static Map<String, TableInfo>
            tables = new HashMap<String, TableInfo>();
   
    // po class relate to TableInfo
    public static Map<Class<?>, TableInfo>
            poClassTableMap = new HashMap<Class<?>, TableInfo>();
    
    // entity-mapping
    public static Map<String, Class<?>> entityMap = new HashMap<>();

    private TableContext() {

    }

    // get all table info in connected db
    static {
        try {
            //Property property = new Property();
            //DBManager dbManager = new DBManager(property, "db.properties");
            //DBManager dbManager = new DBManager("db.properties");
        	
        	// single model
            Connection con = SingleConnection.getInstance().getConnection();
            DatabaseMetaData dbmd = con.getMetaData();

            ResultSet tableRet = dbmd.getTables(null,
                    "%", "%", new String[]{"TABLE"});
            // get all table info in connected db
            while (tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");

				// tableinfo: tableName,column,columinfo
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
				// all tableInfo in this map
				// tableName<->tableInfo
				tables.put(tableName, ti);

				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");
				// query all column name in a table
				while (set.next()) {
					// columninfo: columnName,columnType, primaryFlag
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), set.getString("TYPE_NAME"), 0);
					// columnName<->columnInfo
					ti.getTableInfoMap().put(set.getString("COLUMN_NAME"), ci);
					// columnList
					ti.getColumnInfoList().add(ci);
				}

				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);
				// query primary key
				while (set2.next()) {
					ColumnInfo ci2 = (ColumnInfo) ti.getTableInfoMap().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);
					// setting primary
					ti.getPrimaryKeyList().add(ci2);
				}
				if (ti.getPrimaryKeyList().size() > 1) { // 取唯一主键。。方便使用。如果是联合主键。则为空！
					ti.setOnlyPrimaryKey(ti.getPrimaryKeyList().get(0));
				} else if (ti.getPrimaryKeyList().size() == 1) {
					ti.setOnlyPrimaryKey(ti.getPrimaryKeyList().get(0));
				}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        try {
			loadPoTables();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Map<String, TableInfo> map = TableContext.tables;
        //System.out.println(map);
        JavaSrcCreator jsc = new JavaSrcCreator(map);
        jsc.create();
    }

    // add info to some map
    public static Map<Class<?>, TableInfo> loadPoTables() throws IOException {

        JavaSrcCreator jsc = new JavaSrcCreator(tables);
        for(String className : tables.keySet()) {
        	// create .java file
            CreatePoClass.create(jsc.createSrcCode(className), className);

            try {
            	// load class
                Class<?> cls = Class.forName("po." + className.substring(0, 1).toUpperCase() + className.substring(1, className.length()));
                //Object object = cls.newInstance();
                
                // add entityMapping info
                entityMap.put(className, cls);
                
                poClassTableMap.put(cls, tables.get(className));
                //Method[] method = cls.getMethods();


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        
        //System.out.println("entityMap size : " + entityMap.get("t1"));

        return poClassTableMap;


    }

    public static void main(String[] args) throws IOException {
        //Map<String, TableInfo> map = TableContext.tables;
        //System.out.println(map);

        //JavaSrcCreator jsc = new JavaSrcCreator(map);
        //System.out.println(jsc.createSrcCode());

        loadPoTables();

    }
}
