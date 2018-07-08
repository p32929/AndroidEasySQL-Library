package p32929.androideasysql_library;

/**
 * Created by p32929 on 7/8/18.
 */

public class Column {

    //
    String columnName, columnDataType;

    //
    public Column(String columnName, String columnDataType) {
        this.columnName = columnName.replaceAll(" ", "_");
        this.columnDataType = columnDataType.replaceAll(" ", "_");
    }
}
