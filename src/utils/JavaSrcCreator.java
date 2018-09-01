package utils;

import core.MySQLConvertot;
import javabean.ColumnInfo;
import javabean.TableInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// construct .java source code

public class JavaSrcCreator {

    Map<String, TableInfo> map;
    String className;

    public JavaSrcCreator(Map<String, TableInfo> map) {
        this.map = map;
    }

    public String createHeader(){
        StringBuilder ch = new StringBuilder();
        ch.append("package po;\n");
        ch.append("import ").append("java.util.*;").append("\n");
        ch.append("import ").append("java.sql.*;").append("\n\n");

        return ch.toString();
    }

    public String createVariable(String className){

        StringBuilder cv = new StringBuilder();
        TableInfo ti = map.get(className);

        List<ColumnInfo> columnsInfoList = ti.getColumnInfoList();
        for(ColumnInfo ci : columnsInfoList){
            MySQLConvertot mysqlc= new MySQLConvertot();
            String javaType = mysqlc.databaseType2JavaType(ci.getType());
            cv.append("public ").append(javaType).append(" ").append(ci.getName()).append(";\n");
        }

        return cv.toString();
    }

    public String createConstructor(){

        StringBuilder cc = new StringBuilder();
        cc.append("\tpublic ").append(className.substring(0, 1).toUpperCase()).append(className.substring(1, className.length())).append("()").append("{\n");
        cc.append("\n\t}");
        return cc.toString();
    }

    // create POJO class .java source code
    public String createGetterSetterMethod(){
        StringBuilder cgsm = new StringBuilder();
        TableInfo ti = map.get(className);

        //System.out.println(ti.getTableName());
        List<ColumnInfo> columnsInfoList = ti.getColumnInfoList();
        //System.out.println(columnsInfoList.size());
        for(ColumnInfo ci : columnsInfoList){
            MySQLConvertot mysqlc= new MySQLConvertot();
            String javaType = mysqlc.databaseType2JavaType(ci.getType());
            //System.out.println(ci.getType());
            //public void name(type param){this.param =  param;}
            String name = ci.getName().toUpperCase().subSequence(0, 1) + ci.getName().substring(1);
            cgsm.append("\n\tpublic void ").append("set").append(name).append("(").append(javaType).append(" ").append(ci.getName()).append("){\n");
            cgsm.append("this.").append(ci.getName()).append("=").append(ci.getName()).append(";");
            cgsm.append("\n\t}\n");
            cgsm.append("public ").append(javaType).append(" ").append("get").append(name).append("()").append("{\n");
            cgsm.append("return ").append("this.").append(ci.getName()).append(";\n}");
        }
        //cgsm.append("");


        return cgsm.toString();
    }

    public String createSrcCode(String className){

        this.className = className;
        StringBuilder csc = new StringBuilder();
        csc.append(createHeader());
        csc.append("public class ").append(className.substring(0, 1).toUpperCase()).append(className.substring(1, className.length())).append("{\n\n");
        csc.append(createVariable(className));
        csc.append(createConstructor());
        csc.append(createGetterSetterMethod());

        csc.append("\n\t}");
        return csc.toString();
    }
    
    public void create() {
    	
    	Set<Entry<String, TableInfo>> set = map.entrySet();
    	
    	for(Entry<String, TableInfo> entry : set) {
    		createSrcCode(entry.getKey());
    	}
    }

}
