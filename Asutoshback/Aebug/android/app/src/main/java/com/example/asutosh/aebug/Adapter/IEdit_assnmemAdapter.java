package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.member_assigned_bean;

import java.util.List;

import static com.example.asutosh.aebug.Edit_issue_details.memberlistview;

/**
 * Created by Asutosh on 07-09-2017.
 */

public class IEdit_assnmemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Activity activity;
    private List<member_assigned_bean> tlist;


    public IEdit_assnmemAdapter(Activity activity, List<member_assigned_bean> tlist) {
        this.activity = activity;
        this.tlist = tlist;
    }

    @Override
    public int getCount()
    {
        return tlist.size();
    }

    @Override
    public Object getItem(int position) {
        return tlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.assign_mem_row_item,null);
        TextView memberName = (TextView) convertView.findViewById(R.id.memberName);
        ImageView delete=(ImageView) convertView.findViewById(R.id.delete);

        final member_assigned_bean m=tlist.get(position);
        memberName.setText(String.valueOf(m.getName()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(activity,"name"+m.getName()+"id"+m.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        delete.setTag(position);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                tlist.remove(pos);
                setListViewHeightBasedOnChildren(memberlistview);
                IEdit_assnmemAdapter.this.notifyDataSetChanged();
            }
        });

        return convertView;
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
