package shreyansh.learn.com.timecalculator.Admin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;


public class AdminAdapterSubactivity extends ArrayAdapter<ModelSubactivity> {
    private ArrayList<ModelSubactivity> modelSubActivityItems = null;
    private Context context;

    public AdminAdapterSubactivity(Context context, ArrayList<ModelSubactivity> resource) {
        super(context, R.layout.adapter_admin_subactivities,resource);
        this.context = context;
        this.modelSubActivityItems = resource;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.adapter_admin_subactivities, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.name_activity);
        TextView labour = (TextView) convertView.findViewById(R.id.labour);
        TextView length = (TextView) convertView.findViewById(R.id.length);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView c1 = (TextView) convertView.findViewById(R.id.c1);
        TextView c2 = (TextView) convertView.findViewById(R.id.c2);

        ModelSubactivity currSubActivity = modelSubActivityItems.get(position);
        name.setText(currSubActivity.getName());
        labour.setText(Float.toString(currSubActivity.getLabour()));
        length.setText(Float.toString(currSubActivity.getLength()));
        time.setText(Float.toString(currSubActivity.getTime()));
        c1.setText(Float.toString(currSubActivity.getC1()));
        c2.setText(Float.toString(currSubActivity.getC2()));


        // also set the other details

        return convertView;
    }
}
