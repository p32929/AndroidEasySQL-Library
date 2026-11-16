package p32929.androideasysql_library

/**
 * One column of the table: a name and its constraints, for example
 * `Column("Name", "text", "not null")`. Spaces in the name become underscores.
 */
class Column(columnName: String, vararg columnDataTypes: String) {
    @JvmField val columnName: String = columnName.replace(" ", "_")
    @JvmField val columnDataType: String =
        columnDataTypes.joinToString(" ") { it.trim().uppercase() }
}
