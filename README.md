# UPDATE!!!
A newer, easier and better version of this library (V2) can be found here:
<br>
https://github.com/p32929/EasiestSqlLibrary

But if you want, you can still use this library. If you like Flutter, the flutter version of this library can be found here -> [Github](https://github.com/p32929/EasiestdbFlutter) or [pub.dev](https://pub.dev/packages/easiestdb) . Thanks...

# AndroidEasySQL-Library
An Easier & Lazier approach to SQL database for Android


## Installation
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency
```gradle
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

```java
EasyDB easyDB = EasyDB.init(this, "TEST") // TEST is the name of the DATABASE
                       .setTableName("DEMO TABLE")  // You can ignore this line if you want
                       .addColumn("C1", "text") // Contrains like "text", "unique", "not null" are not case sensitive
                       .addColumn("C2", "text", "unique")
                       .addColumn("C3", "text", "unique", "not null")
                       .doneTableColumn();
```

or

```java
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
```java
boolean done = easyDB.addData(1, "Data1")
                .addData(2, "Data2")
                .addData(3, "Data3")
                .doneDataAdding();
```

or

```java
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
```java
int anIntegerVariable = res.getInt(columnIndex);
String aStringVariable = res.getString(columnIndex);
```

here ```columnIndex``` is an integer, starts from 0.

Example:

```java
Cursor res = easyDB.getAllData();
while (res.moveToNext()) {
    int anIntegerVariable = res.getInt(columnIndex);
    String aStringVariable = res.getString(columnIndex);
}
```

or

```java
Cursor res = easyDB.getAllDataOrderedBy(columnNumber, false);
while (res.moveToNext()) {
    int anIntegerVariable = res.getInt(columnIndex);
    String aStringVariable = res.getString(columnIndex);
}
```


### Get/Read one row data:
To get data from a row, call ```getOneRowData(rowID)```. It will return the data as a Cursor object. You can then retrieve each column data from the cursor.
Example:
```java
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
```java
Cursor res = easyDB.searchInColumn(1, "data", 1); // Here we passed limit = 1. Thus it will return only one row data with the matched column value
if (res != null) {
    res.moveToFirst(); // Because here's only one row data
    String ID = res.getString(0); // Column 0 is the ID column
    String c1 = res.getString(1);
    String c2 = res.getString(2);
}
```

or

```java
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

```java
String columnsToMatch[] = new String[]{columns[1], columns[2]};
```
And an array of values(String) you want to match, like this:

```java
String valuesToMatch[] = new String[]{valueToSearchInColumn1, valueToSearchInColumn2};
```

Now call ```matchColumns()``` like this:

```boolean matched = easyDB.matchColumns(columnsToMatch, valuesToMatch);```

Thus, it will return a boolean value. So, you can know if your given values are matched or not.

** Creating these two array variables(```columnsToMatch``` & ```valuesToMatch```) will make it easier to pass them into ```matchColumns()``` method. **

### Update / Edit data:
To update / Edit, call ```updateData(columnNumber, data)``` method.
Example:
```java
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

## License

MIT License — Copyright (c) 2018 Fayaz Bin Salam. See [LICENSE](LICENSE) for the full text.

## Contributing

Contributions are warmly welcomed and greatly appreciated! Whether it's a bug fix, new feature, or improvement, your input helps make this project better for everyone.

Before submitting a pull request, please:

1. Create an issue describing the feature or bug fix you'd like to work on
2. Wait for discussion and approval to ensure alignment with project goals
3. Fork the repository and create your feature branch
4. Submit your pull request with a clear description of changes

This approach helps avoid duplicate efforts and ensures smooth collaboration. Thank you for considering contributing!

## Share

Sharing this repository with your friends is just one click away from here

[![facebook](https://user-images.githubusercontent.com/6418354/179013321-ac1d1452-0689-493f-9066-940cf2302b6e.png)](https://www.facebook.com/sharer/sharer.php?u=https://github.com/p32929/AndroidEasySQL-Library/)
[![twitter](https://user-images.githubusercontent.com/6418354/179013351-7d8d6d1c-4ce2-46ab-bef8-4c4765a1b888.png)](https://twitter.com/intent/tweet?url=https://github.com/p32929/AndroidEasySQL-Library/)
[![tumblr](https://user-images.githubusercontent.com/6418354/179013343-3111f55a-3b90-40c7-8487-9777348672b0.png)](https://www.tumblr.com/share?v=3&u=https://github.com/p32929/AndroidEasySQL-Library/)
[![pocket](https://user-images.githubusercontent.com/6418354/179013334-b095c45f-becf-49f4-9ee1-5a731a9b1f85.png)](https://getpocket.com/save?url=https://github.com/p32929/AndroidEasySQL-Library/)
[![pinterest](https://user-images.githubusercontent.com/6418354/179013331-44cd9206-11b1-4b65-becb-5863b61c828f.png)](https://pinterest.com/pin/create/button/?url=https://github.com/p32929/AndroidEasySQL-Library/)
[![reddit](https://user-images.githubusercontent.com/6418354/179013338-7416ae3f-73ba-4522-86e1-1374d7082d22.png)](https://www.reddit.com/submit?url=https://github.com/p32929/AndroidEasySQL-Library/)
[![linkedin](https://user-images.githubusercontent.com/6418354/179013327-ca7b7102-1da8-4b1c-858f-1a6e5f21bd70.png)](https://www.linkedin.com/shareArticle?mini=true&url=https://github.com/p32929/AndroidEasySQL-Library/)
[![whatsapp](https://user-images.githubusercontent.com/6418354/179013353-f477fa0b-3e6f-4138-a357-c9991b23ff88.png)](https://api.whatsapp.com/send?text=https://github.com/p32929/AndroidEasySQL-Library/)

---

## Support

If this saved you time, you can buy me a coffee — it keeps these projects maintained and free.

[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20me%20a%20coffee-%E2%98%95-FFDD00?style=for-the-badge&logo=buymeacoffee&logoColor=black)](https://www.buymeacoffee.com/p32929)

**Sponsor a mention** — $499 one-time: your name + link in this section for 3 months. **$1,200 one-time:** featured placement at the top of this section, plus a pinned mention on [my X profile](https://x.com/p32929), for 3 months. Email **[fayazdevinbox@uberip.com](mailto:fayazdevinbox@uberip.com)** for an invoice.

<!-- hire-block -->

---

## 💼 Using this at a company?

I do fixed-price delivery work on my own projects. One invoice, one date, no hourly billing:

| | |
|---|---|
| **White-label build** — this project rebranded, extended and deployed as yours | **$6,500** · 3 weeks |
| **Custom app from scratch** on my own stack, signed and auto-updating | **$12,500** · 6 weeks |
| **Production-hardening sprint** — 72 hours on this project, for your load and your security review | **$999** |
| **Ongoing capacity** — one project-week of my time reserved every month | **$9,000 / month** |

Full details → **[p32929.github.io/hire](https://p32929.github.io/hire/)** · Email **[fayazdevinbox@uberip.com](mailto:fayazdevinbox@uberip.com)** — scoping and quotes are free and I answer within one business day.
