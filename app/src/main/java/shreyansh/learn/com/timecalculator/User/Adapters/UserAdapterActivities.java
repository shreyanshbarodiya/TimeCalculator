package shreyansh.learn.com.timecalculator.User.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelActivity;
import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;
import shreyansh.learn.com.timecalculator.User.UserSubActivities;

public class UserAdapterActivities extends ArrayAdapter<ModelActivity> {

    private ArrayList<ModelActivity> modelActivityItems = null;
    private Context context;
    public UserAdapterActivities(Context context, ArrayList<ModelActivity> resource) {
        super(context, R.layout.adapter_user_activities,resource);
        this.context = context;
        this.modelActivityItems = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.adapter_user_activities, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.name_activity);
        TextView totalTime = (TextView) convertView.findViewById(R.id.total_time);
        Button btnSubactivities = (Button) convertView.findViewById(R.id.btnSubactivities);

        name.setText(modelActivityItems.get(position).getName());
        totalTime.setText(String.valueOf(modelActivityItems.get(position).getTotalTime()));


        btnSubactivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserSubActivities.class);
                intent.putExtra("activity", modelActivityItems.get(position).getName());
                intent.putExtra("totalTime", modelActivityItems.get(position).getTotalTime());
                context.startActivity(intent);
            }
        });

        return convertView;
    }


}
