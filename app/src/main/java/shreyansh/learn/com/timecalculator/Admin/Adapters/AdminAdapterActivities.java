package shreyansh.learn.com.timecalculator.Admin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelActivity;
import shreyansh.learn.com.timecalculator.R;
import shreyansh.learn.com.timecalculator.Admin.AdminSubactivities;

public class AdminAdapterActivities extends ArrayAdapter<ModelActivity> {

    private ArrayList<ModelActivity> modelActivityItems = null;
    private Context context;
    public AdminAdapterActivities(Context context, ArrayList<ModelActivity> resource) {
        super(context, R.layout.adapter_admin_activities,resource);
        this.context = context;
        this.modelActivityItems = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.adapter_admin_activities, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.name_activity);
        Button btnSubactivities = (Button) convertView.findViewById(R.id.btnSubactivities);

        name.setText(modelActivityItems.get(position).getName());

        btnSubactivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminSubactivities.class);
                intent.putExtra("activity", modelActivityItems.get(position).getName());
                intent.putExtra("totalTime", modelActivityItems.get(position).getTotalTime());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
