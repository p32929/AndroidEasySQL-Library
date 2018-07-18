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
  implementation 'com.github.p32929:AndroidEasySQL-Library:1.1'
}
```

## Usage
Steps to follow:
* Initialize
* Set Table Name
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
EasyDB easyDB = EasyDB.init(this, "TEST", null, 1)
                .setTableName("DEMO TABLE")  // You can ignore this line if you want :D
                .addColumn(new Column("C1", DataType.TEXT))
                .addColumn(new Column("C2", DataType.TEXT))
                .addColumn(new Column("C3", DataType.TEXT))
                .doneTableColumn();
```
> addColumn(column)

> Column(columnName, dataType)

saving the object into a variable will make easier to work with the database later.

** You don't have to add any primary key. The library does it by default **

### Adding data:
You can call the ```addData()``` in two ways:

> addData(columnNumber, data)

> addData(columnName, data)

```data``` can be either ```integer``` or ```string```
after adding all data, call ```doneDataAdding()``` method.
```columnName``` is String and ```columnNumber``` is integer

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

### Showing data:
To get all data as a ```Cursor``` object, call ```getAllData()``` like this:
```Cursor res = easyDB.getAllData();```

Later use a while loop like this:
```
while (res.moveToNext()) {
	// Your code here
}
```

To get an integer from ```res``` call ```getInt(columnIndex)``` and to get a String call ```getString(columnIndex)``` like this:
```
int anIntegerVariable = res.getString(columnIndex);
String aStringVariable = res.getString(columnIndex);
```

here ```columnIndex``` is an integer, starts from 0

Example
```
while (res.moveToNext()) {
	int anIntegerVariable = res.getString(columnIndex);
    String aStringVariable = res.getString(columnIndex);
}
```

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

### Delete a table fully:
To delete a table and its all data, call ```deleteAllDataFromTable``` like this:

```easyDB.deleteAllDataFromTable();```

Hope you'll enjoy using the library :)

> Thanks

## Lisense:
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