package javabean;

public class ColumnInfo {

    // columnname
    String name;

    // columnType
    String type;

    // columnInfo for this
    int keyType;

    public ColumnInfo(){}

    public ColumnInfo(String name, String type, int keyType) {
        this.name = name;
        this.type = type;
        this.keyType = keyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }
}
