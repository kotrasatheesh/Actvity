package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.ActivityBean;
import com.example.asutosh.aebug.bean.projectbean;
import com.example.asutosh.aebug.fragments.home_fragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asutosh on 15-09-2017.
 */

public class notification_recycler_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ClickListener clicklistener = null;
    List<ActivityBean> Alist;
    public static final String PROJECT_NAME = "PRO" ;
    private Activity activity;
    private String rollid="";
    private static final int TYPE_ITEM2 = 1;
    private static final int TYPE_FOOTER2 = 2;
    public static ProgressBar progresss;

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER2;
        }
        return TYPE_ITEM2;
    }

    /**
     * View holder class
     * */

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView by_name;
        public TextView projectName;
        public TextView Description;
        public TextView When;

        public MyViewHolder(View view) {
            super(view);
            by_name = (TextView) view.findViewById(R.id.by_name);
            projectName = (TextView) view.findViewById(R.id.projectName);
            Description = (TextView) view.findViewById(R.id.Description);
            When = (TextView) view.findViewById(R.id.When);
        }

        @Override
        public void onClick(View v) {
            if (clicklistener != null) {
                clicklistener.itemClicked(v, getAdapterPosition());
            }
        }
    }
    public void setClickListener(ClickListener clicklistener) {
        this.clicklistener = clicklistener;
    }
    public notification_recycler_Adapter(Activity activity, List<ActivityBean> Alist) {
        this.activity=activity;
        this.Alist=Alist;
    }

    @Override
    public int getItemCount() {
        if (Alist == null || Alist.size() == 0) {
            return 0;
        }
        return Alist.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false);

        if (viewType == TYPE_ITEM2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_item, parent, false);
            return new MyViewHolder(view);

        }  else if (viewType == TYPE_FOOTER2) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout,
                    parent, false);
            return new FooterViewHolder(view);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;;
            final ActivityBean m = Alist.get(position);
            viewHolder.by_name.setText(String.valueOf(m.getActivty_by()));
            viewHolder.projectName.setText(String.valueOf(m.getProj_name()));
            viewHolder.Description.setText(String.valueOf(m.getDescription()));
            viewHolder.When.setText(String.valueOf(m.getTime()));
        }else if (holder instanceof FooterViewHolder) {

            //your code here
        }


    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View View;


        public FooterViewHolder(View v) {
            super(v);
            View = v;
            progresss = (ProgressBar) View.findViewById(R.id.pro);
            // Add your UI Components here
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progresss.setVisibility(android.view.View.GONE);
                }
            }, 1000);
        }
    }

}