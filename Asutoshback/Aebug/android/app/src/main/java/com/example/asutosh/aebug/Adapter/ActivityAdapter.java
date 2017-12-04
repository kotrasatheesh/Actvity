package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.asutosh.aebug.Issue_DetailsActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.ActivityBean;
import com.example.asutosh.aebug.bean.commentBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.asutosh.aebug.Issue_DetailsActivity.CommnetListV;

/**
 * Created by Asutosh on 13-09-2017.
 */

public class ActivityAdapter extends BaseAdapter {
    List<ActivityBean> Alist;
    private Activity activity;
    private LayoutInflater inflater;


    public ActivityAdapter(Activity activity, List<ActivityBean> Alist){
        this.activity = activity;
        this.Alist=Alist;
    }

    @Override
    public int getCount() {
        return Alist.size();
    }

    @Override
    public Object getItem(int position) {
        return Alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.notification_row_item, null);
            holder.rltv_item = (RelativeLayout) convertView.findViewById(R.id.relt);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.ref = position;
        TextView by_name = (TextView) convertView.findViewById(R.id.by_name);
        TextView projectName = (TextView) convertView.findViewById(R.id.projectName);
        TextView Description = (TextView) convertView.findViewById(R.id.Description);
        TextView When = (TextView) convertView.findViewById(R.id.When);

        final ActivityBean m = Alist.get(position);
        by_name.setText(String.valueOf(m.getActivty_by()));
        projectName.setText(String.valueOf(m.getProj_name()));
        Description.setText(String.valueOf(m.getDescription()));
        When.setText(String.valueOf(m.getTime()));

        return convertView;
    }
    private class ViewHolder {
        RelativeLayout rltv_item;
        EditText editText1;
        int ref;
        String id;
    }
}
