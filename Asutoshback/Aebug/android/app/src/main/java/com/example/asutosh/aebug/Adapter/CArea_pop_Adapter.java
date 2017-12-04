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
import com.example.asutosh.aebug.bean.Areabean;

import java.util.List;

import static com.example.asutosh.aebug.CreateTicket.CareaList;
import static com.example.asutosh.aebug.CreateTicket.CareapopList;
import static com.example.asutosh.aebug.CreateTicket.Cdialog2;
import static com.example.asutosh.aebug.Edit_issue_details.dialog2;

/**
 * Created by Asutosh on 12-09-2017.
 */

public class CArea_pop_Adapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private Activity activity;
    private List<Areabean> tlist;
    public static final String AREAPOPPRE = "Apop";


    public CArea_pop_Adapter(Activity activity, List<Areabean> tlist,Context context) {
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
                if (CareaList.size()==0){
                    //do nothing
                }else {
                    for (int i=0;i<CareaList.size();i++){
                        if (m.getTickt_id().matches(CareaList.get(i).getTickt_id().toString())){
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
                    CareapopList.clear();
                    Cdialog2.dismiss();
                    if(mContext instanceof CreateTicket){
                        ((CreateTicket)mContext).areaFromAdapter();
                    }
                }
            }
        });



        return convertView;
    }
}
