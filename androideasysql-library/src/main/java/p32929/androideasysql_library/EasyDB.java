package p32929.androideasysql_library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by p32929 on 7/8/18.
 */

/*
MIT License

Copyright (c) 2018 Fayaz Bin Salam

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

public class EasyDB extends SQLiteOpenHelper {


    // Variables
    private String DATABASE_NAME, TABLE_NAME = "DEMO_TABLE", SQL = "";
    private ArrayList<Column> columns = new ArrayList<>();
    private SQLiteDatabase writableDatabase;
    private ContentValues contentValues = new ContentValues();
    private boolean initedDb = false;

    //
    public EasyDB addData(int columnNumber, String data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public EasyDB addData(String columnName, String data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }

    public EasyDB addData(int columnNumber, int data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public EasyDB addData(String columnName, int data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }


    public boolean doneDataAdding() {
        long result = writableDatabase.insert(TABLE_NAME, null, contentValues);
        contentValues = new ContentValues();

        if (result == -1)
            return false;
        else
            return true;
    }

    //
    public Cursor getAllData() {
        if (!initedDb || writableDatabase == null) initDatabase();
        Cursor res = writableDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getAllDataOrderedBy(int columnNumber, boolean ascending) {
        String postfix = ascending ? "" : " DESC ";
        String colNam = columnNumber == 0 ? " ID " : columns.get(columnNumber - 1).columnName;
        if (!initedDb || writableDatabase == null) initDatabase();
        Cursor res = writableDatabase.rawQuery("select * from " + TABLE_NAME + " ORDER BY " + colNam + postfix, null);
        return res;
    }

    //
    public Cursor getOneRowData(int rowID) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames, allColNames[0].toString() + "=?",
                new String[]{String.valueOf(rowID)},
                null, null, null, "1");

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    @Deprecated
    public Cursor getOneRowData(int columnNumber, String value) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames, allColNames[columnNumber].toString() + "=?",
                new String[]{value},
                null, null, null, "1");

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    @Deprecated
    public Cursor getOneRowData(String columnName, String value) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames, " " + columnName + " " + "=?",
                new String[]{value},
                null, null, null, "1");

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    public Cursor searchInColumn(int columnNumber, String valueToSearch, int limit) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames, allColNames[columnNumber].toString() + "=?",
                new String[]{valueToSearch},
                null, null, null, limit == -1 ? null : String.valueOf(limit));

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    public Cursor searchInColumn(String columnName, String valueToSearch, int limit) {
        if (!initedDb || writableDatabase == null) initDatabase();
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME,
                allColNames, " " + columnName + " " + "=?",
                new String[]{valueToSearch},
                null, null, null, limit == -1 ? null : String.valueOf(limit));

        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    //
    public boolean matchColumns(String columnsToMatch[], String valuesToMatch[]) {
        String query = "";
        for (int i = 0; i < columnsToMatch.length; i++) {
            query += columnsToMatch[i] + " = ? ";
            if (i != columnsToMatch.length - 1) {
                query += " AND ";
            }
        }
        Cursor cursor = writableDatabase.query(TABLE_NAME, columnsToMatch, query, valuesToMatch, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //
    public EasyDB updateData(int columnNumber, String data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public EasyDB updateData(int columnNumber, int data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public EasyDB updateData(String columnName, String data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }

    public EasyDB updateData(String columnName, int data) {
        columnName = columnName.replaceAll(" ", "_");
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columnName, data);
        return this;
    }


    public boolean rowID(int id) {
        try {
            return writableDatabase.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)}) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    //
    public boolean deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)}) == 1;
    }

    public void deleteAllDataFromTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    //
    public EasyDB setTableName(String tableName) {
        this.TABLE_NAME = tableName.replaceAll(" ", "_");
        return this;
    }

    public EasyDB addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public EasyDB doneTableColumn() {
        SQL = " CREATE TABLE " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
        for (int i = 0; i < columns.size(); i++) {
            SQL += " " + columns.get(i).columnName + " " + columns.get(i).columnDataType + " ";
            if (i == columns.size() - 1) {
                SQL += " ) ";
            } else {
                SQL += " , ";
            }
        }

        if (!initedDb || writableDatabase == null) initDatabase();
        return this;
    }

    //
    public String[] getAllColumns() {
        String allColNames[] = new String[columns.size() + 1];
        allColNames[0] = "ID";
        for (int i = 0; i < columns.size(); i++) {
            allColNames[i + 1] = columns.get(i).columnName;
        }
        return allColNames;
    }

    //
    private void initDatabase() {
        writableDatabase = getWritableDatabase();
        initedDb = true;
    }

    //
    public static EasyDB init(Context context, String dbName) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new EasyDB(context, dbName, null, 1);
    }

    public static EasyDB init(Context context, String dbName, int version) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new EasyDB(context, dbName, null, version);
    }

    public static EasyDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new EasyDB(context, dbName, factory, version);
    }

    public static EasyDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        dbName = dbName.replaceAll(" ", "_");
        return new EasyDB(context, dbName, factory, version, errorHandler);
    }

    //
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.writableDatabase = db;
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Saving, just in case :)
    // Codes below this might once or never be used :D
    private Context context;
    private SQLiteDatabase.CursorFactory factory;
    private int version;
    private DatabaseErrorHandler errorHandler;

    private EasyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        //
        this.context = context;
        this.DATABASE_NAME = name;
        this.factory = factory;
        this.version = version;
    }

    private EasyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);

        //
        this.context = context;
        this.DATABASE_NAME = name;
        this.factory = factory;
        this.version = version;
        this.errorHandler = errorHandler;
    }
}
