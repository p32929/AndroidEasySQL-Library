package p32929.databaseeasier;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class MainActivity extends AppCompatActivity {

    EditText editTextC1, editTextC2;
    Button buttonSaver, buttonShow, buttonEdit, buttonDelete, buttonMatch;
    TextView textViewResult;

    EasyDB easyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextC1 = findViewById(R.id.c1);
        editTextC2 = findViewById(R.id.c2);
        buttonSaver = findViewById(R.id.saver_button);
        buttonShow = findViewById(R.id.show_button);
        buttonEdit = findViewById(R.id.edit_button);
        buttonDelete = findViewById(R.id.delete_button);
        buttonMatch = findViewById(R.id.match_button);
        textViewResult = findViewById(R.id.res);

        easyDB = EasyDB.init(this, "TEST") // TEST is the name of the DATABASE
                .setTableName("DEMO TABLE")  // You can ignore this line if you want
                .addColumn("C1", "text")
                .addColumn("C2", "text", "unique")
                .doneTableColumn();

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData();
            }
        });

        buttonSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1 = editTextC1.getText().toString();
                String c2 = editTextC2.getText().toString();

                if (c1 != null && c2 != null) {
                    if (!c1.isEmpty() && !c2.isEmpty()) {
                        boolean done = easyDB.addData(1, c1)
                                .addData(2, c2)
                                .doneDataAdding();

                        Toast.makeText(MainActivity.this, "Done: " + done, Toast.LENGTH_SHORT).show();
                        showData();
                    }
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Edit Data")
                        .setMessage("Enter row number")
                        .setView(taskEditText)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String c1 = editTextC1.getText().toString();
                                String c2 = editTextC2.getText().toString();

                                if (c1 != null && c2 != null) {
                                    if (!c1.isEmpty() && !c2.isEmpty()) {
                                        boolean done = easyDB.updateData(1, c1)
                                                .updateData(2, c2)
                                                .rowID(Integer.valueOf(taskEditText.getText().toString()));

                                        Toast.makeText(MainActivity.this, "Updated: " + done, Toast.LENGTH_SHORT).show();
                                        showData();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Data")
                        .setMessage("Enter row number")
                        .setView(taskEditText)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean deleted = easyDB.deleteRow(Integer.valueOf(taskEditText.getText().toString()));
                                Toast.makeText(MainActivity.this, "Deleted: " + deleted, Toast.LENGTH_SHORT).show();
                                showData();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        buttonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c1 = editTextC1.getText().toString();
                String c2 = editTextC2.getText().toString();
                String cols[] = easyDB.getAllColumns();

                String columnsToMatch[] = new String[]{cols[1], cols[2]};
                String valuesToMatch[] = new String[]{c1, c2};

                boolean matched = easyDB.matchColumns(columnsToMatch, valuesToMatch);
                Toast.makeText(MainActivity.this, "Matched = " + matched, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showData() {
        String tres = "";
        Cursor res = easyDB.getAllData();
        //Cursor res = easyDB.getAllDataOrderedBy(0, false);

        while (res.moveToNext()) {
            String row = res.getString(0);
            String c1 = res.getString(1);
            String c2 = res.getString(2);
            tres += "Row: " + row + " C1 = " + c1 + " C2 = " + c2 + "\n";
        }
        textViewResult.setText(tres);
    }

    public void getData1(View view) {
        String tres = "";
        Cursor res = easyDB.searchInColumn(1, "1", -1);
        
        if (res != null) {
            while (res.moveToNext()) {
                String row = res.getString(0);
                String c1 = res.getString(1);
                String c2 = res.getString(2);
                tres += "Row: " + row + " C1 = " + c1 + " C2 = " + c2 + "\n";
            }

            textViewResult.setText(tres);

        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }
}
