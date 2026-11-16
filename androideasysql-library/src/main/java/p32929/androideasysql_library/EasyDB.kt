package p32929.androideasysql_library

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseErrorHandler
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/*
MIT License, Copyright (c) 2018 Fayaz Bin Salam. See LICENSE.
 */

/**
 * A small, chainable SQLite helper. Create a table in one chain, then add, read,
 * update and delete rows without writing SQL. Every table gets an `ID` primary key.
 *
 * ```
 * val db = EasyDB.init(context, "Notes")
 *     .setTableName("Notes")
 *     .addColumn("Title", "text", "not null")
 *     .addColumn("Body", "text")
 *     .doneTableColumn()
 * ```
 */
class EasyDB private constructor(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int,
    errorHandler: DatabaseErrorHandler?,
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {

    private var tableName = "DEMO_TABLE"
    private val columns = ArrayList<Column>()
    private var contentValues = ContentValues()
    private val db: SQLiteDatabase get() = writableDatabase

    // ---- Table setup ----

    fun setTableName(tableName: String): EasyDB = apply { this.tableName = tableName.replace(" ", "_") }

    fun addColumn(column: Column): EasyDB = apply { columns.add(column) }

    fun addColumn(columnName: String, vararg columnDataTypes: String): EasyDB =
        apply { columns.add(Column(columnName, *columnDataTypes)) }

    /** Call after the last [addColumn]. Creates the table the first time the database opens. */
    fun doneTableColumn(): EasyDB = apply { db }

    /** All column names, with `ID` first. */
    fun getAllColumns(): Array<String> = (listOf("ID") + columns.map { it.columnName }).toTypedArray()

    // ---- Add ----

    fun addData(columnNumber: Int, data: String?): EasyDB = put(columnNumber, data)
    fun addData(columnNumber: Int, data: Int): EasyDB = put(columnNumber, data)
    fun addData(columnNumber: Int, data: Long): EasyDB = put(columnNumber, data)
    fun addData(columnNumber: Int, data: Double): EasyDB = put(columnNumber, data)
    fun addData(columnName: String, data: String?): EasyDB = put(columnName, data)
    fun addData(columnName: String, data: Int): EasyDB = put(columnName, data)
    fun addData(columnName: String, data: Long): EasyDB = put(columnName, data)
    fun addData(columnName: String, data: Double): EasyDB = put(columnName, data)

    /** Saves the row built with [addData]. Returns true if it was saved. */
    fun doneDataAdding(): Boolean {
        val values = contentValues
        contentValues = ContentValues()
        return db.insert(tableName, null, values) != -1L
    }

    // ---- Read ----

    fun getAllData(): Cursor = db.rawQuery("SELECT * FROM $tableName", null)

    /** [columnNumber] 0 is `ID`, 1 is your first column, and so on. */
    fun getAllDataOrderedBy(columnNumber: Int, ascending: Boolean): Cursor =
        db.rawQuery("SELECT * FROM $tableName ORDER BY ${columnName(columnNumber)}${if (ascending) "" else " DESC"}", null)

    /** Every row as a list of `column name -> value` maps, so you don't have to touch a Cursor. */
    @JvmOverloads
    fun getAllRows(orderByColumnNumber: Int = 0, ascending: Boolean = true): List<Map<String, Any?>> =
        getAllDataOrderedBy(orderByColumnNumber, ascending).use { it.toRows() }

    /** Number of rows in the table. */
    fun count(): Long = DatabaseUtils.queryNumEntries(db, tableName)

    /** The row with this ID, or null. */
    fun getOneRowData(rowID: Int): Cursor? = query("ID", rowID.toString(), "1")

    @Deprecated("Use searchInColumn(columnNumber, value, 1)", ReplaceWith("searchInColumn(columnNumber, value, 1)"))
    fun getOneRowData(columnNumber: Int, value: String): Cursor? = searchInColumn(columnNumber, value, 1)

    @Deprecated("Use searchInColumn(columnName, value, 1)", ReplaceWith("searchInColumn(columnName, value, 1)"))
    fun getOneRowData(columnName: String, value: String): Cursor? = searchInColumn(columnName, value, 1)

    /** Rows where the column equals [valueToSearch], or null if none. Pass [limit] -1 for all of them. */
    fun searchInColumn(columnNumber: Int, valueToSearch: String, limit: Int): Cursor? =
        query(columnName(columnNumber), valueToSearch, limitOf(limit))

    fun searchInColumn(columnName: String, valueToSearch: String, limit: Int): Cursor? =
        query(columnName.replace(" ", "_"), valueToSearch, limitOf(limit))

    /** True if one row has all these values in these columns. */
    fun matchColumns(columnsToMatch: Array<String>, valuesToMatch: Array<String>): Boolean {
        val where = columnsToMatch.joinToString(" AND ") { "$it = ?" }
        return db.query(tableName, columnsToMatch, where, valuesToMatch, null, null, null, "1").use { it.count > 0 }
    }

    // ---- Update ----

    fun updateData(columnNumber: Int, data: String?): EasyDB = put(columnNumber, data)
    fun updateData(columnNumber: Int, data: Int): EasyDB = put(columnNumber, data)
    fun updateData(columnNumber: Int, data: Long): EasyDB = put(columnNumber, data)
    fun updateData(columnNumber: Int, data: Double): EasyDB = put(columnNumber, data)
    fun updateData(columnName: String, data: String?): EasyDB = put(columnName, data)
    fun updateData(columnName: String, data: Int): EasyDB = put(columnName, data)
    fun updateData(columnName: String, data: Long): EasyDB = put(columnName, data)
    fun updateData(columnName: String, data: Double): EasyDB = put(columnName, data)

    /** Saves the changes built with [updateData] to the row with this ID. Returns true if it changed. */
    fun rowID(id: Int): Boolean {
        val values = contentValues
        contentValues = ContentValues()
        return try {
            db.update(tableName, values, "ID = ?", arrayOf(id.toString())) > 0
        } catch (e: Exception) {
            false
        }
    }

    // ---- Delete ----

    /** Deletes the row with this ID. Returns true if a row was deleted. */
    fun deleteRow(id: Int): Boolean = delete("ID", id.toString())

    /** Deletes every row where the column matches. Returns true if at least one row was deleted. */
    fun deleteRow(columnNumber: Int, valueToMatch: Int): Boolean = delete(columnName(columnNumber), valueToMatch.toString())
    fun deleteRow(columnNumber: Int, valueToMatch: String): Boolean = delete(columnName(columnNumber), valueToMatch)
    fun deleteRow(columnName: String, valueToMatch: Int): Boolean = delete(columnName.replace(" ", "_"), valueToMatch.toString())
    fun deleteRow(columnName: String, valueToMatch: String): Boolean = delete(columnName.replace(" ", "_"), valueToMatch)

    /** Deletes every row. The table stays. */
    fun deleteAllDataFromTable() {
        db.execSQL("DELETE FROM $tableName")
    }

    // ---- SQLiteOpenHelper ----

    override fun onCreate(db: SQLiteDatabase) {
        val cols = columns.joinToString("") { ", ${it.columnName} ${it.columnDataType}" }
        db.execSQL("CREATE TABLE $tableName (ID INTEGER PRIMARY KEY AUTOINCREMENT$cols)")
    }

    // ponytail: a version bump drops the table, same as 1.x. Override nothing else; write your own migration if you need to keep data.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    // ---- Helpers ----

    private fun columnName(columnNumber: Int) = if (columnNumber == 0) "ID" else columns[columnNumber - 1].columnName

    private fun limitOf(limit: Int) = if (limit < 1) null else limit.toString()

    private fun put(columnNumber: Int, data: Any?) = put(columnName(columnNumber), data)

    private fun put(columnName: String, data: Any?): EasyDB = apply {
        val key = columnName.replace(" ", "_")
        when (data) {
            null -> contentValues.putNull(key)
            is String -> contentValues.put(key, data)
            is Int -> contentValues.put(key, data)
            is Long -> contentValues.put(key, data)
            is Double -> contentValues.put(key, data)
        }
    }

    private fun query(column: String, value: String, limit: String?): Cursor? {
        val cursor = db.query(tableName, getAllColumns(), "$column = ?", arrayOf(value), null, null, null, limit)
        if (cursor.count > 0) return cursor
        cursor.close()
        return null
    }

    private fun delete(column: String, value: String) = db.delete(tableName, "$column = ?", arrayOf(value)) > 0

    private fun Cursor.toRows(): List<Map<String, Any?>> {
        val rows = ArrayList<Map<String, Any?>>(count)
        while (moveToNext()) {
            rows += (0 until columnCount).associate { i ->
                getColumnName(i) to when (getType(i)) {
                    Cursor.FIELD_TYPE_NULL -> null
                    Cursor.FIELD_TYPE_INTEGER -> getLong(i)
                    Cursor.FIELD_TYPE_FLOAT -> getDouble(i)
                    Cursor.FIELD_TYPE_BLOB -> getBlob(i)
                    else -> getString(i)
                }
            }
        }
        return rows
    }

    companion object {
        private fun fileName(dbName: String) = (if (dbName.endsWith(".db")) dbName else "$dbName.db").replace(" ", "_")

        @JvmStatic
        fun init(context: Context, dbName: String): EasyDB = init(context, dbName, null, 1, null)

        @JvmStatic
        fun init(context: Context, dbName: String, version: Int): EasyDB = init(context, dbName, null, version, null)

        @JvmStatic
        fun init(context: Context, dbName: String, factory: SQLiteDatabase.CursorFactory?, version: Int): EasyDB =
            init(context, dbName, factory, version, null)

        @JvmStatic
        fun init(
            context: Context,
            dbName: String,
            factory: SQLiteDatabase.CursorFactory?,
            version: Int,
            errorHandler: DatabaseErrorHandler?,
        ): EasyDB = EasyDB(context.applicationContext, fileName(dbName), factory, version, errorHandler)
    }
}
