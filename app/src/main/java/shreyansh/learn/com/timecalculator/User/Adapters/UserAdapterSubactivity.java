package shreyansh.learn.com.timecalculator.User.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import shreyansh.learn.com.timecalculator.Admin.Models.ModelSubactivity;
import shreyansh.learn.com.timecalculator.R;


public class UserAdapterSubactivity extends ArrayAdapter<ModelSubactivity> {
    private ArrayList<ModelSubactivity> modelSubActivityItems = null;
    private Context context;

    public UserAdapterSubactivity(Context context, ArrayList<ModelSubactivity> resource) {
        super(context, R.layout.adapter_user_subactivities,resource);
        this.context = context;
        this.modelSubActivityItems = resource;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.adapter_user_subactivities, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.name_activity);
        TextView labour = (TextView) convertView.findViewById(R.id.labour);
        TextView length = (TextView) convertView.findViewById(R.id.length);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        convertView.setTag(cb);

/*
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view.getTag();
                cb.set
            }
        });
*/

        ModelSubactivity currSubActivity = modelSubActivityItems.get(position);
        if(currSubActivity.getValue()==1){
            cb.setChecked(true);
        }

        name.setText(currSubActivity.getName());
        labour.setText(Float.toString(currSubActivity.getUserLabour()));
        length.setText(Float.toString(currSubActivity.getUserLength()));
        float userTime;
        if(Float.valueOf(currSubActivity.getUserLabour())!=0.0f){
            userTime = Float.valueOf(currSubActivity.getC2())*Float.valueOf(currSubActivity.getUserLength())/Float.valueOf(currSubActivity.getUserLabour());
            time.setText(Float.toString(userTime));
        }else{
            time.setText("-");
        }

        return convertView;
    }

}
