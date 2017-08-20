package shreyansh.learn.com.timecalculator.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Adapters.AdminAdapterSubactivity;
import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;

public class AdminSubactivities extends AppCompatActivity {

    ArrayList<ModelSubactivity> subactivities;
    ListView listView;
    FloatingActionButton btnAddSubActivity;
    AdminAdapterSubactivity adapter;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subactivities);

        btnAddSubActivity = (FloatingActionButton) findViewById(R.id.btnAddSubActivity);
        listView = (ListView) findViewById(R.id.listView1);

        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS AdminSubActivity" +
                        "(SubActivityName VARCHAR," +
                        " AdminActivity VARCHAR," +
                        " labour VARCHAR," +
                        " length VARCHAR," +
                        " time VARCHAR ," +
                        " c1 VARCHAR, " +
                        " c2 VARCHAR, " +
                        " userLabour VARCHAR, " +
                        " userLength VARCHAR, " +
                        " checked INTEGER );");

        mydatabase.close();
        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        subactivities = getSubactivities(activity);


        adapter = new AdminAdapterSubactivity(AdminSubactivities.this, subactivities);
        listView.setAdapter(adapter);

        btnAddSubActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open an alert asking for activity name, length, labour and time
                // On filling details, calculate c1,c2 and save to the database and update the main list
                createDialogBox();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelSubactivity mSubactivity = (ModelSubactivity) adapterView.getItemAtPosition(i);
                createDialogBoxForEditingSubactivity(mSubactivity);
            }
        });
    }

    private void createDialogBoxForEditingSubactivity(final ModelSubactivity mSubactivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(mSubactivity.getName());
        builder.setMessage("Enter the subactivity details:");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText labour = new EditText(this);
        labour.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        labour.setHint("labour");
        layout.addView(labour);

        final EditText length = new EditText(this);
        length.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        length.setHint("length");
        layout.addView(length);

        final EditText time = new EditText(this);
        time.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        time.setHint("time");
        layout.addView(time);

        final CheckBox calcBothSides = new CheckBox(this);
        calcBothSides.setHint(" Recorded for both sides ");
        layout.addView(calcBothSides);

        builder.setView(layout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String labourString = labour.getText().toString();
                String lengthString = length.getText().toString();
                String timeString = time.getText().toString();
                Log.i("myTag", labourString + lengthString + timeString);
                // Call to save the value in the database and update the list
                mSubactivity.setLabour(Float.valueOf(labourString));
                mSubactivity.setLength(Float.valueOf(lengthString));
                mSubactivity.setTime(Float.valueOf(timeString));
                mSubactivity.setC1(Float.valueOf(labourString)*Float.valueOf(timeString) / Float.valueOf(lengthString));
                if(calcBothSides.isChecked()){
                    mSubactivity.setC2(Float.valueOf(labourString)*Float.valueOf(timeString) / Float.valueOf(lengthString));
                }else{
                    mSubactivity.setC2(2*Float.valueOf(labourString)*Float.valueOf(timeString) / Float.valueOf(lengthString));
                }
                editSubactivityInDb(mSubactivity);
                updateList();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSubactivity(mSubactivity);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        Button b = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        if(b != null) {
            b.setBackgroundColor(Color.RED);
            b.setTextColor(Color.YELLOW);
        }
    }

    private void deleteSubactivity(ModelSubactivity mSubactivity) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "DELETE FROM AdminSubActivity " +
                        "WHERE SubActivityName = '" + mSubactivity.getName() +"' " +
                        "AND AdminActivity = '" + activity + "'" +
                        "; ");

        if(mSubactivity.getValue()==1 && mSubactivity.getUserLabour()!=0){
            float newTotalTime = getIntent().getFloatExtra("totalTime", 0.0f) - (mSubactivity.getC2()*mSubactivity.getUserLength()/mSubactivity.getUserLabour());
            mydatabase.execSQL("UPDATE AdminActivity " +
                                    "SET " +
                                    "TotalTime = '" + newTotalTime + "' " +
                                    "WHERE ActivityName = '" + activity + "';");
        }
        updateList();
        mydatabase.close();
    }

    private void editSubactivityInDb(ModelSubactivity mSubactivity) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "UPDATE AdminSubActivity" +
                        " SET " +
                            "labour = '" + mSubactivity.getLabour() + "', " +
                            "length = '" + mSubactivity.getLength() + "', " +
                            "time = '" + mSubactivity.getTime() + "', " +
                            "c1 = '" + mSubactivity.getC1() + "', " +
                            "c2 = '" + mSubactivity.getC2() + "' " +
                            "WHERE SubActivityName = '" + mSubactivity.getName() +"' " +
                            "AND AdminActivity = '" + activity + "'" +
                            "; ");
    }

    private ArrayList<ModelSubactivity> getSubactivities(String activity) {
        // populate from SQLite database
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);

        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM AdminSubActivity WHERE AdminActivity='" + activity + "';",null);
        ArrayList<ModelSubactivity> modelSubactivities = new ArrayList<>();
        String name, labour, length, time, c1, c2, userLabour, userLength;

        while(resultSet.moveToNext()){
            name = resultSet.getString(0);
            labour = resultSet.getString(2);
            length = resultSet.getString(3);
            time = resultSet.getString(4);
            c1 = resultSet.getString(5);
            c2 = resultSet.getString(6);
            userLabour = resultSet.getString(7);
            userLength = resultSet.getString(8);
            int checked = resultSet.getInt(9);
            //get other details as well
            modelSubactivities.add(new ModelSubactivity(name, checked, Float.valueOf(labour), Float.valueOf(time),
                    Float.valueOf(length), Float.valueOf(c1), Float.valueOf(c2), activity,
                    Float.valueOf(userLabour), Float.valueOf(userLength)));
        }

        resultSet.close();
        mydatabase.close();
        return modelSubactivities;
    }

    private void createDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subactivity name");
        builder.setMessage("Enter the subactivity name:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                // Call to save the value in the database and update the list
                saveNewSubActivity(value,"0", "0", "0", "0", "0");
                updateList();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void saveNewSubActivity(String SubAcName, String labour, String length, String time, String c1, String c2) {
        // save the new activity in the database
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "INSERT INTO AdminSubActivity VALUES" +
                        "('"+ SubAcName.trim() + "'," +
                        "'" + activity +"'," +
                        "'" + labour +"'," +
                        "'" + length +"'," +
                        "'" + time +"'," +
                        "'" + c1 +"'," +
                        "'" + c2 +"'," +
                        "'0'," +
                        "'0', 0 );");
    }

    private void updateList() {
        //adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }



}
