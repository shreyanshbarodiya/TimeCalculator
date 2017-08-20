package shreyansh.learn.com.timecalculator.User;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelActivity;
import shreyansh.learn.com.timecalculator.R;
import shreyansh.learn.com.timecalculator.User.Adapters.UserAdapterActivities;

public class UserActivities extends AppCompatActivity {

    ListView lv;
    ArrayList<ModelActivity> modelActivityItems;
    UserAdapterActivities adapter;
    TextView totalTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities);
        lv = (ListView) findViewById(R.id.listView1);
        totalTimeTV = (TextView) findViewById(R.id.totalTimeTV);

        modelActivityItems = new ArrayList<>();

        SQLiteDatabase mydatabase = openOrCreateDatabase("TimeCalculatorDB", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS AdminActivity(ActivityName VARCHAR, TotalTime VARCHAR);");

        Cursor resultSet = mydatabase.rawQuery("Select * from AdminActivity",null);

        float mahaTotalTime = 0.0f;
        String name, totalTime;
        while(resultSet.moveToNext()){
            name = resultSet.getString(0);
            totalTime = resultSet.getString(1);
            mahaTotalTime = mahaTotalTime + Float.valueOf(totalTime);
            modelActivityItems.add(new ModelActivity(name, Float.valueOf(totalTime)));
        }


        adapter = new UserAdapterActivities(this, modelActivityItems);
        lv.setAdapter(adapter);

        totalTimeTV.setText(Float.toString(mahaTotalTime));
        resultSet.close();
        mydatabase.close();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
