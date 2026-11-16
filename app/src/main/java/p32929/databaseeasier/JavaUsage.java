package p32929.databaseeasier;

import android.content.Context;
import android.database.Cursor;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

/** Not used by the app. It only proves the 1.x Java calls still compile against 2.0.0. */
final class JavaUsage {
    private JavaUsage() {}

    static void demo(Context context) {
        EasyDB easyDB = EasyDB.init(context, "TEST")
                .setTableName("DEMO TABLE")
                .addColumn("C1", "text")
                .addColumn(new Column("C2", "text", "unique"))
                .doneTableColumn();

        boolean added = easyDB.addData(1, "Data1").addData("C2", 2).doneDataAdding();
        Cursor all = easyDB.getAllDataOrderedBy(1, false);
        all.close();
        Cursor row = easyDB.searchInColumn("C1", "Data1", -1);
        if (row != null) row.close();
        boolean matched = easyDB.matchColumns(new String[]{"C1"}, new String[]{"Data1"});
        boolean updated = easyDB.updateData(1, "New").rowID(1);
        boolean deleted = easyDB.deleteRow(1);
        easyDB.deleteAllDataFromTable();
    }
}
