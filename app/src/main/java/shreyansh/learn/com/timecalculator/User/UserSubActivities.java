package shreyansh.learn.com.timecalculator.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;
import shreyansh.learn.com.timecalculator.User.Adapters.UserAdapterSubactivity;

public class UserSubActivities extends AppCompatActivity {

    ArrayList<ModelSubactivity> subactivities;
    ListView listView;
    UserAdapterSubactivity adapter;
    String activity;
    Button saveNCalculate;
    TextView totalTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sub_activities);

        listView = (ListView) findViewById(R.id.listView1);
        saveNCalculate = (Button) findViewById(R.id.saveButton);
        totalTimeTV = (TextView) findViewById(R.id.totalTimeTV);

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
        totalTimeTV.setText(Float.toString(intent.getFloatExtra("totalTime",0.0f)));
        subactivities = getSubactivities(activity);


        adapter = new UserAdapterSubactivity(UserSubActivities.this, subactivities);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelSubactivity mSubactivity = (ModelSubactivity) adapterView.getItemAtPosition(i);
                createDialogBox(mSubactivity);
            }
        });

        saveNCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            calculateTotalTime();
            }
        });
    }

    private void updateTotalTimeOfActivityInDB(String activity, Float totalTime) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "UPDATE AdminActivity" +
                        " SET " +
                        "TotalTime = '" + totalTime + "' " +
                        "WHERE ActivityName = '" + activity + "'" +
                        "; ");
        mydatabase.close();
    }

    private void saveCheckBoxDetail(ModelSubactivity modelSubactivity, int i) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "UPDATE AdminSubActivity" +
                        " SET " +
                        "checked = '" + i + "' " +
                        "WHERE SubActivityName = '" + modelSubactivity.getName() +"' " +
                        "AND AdminActivity = '" + activity + "'" +
                        "; ");
        mydatabase.close();
    }

    private ArrayList<ModelSubactivity> getSubactivities(String activity) {
        // populate from SQLite database
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);

        Cursor resultSet = mydatabase.rawQuery("SELECT * FROM AdminSubActivity WHERE AdminActivity='" + activity + "';",null);
        ArrayList<ModelSubactivity> modelSubactivities = new ArrayList<>();
        String name, labour, length, time, c1, c2, userLabour, userLength;
        int checked;
        while(resultSet.moveToNext()){
            name = resultSet.getString(0);
            labour = resultSet.getString(2);
            length = resultSet.getString(3);
            time = resultSet.getString(4);
            c1 = resultSet.getString(5);
            c2 = resultSet.getString(6);
            userLabour = resultSet.getString(7);
            userLength = resultSet.getString(8);
            checked = resultSet.getInt(9);

            modelSubactivities.add(new ModelSubactivity(name, checked, Float.valueOf(labour), Float.valueOf(time),
                    Float.valueOf(length), Float.valueOf(c1), Float.valueOf(c2), activity,
                    Float.valueOf(userLabour), Float.valueOf(userLength)));
        }

        resultSet.close();
        mydatabase.close();
        return modelSubactivities;
    }

    private void createDialogBox(final ModelSubactivity modelSubactivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subactivity");
        builder.setMessage("Enter length and labour:");

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

        builder.setView(layout);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String labourString = labour.getText().toString();
                String lengthString = length.getText().toString();
                editUserDetailsInDb(labourString, lengthString, modelSubactivity);
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

    private void editUserDetailsInDb(String labourString, String lengthString, ModelSubactivity modelSubactivity) {
        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL(
                "UPDATE AdminSubActivity" +
                        " SET " +
                        "userLabour = '" + labourString + "', " +
                        "userLength = '" + lengthString + "' " +
                        "WHERE SubActivityName = '" + modelSubactivity.getName() +"' " +
                        "AND AdminActivity = '" + activity + "'" +
                        "; ");
        mydatabase.close();
    }

    private void updateList() {
        //adapter.notifyDataSetChanged();
        finish();
        startActivity(getIntent());
    }

    private void calculateTotalTime(){
        Float totalTime = 0.0f;
        for(int i=0; i<adapter.getCount(); i++){
            ModelSubactivity modelSubactivity = adapter.getItem(i);
            CheckBox cb = (CheckBox) listView.getChildAt(i).findViewById(R.id.checkBox);
            if(cb.isChecked()){
                if(modelSubactivity.getUserLabour()!=0){
                    totalTime = totalTime + modelSubactivity.getC2()*modelSubactivity.getUserLength()/modelSubactivity.getUserLabour();
                }
                saveCheckBoxDetail(modelSubactivity, 1);
            }else{
                saveCheckBoxDetail(modelSubactivity, 0);

            }
        }
        totalTimeTV.setText(Float.toString(totalTime));
        updateTotalTimeOfActivityInDB(activity, totalTime);

    }
}
