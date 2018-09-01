package javabean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {

    // table name
    String tableName;
    // 
    // K->V == tableName<->columninfo
     
    Map<String, ColumnInfo> tableInfoMap = new HashMap<String, ColumnInfo>();
    
    // column info list
    List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();

    // primary key info list 
    List<ColumnInfo> primaryKeyList = new ArrayList<ColumnInfo>();
    
    ColumnInfo onlyPrimaryKey = null;

    public TableInfo(){}

    public List<ColumnInfo> getPrimaryKeyList(){

        return primaryKeyList;
    }
    
    public void setOnlyPrimaryKey(ColumnInfo columnInfo) {
    	
    	onlyPrimaryKey = columnInfo;
    }
    
    public ColumnInfo getOnlyPrimaryKey() {
    	
    	return onlyPrimaryKey;
    }

    public TableInfo(String tableName, List<ColumnInfo> columnInfoList, Map<String, ColumnInfo> tableInfoMap) {
        this.tableName = tableName;
        this.tableInfoMap = tableInfoMap;
        this.columnInfoList = columnInfoList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ColumnInfo> getTableInfoMap() {
        return tableInfoMap;
    }

    public void setTableInfoMap(Map<String, ColumnInfo> tableInfoMap) {
        this.tableInfoMap = tableInfoMap;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }
    
    
}
