package core;

public class MySQLConvertot implements TypeConvertor {
    @Override
    public String databaseType2JavaType(String databaseType) {

        if("int".equalsIgnoreCase(databaseType))
            return "Integer";
        if("varchar".equalsIgnoreCase(databaseType))
            return "String";
        if("double".equalsIgnoreCase(databaseType))
            return "Double";
        return null;
    }

    @Override
    public String javaType2DatabaseType(String javaType) {
        return null;
    }
}
