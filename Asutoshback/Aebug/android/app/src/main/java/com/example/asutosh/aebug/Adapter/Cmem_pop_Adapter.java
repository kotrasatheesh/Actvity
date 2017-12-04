package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asutosh.aebug.CreateTicket;
import com.example.asutosh.aebug.Edit_issue_details;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.member_assigned_bean;

import java.util.List;

import static com.example.asutosh.aebug.CreateTicket.Cdialog;
import static com.example.asutosh.aebug.CreateTicket.CmemberpopList;
import static com.example.asutosh.aebug.CreateTicket.CmermberList;
import static com.example.asutosh.aebug.Edit_issue_details.PREF_NAME;
import static com.example.asutosh.aebug.Edit_issue_details.dialog1;
import static com.example.asutosh.aebug.Edit_issue_details.memberId;
import static com.example.asutosh.aebug.Edit_issue_details.membername;
import static com.example.asutosh.aebug.Edit_issue_details.mempopList;

/**
 * Created by Asutosh on 12-09-2017.
 */

public class Cmem_pop_Adapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private Activity activity;
    private List<member_assigned_bean> tlist;


    public Cmem_pop_Adapter(Activity activity, List<member_assigned_bean> tlist,Context context) {
        this.activity = activity;
        this.tlist = tlist;
        this.mContext=context;
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
            convertView = inflater.inflate(R.layout.mem_pop_rowitem,null);
        TextView memberName = (TextView) convertView.findViewById(R.id.memberName);

        final member_assigned_bean m=tlist.get(position);
        memberName.setText(String.valueOf(m.getName()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false;
                if (CmermberList.size()==0){

                }else {
                    for (int i=0;i<CmermberList.size();i++){
                        if (m.getId().matches(CmermberList.get(i).getId())){
                            flag=true;
                        }
                    }
                }

                if (flag==true){
                    Toast.makeText(activity,"Member already exists",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences setting = activity.getSharedPreferences(
                            PREF_NAME, 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("POPNAME", String.valueOf(m.getName()));
                    editor.putString("POPID",String.valueOf(m.getId()));
                    editor.commit();
                    Cdialog.dismiss();
                    CmemberpopList.clear();
                    if(mContext instanceof CreateTicket){
                        ((CreateTicket)mContext).memFromAdapter();
                    }
                }
            }
        });



        return convertView;
    }
}
