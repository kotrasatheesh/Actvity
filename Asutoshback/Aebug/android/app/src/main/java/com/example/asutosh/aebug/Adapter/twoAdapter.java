package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.todaysbean;
import java.util.List;

/**
 * Created by Asutosh on 23-08-2017.
 */

public class twoAdapter  extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    private List<todaysbean> appoointmentList ;

    public twoAdapter(Activity activity, List<todaysbean> appoointmentList) {
        this.activity = activity;
        this.appoointmentList = appoointmentList;
    }

    @Override
    public int getCount() {
        return appoointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appoointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.appointment_row_items,null);
        TextView custnm = (TextView) convertView.findViewById(R.id.nameshow);

        todaysbean m=appoointmentList.get(position);
        final String WOid=m.getName();

        custnm.setText(WOid);


        return convertView;
    }
}
