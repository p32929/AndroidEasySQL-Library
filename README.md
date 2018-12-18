# AndroidEasySQL-Library
An Easier &amp; Lazier approach to SQL database for Android

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
     implementation 'com.github.p32929:AndroidEasySQL-Library:1.3.6'
}
```

## Usage
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
* & more

## Code example
### Initialization, Set Table Name, Add columns, altogether:

```
EasyDB easyDB = EasyDB.init(this, "TEST", null, 1) // TEST is the name of the DATABASE
                .setTableName("DEMO TABLE")  // You can ignore this line if you want
                .addColumn(new Column("C1", new String[]{"text", "unique"}))
                .addColumn(new Column("C2", new String[]{"text", "not null"}))
                .addColumn(new Column("C3", new String[]{"text"}))
                .doneTableColumn();
```

or

```
EasyDB easyDB = EasyDB.init(this, "TEST", null, 1) // TEST is the name of the DATABASE
                .setTableName("DEMO TABLE")  // You can ignore this line if you want
                .addColumn(new Column("C1", new DataType()._text_().unique().done()))
                .addColumn(new Column("C2", new DataType()._text_().notNull().done()))
                .addColumn(new Column("C3", new DataType()._text_().done()))
                .doneTableColumn();
```

** You can add as many constraint methods like  ```unique()``` , ```notNull()``` etc. Just, don't forget to call the ```done()``` method at the end. See the example above...

** Saving the ```easyDB``` object into a variable will make easier to work with the database later. **

** You don't have to add any primary key. The library does it by default (as ```ID``` column) **

### Adding data:
You can call the ```addData()``` in two ways:

> addData(columnNumber, data)

> addData(columnName, data)

```data``` parameter in ```addData()``` can be either integer or string. After adding all data, call ```doneDataAdding()``` method.

 ```columnName``` is a String and ```columnNumber``` is an integer

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

```ascendingOrDescending``` parameter in ```getAllDataOrderedBy()``` is a boolean value. To get all data in ascending order pass ```true```, or to get all in descending order pass ```false``` as the parameter.

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
To get all column data from a row, call ```getOneRowData(rowID)```. It will return the data as a Cursor object. You can then retrieve each column data from the cursor.
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
