package com.example.asutosh.aebug.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.asutosh.aebug.App_config.AppController;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.Areabean;

import static com.example.asutosh.aebug.App_config.AppController.addedArea;
import static com.example.asutosh.aebug.App_config.AppController.addedmem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asutosh on 29-08-2017.
 */

public class AreaAdapter extends ArrayAdapter<Areabean> {
    private Context mContext;
    private ArrayList<Areabean> listState;
    private AreaAdapter myAdapter;
    private boolean isFromView = false;

    public AreaAdapter(Context context, int resource, List<Areabean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<Areabean>) objects;
        for (int i=0;i<objects.size();i++){
            addedArea.add("hi");
        }
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;


        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input

      //  holder.mCheckBox.setChecked(listState.get(position).isSelected());


        if ((position == 0)) {
            holder.mTextView.setEnabled(false);
            holder.mCheckBox.setVisibility(View.INVISIBLE);

        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (isChecked){
                    isFromView = true;
                    addedArea.set(holder.ref, listState.get(getPosition).getTickt_id());
                    System.out.println("position  "+ addedArea.get(holder.ref));
                }else {
                    isFromView = false;
                    addedArea.set(holder.ref,"hi");
                    System.out.println("position1  "+ addedArea.get(holder.ref));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        int ref;
        private CheckBox mCheckBox;
    }
}
