# UPDATE!!!
A newer, easier and better version of this library (V2) can be found here:
<br>
https://github.com/p32929/EasiestSqlLibrary

But if you want, you can still use this library. If you like Flutter, the flutter version of this library can be found here -> [Github](https://github.com/p32929/EasiestSqlFlutter) or [pub.dev](https://pub.dev/packages/easiestdb) . Thanks...

# AndroidEasySQL-Library
An Easier &amp; Lazier approach to SQL database for Android

[![](https://badgen.net/github/release/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/release/p32929/AndroidEasySQL-Library/stable)]() [![](https://badgen.net/github/tag/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/watchers/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/checks/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/status/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/stars/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/forks/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/issues/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/open-issues/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/closed-issues/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/label-issues/p32929/AndroidEasySQL-Library/help-wanted/open)]() [![](https://badgen.net/github/prs/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/open-prs/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/closed-prs/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/merged-prs/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/commits/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/last-commit/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/branches/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/releases/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/tags/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/license/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/contributors/p32929/AndroidEasySQL-Library)]() [![](https://badgen.net/github/dependents-pkg/p32929/AndroidEasySQL-Library)]() 

## Share
Sharing with your friends is just one click away from here

[![facebook](https://image.flaticon.com/icons/png/32/124/124010.png)](https://www.facebook.com/sharer/sharer.php?u=https://github.com/p32929/AndroidEasySQL-Library)
[![twitter](https://image.flaticon.com/icons/png/32/124/124021.png)](https://twitter.com/intent/tweet?source=https://github.com/p32929/AndroidEasySQL-Library)
[![tumblr](https://image.flaticon.com/icons/png/32/124/124012.png)](https://www.tumblr.com/share?v=3&u=https://github.com/p32929/AndroidEasySQL-Library)
[![pocket](https://image.flaticon.com/icons/png/32/732/732238.png)](https://getpocket.com/save?url=https://github.com/p32929/AndroidEasySQL-Library)
[![pinterest](https://image.flaticon.com/icons/png/32/124/124039.png)](https://pinterest.com/pin/create/button/?url=https://github.com/p32929/AndroidEasySQL-Library)
[![reddit](https://image.flaticon.com/icons/png/32/2111/2111589.png)](https://www.reddit.com/submit?url=https://github.com/p32929/AndroidEasySQL-Library)
[![linkedin](https://image.flaticon.com/icons/png/32/1409/1409945.png)](https://www.linkedin.com/shareArticle?mini=true&url=https://github.com/p32929/AndroidEasySQL-Library)
[![whatsapp](https://image.flaticon.com/icons/png/32/733/733585.png)](https://api.whatsapp.com/send?text=https://github.com/p32929/AndroidEasySQL-Library)

## Support
If you like my works and want to support me/my works, feel free to support or donate. My payment details can be found here: https://p32929.github.io/SendMoney2Me/

## Installation
Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency
```
dependencies {
     implementation 'com.github.p32929:AndroidEasySQL-Library:1.4.1'
}
```

## Basic Usage
Steps to follow:
* Initialize
* Set Table Name (Not mandatory)
* Add columns
* Call done method

After that you can do:
* Add data
* Read data
* Edit data
* Delete data
* & so much more

## Code example
### Initialization, Set Table Name, Add columns, altogether:

```
EasyDB easyDB = EasyDB.init(this, "TEST") // TEST is the name of the DATABASE
                       .setTableName("DEMO TABLE")  // You can ignore this line if you want
                       .addColumn("C1", "text") // Contrains like "text", "unique", "not null" are not case sensitive
                       .addColumn("C2", "text", "unique")
                       .addColumn("C3", "text", "unique", "not null")
                       .doneTableColumn();
```

or

```
EasyDB easyDB = EasyDB.init(this, "TEST") // "TEST" is the name of the DATABASE
                .setTableName("DEMO TABLE")  // You can ignore this line if you want
                .addColumn(new Column("C1", "text", "unique")) // Contrains like "text", "unique", "not null" are not case sensitive
                .addColumn(new Column("C2", "text", "not null"))
                .addColumn(new Column("C3", "text"))
                .doneTableColumn();
```

** Saving the ```easyDB``` object into a variable will make easier to work with the database later. **

** You don't have to add any primary key. The library does it by default (as ```ID``` column) **

### Adding data:
You can call the ```addData()``` in two ways:

> addData(columnNumber, data) // ```columnNumber``` is an integer

> addData(columnName, data) // ```columnName``` is a String

```data``` parameter in ```addData()``` can be either integer or string. After adding all the data, call ```doneDataAdding()``` method.

Example:
```
boolean done = easyDB.addData(1, "Data1")
                .addData(2, "Data2")
                .addData(3, "Data3")
                .doneDataAdding();
```

or

```
boolean done = easyDB.addData("C1", "Data1")
                .addData("C2", "Data2")
                .addData("C3", "Data3")
                .doneDataAdding();
```

Thus, it will return a boolean value.
```True``` means data added successfully,
```False``` means data isn't added successfully.

### Get/Read All Data:
To get all data as a ```Cursor``` object, call ```getAllData()``` or ```getAllDataOrderedBy()``` like this:

```Cursor res = easyDB.getAllData();```

or

```Cursor res = easyDB.getAllDataOrderedBy(columnNumber, ascendingOrDescending);```

```ascendingOrDescending``` parameter in ```getAllDataOrderedBy()``` is a boolean value. To get all data in ascending order pass ```true```, or to get all data in descending order pass ```false``` as the parameter.

Later use a while loop like this:

```
while (res.moveToNext()) {
	// Your code here
}
```

To get an integer from ```res``` call ```getInt(columnIndex)``` and to get a String call ```getString(columnIndex)``` like this:
```
int anIntegerVariable = res.getInt(columnIndex);
String aStringVariable = res.getString(columnIndex);
```

here ```columnIndex``` is an integer, starts from 0.

Example:

```
Cursor res = easyDB.getAllData();
while (res.moveToNext()) {
    int anIntegerVariable = res.getInt(columnIndex);
    String aStringVariable = res.getString(columnIndex);
}
```

or

```
Cursor res = easyDB.getAllDataOrderedBy(columnNumber, false);
while (res.moveToNext()) {
    int anIntegerVariable = res.getInt(columnIndex);
    String aStringVariable = res.getString(columnIndex);
}
```


### Get/Read one row data:
To get data from a row, call ```getOneRowData(rowID)```. It will return the data as a Cursor object. You can then retrieve each column data from the cursor.
Example:
```
Cursor res = easyDB.getOneRowData(1);
if (res != null) {
    res.moveToFirst(); // Because here's only one row data
    String ID = res.getString(0); // Column 0 is the ID column
    String c1 = res.getString(1);
    String c2 = res.getString(2);
}
```

### Get/Read/Search one/multiple row data by matching a column data:
To get data from one/multiple rows by matching data with a column, call ```searchInColumn(columnNumber, valueToSearch, limit)``` or ```searchInColumn(columnName, valueToSearch, limit)```
example:
```
Cursor res = easyDB.searchInColumn(1, "data", 1); // Here we passed limit = 1. Thus it will return only one row data with the matched column value
if (res != null) {
    res.moveToFirst(); // Because here's only one row data
    String ID = res.getString(0); // Column 0 is the ID column
    String c1 = res.getString(1);
    String c2 = res.getString(2);
}
```

or

```
Cursor res = easyDB.searchInColumn("ID", "data", -1); // Here we passed limit = -1. Thus it will return all the rows with the matched column values
if (res != null) {
    while (res.moveToNext()) {
            String ID = res.getString(0); // Column 0 is the ID column
            String c1 = res.getString(1);
            String c2 = res.getString(2);
    }
}
```

> Please DO NOT pass ```limit = 0``` as the parameter

### Match data from multiple columns:
To check if some values exist or not in the database, first call ```getAllColumns()``` to get all the column names like this:

```String columns[] = easyDB.getAllColumns();```

Now, You may create an array of ```String``` defining which columns you want to match with some values, like this:

```
String columnsToMatch[] = new String[]{columns[1], columns[2]};
```
And an array of values(String) you want to match, like this:

```
String valuesToMatch[] = new String[]{valueToSearchInColumn1, valueToSearchInColumn2};
```

Now call ```matchColumns()``` like this:

```boolean matched = easyDB.matchColumns(columnsToMatch, valuesToMatch);```

Thus, it will return a boolean value. So, you can know if your given values are matched or not.

** Creating these two array variables(```columnsToMatch``` & ```valuesToMatch```) will make it easier to pass them into ```matchColumns()``` method. **

### Update / Edit data:
To update / Edit, call ```updateData(columnNumber, data)``` method.
Example:
```
boolean updated = easyDB.updateData(1, "UpdatedData1")
                .updateData(2, "UpdatedData2")
                .updateData(3, "UpdatedData3")
                .rowID(id);
```

UpdatedData can be either integer or String.
Thus, it will return a boolean value. So, you can know if your data is updated or not...

### Delete data:
To delete a row data, call ```deleteRow(rowId)``` like this:

```boolean deleted = easyDB.deleteRow(rowId);```

or

```boolean deleted = easyDB.deleteRow(columnNumber, valueToMatch)```

or

```boolean deleted = easyDB.deleteRow(columnName, valueToMatch)```

Thus, it will return a boolean value. So, you can know if your data is updated or not...

### Delete all data from the Table:
To delete the table and its all data, call ```deleteAllDataFromTable``` like this:

```easyDB.deleteAllDataFromTable();```

Hope you'll enjoy using the library :)

> Thanks

## License:
```
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
```
