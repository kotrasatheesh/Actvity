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

import com.example.asutosh.aebug.Edit_issue_details;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.Areabean;
import com.example.asutosh.aebug.bean.member_assigned_bean;

import java.util.List;

import static com.example.asutosh.aebug.Edit_issue_details.PREF_NAME;
import static com.example.asutosh.aebug.Edit_issue_details.areaList;
import static com.example.asutosh.aebug.Edit_issue_details.areapopList;
import static com.example.asutosh.aebug.Edit_issue_details.dialog1;
import static com.example.asutosh.aebug.Edit_issue_details.dialog2;
import static com.example.asutosh.aebug.Edit_issue_details.memberId;
import static com.example.asutosh.aebug.Edit_issue_details.membername;
import static com.example.asutosh.aebug.Edit_issue_details.mempopList;

/**
 * Created by Asutosh on 07-09-2017.
 */

public class AreaPop_Adapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private Activity activity;
    private List<Areabean> tlist;
    public static final String AREAPOPPRE = "Apop";


    public AreaPop_Adapter(Activity activity, List<Areabean> tlist,Context context) {
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

        final Areabean m=tlist.get(position);
        memberName.setText(String.valueOf(m.getTitle()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false;
                if (areaList.size()==0){
                    //do nothging
                }else {
                    for (int i=0;i<areaList.size();i++){
                        if (m.getTickt_id().matches(areaList.get(i).getTickt_id().toString())){
                            flag=true;
                        }
                    }
                }
                if (flag==true){
                    Toast.makeText(activity,"Area already exists",Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences setting = activity.getSharedPreferences(AREAPOPPRE, 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("POPTITLE", String.valueOf(m.getTitle()));
                    editor.putString("POPTICKETID",String.valueOf(m.getTickt_id()));
                    editor.commit();
                    dialog2.dismiss();
                    areapopList.clear();
                    if(mContext instanceof Edit_issue_details){
                        ((Edit_issue_details)mContext).areaFromAdapter();
                    }
                }


            }
        });



        return convertView;
    }
}
