package p32929.databaseeasier;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.DataType;
import p32929.androideasysql_library.EasyDB;

public class MainActivity extends AppCompatActivity {

    //
    EasyDB easyDB;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.dataText);

        easyDB = EasyDB.init(this, "TEST", null, 1)
                .addColumn(new Column("C1", DataType.TEXT))
                .addColumn(new Column("C2", DataType.TEXT))
                .addColumn(new Column("C3", DataType.TEXT))
                .doneTableColumn();
    }

    // Show
    public void showData(View view) {
        Cursor res = easyDB.getAllData();
        String totalData = "";

        while (res.moveToNext()) {
            String id = res.getString(0);
            String sa = res.getString(1);
            String sb = res.getString(2);
            String sc = res.getString(3);

            totalData += "ID " + id + " , SA = " + sa + " , SB = " + sb + " , SC = " + sc + "\n";
        }

        if (totalData.isEmpty()) textView.setText("EMPTY DB");
        else textView.setText(totalData);
    }


    // Add
    int dataInt = 0;

    public void addData(View view) {
        boolean done = easyDB.addData(1, "A" + ++dataInt)
                .addData(2, "B" + ++dataInt)
                .addData(3, "C" + ++dataInt)
                .doneDataAdding();

        Toast.makeText(this, "Data Added: " + done, Toast.LENGTH_SHORT).show();
    }


    // update
    int updateInt = 0;
    int row = 0;

    public void editData(View view) {
        boolean updated = easyDB.updateData(1, "AA" + ++updateInt)
                .updateData(2, "BB" + ++updateInt)
                .updateData(3, "CC" + ++updateInt)
                .rowID(++row);

        Toast.makeText(this, "Updated: " + updated, Toast.LENGTH_SHORT).show();
    }


    // Delete
    int deleteInt = 0;

    public void deleteData(View view) {
        boolean deleted = easyDB.deleteRow(++deleteInt);
        Toast.makeText(this, "Deleted: " + deleted, Toast.LENGTH_SHORT).show();
    }

    public void deleteAll(View view) {
        easyDB.deleteAllDataFromTable();
        Toast.makeText(this, "All data from table deleted", Toast.LENGTH_SHORT).show();
    }
}
