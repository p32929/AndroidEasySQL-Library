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

public class EasyDB extends SQLiteOpenHelper {


    // Variables
    private String DATABASE_NAME, TABLE_NAME, SQL = "";
    private ArrayList<Column> columns = new ArrayList<>();
    private SQLiteDatabase writableDatabase;
    ContentValues contentValues = new ContentValues();
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

    //
    public EasyDB updateData(int columnNumber, String data) {
        if (!initedDb || writableDatabase == null) initDatabase();
        contentValues.put(columns.get(columnNumber - 1).columnName, data);
        return this;
    }

    public boolean rowToUpdate(int id) {
        return writableDatabase.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)}) > 0;
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
    public void initDatabase() {
        writableDatabase = getWritableDatabase();
        initedDb = true;
    }

    //
    public static EasyDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
        return new EasyDB(context, dbName, factory, version);
    }

    public static EasyDB init(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        if (!dbName.endsWith(".db"))
            dbName += ".db";
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
