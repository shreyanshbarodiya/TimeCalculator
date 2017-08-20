package shreyansh.learn.com.timecalculator.Admin;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Adapters.AdminAdapterActivities;
import shreyansh.learn.com.timecalculator.Admin.Models.ModelActivity;
import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;

public class AdminActivities extends AppCompatActivity {

    ListView lv;
    ArrayList<ModelActivity> modelActivityItems;
    FloatingActionButton btnAddActivity;
    AdminAdapterActivities adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activities);

        lv = (ListView) findViewById(R.id.listView1);
        btnAddActivity = (FloatingActionButton) findViewById(R.id.btnAddActivity);

        modelActivityItems = new ArrayList<>();

        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS AdminActivity(ActivityName VARCHAR, TotalTime VARCHAR);");
//        mydatabase.execSQL("INSERT INTO AdminActivity VALUES('test');");

        Cursor resultSet = mydatabase.rawQuery("Select * from AdminActivity",null);

        String name, totalTime;
        while(resultSet.moveToNext()){
            name = resultSet.getString(0);
            totalTime = resultSet.getString(1);
            modelActivityItems.add(new ModelActivity(name, Float.valueOf(totalTime)));
        }

        adapter = new AdminAdapterActivities(this, modelActivityItems);
        lv.setAdapter(adapter);

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open an alert asking for activity name
                // On filling name, save to the database and update the main list
                createDialogBox();
        }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelActivity mActivity = (ModelActivity) adapterView.getItemAtPosition(i);
                createDialogBoxToDelete(mActivity);
            }
        });
        resultSet.close();
    }

    private void createDialogBoxToDelete(final ModelActivity mActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Activity " + mActivity.getName());
        builder.setMessage("Delete activity: " + mActivity.getName() +  " ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteActivity(mActivity);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void deleteActivity(ModelActivity mActivity) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "DELETE FROM AdminActivity " +
                        "WHERE ActivityName = '" + mActivity.getName() +"' " +
                        "; ");

        mydatabase.execSQL(
                "DELETE FROM AdminSubActivity " +
                        "WHERE AdminActivity = '" + mActivity.getName() +"' " +
                        "; ");
        updateList();
        mydatabase.close();

    }

    private void createDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Activity name");
        builder.setMessage("Enter the activity name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Call to save the value in the database and update the list
                saveNewActivity(value);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
//        AlertDialog dialog = builder.create();
    }

    private void saveNewActivity(String value) {
        // save the new activity in the database
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL("INSERT INTO AdminActivity VALUES('"+ value.trim() + "', '0');");
        updateList();
    }

    private void updateList() {
        //update the list in the main page
//        adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }
}
