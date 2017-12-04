package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.IssueListActivity;
import com.example.asutosh.aebug.Issue_DetailsActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.IssueListBean;

import java.util.ArrayList;
import java.util.List;

import static com.example.asutosh.aebug.App_config.AppController.TeamId;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;

/**
 * Created by Asutosh on 24-08-2017.
 */

public class issue_llist_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<IssueListBean> mItemList;
    private List<IssueListBean> issuefilterList;
    public static final String ISSUEtICKET = "myTicket";
    Context context;
    String rollid;
    Activity activity;
    public static ProgressBar progre;
    private static final int TYPE_ITEM1 = 1;
    private static final int TYPE_FOOTER1 = 2;
    private ClickListener clicklistener = null;
    View view;

    public issue_llist_Adapter(Context context,Activity activity,List<IssueListBean> itemList,String rollid) {
        mItemList = itemList;
        this.issuefilterList = new ArrayList<IssueListBean>();
        this.context=context;
        this.rollid=rollid;
        this.activity=activity;
        this.issuefilterList.addAll(this.mItemList);

    }
    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER1;
        }
        return TYPE_ITEM1;
    }
    public class issule_list_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView issueTitle;
        private final TextView ticketID;
        private final TextView status;
        private final TextView assing;
        private final TextView priority;
        private final TextView cteateddate;
        private final TextView environment;
        private final TextView modifedDate;
        private final TextView modifiedTag;
        private final RelativeLayout listRelt;


        public issule_list_Viewholder(View view) {
            super(view);
            issueTitle = (TextView) view.findViewById(R.id.issueTitle);
            ticketID = (TextView) view.findViewById(R.id.ticketId);
            status = (TextView) view.findViewById(R.id.status);
            assing = (TextView) view.findViewById(R.id.assing);
            priority = (TextView) view.findViewById(R.id.priority);
            cteateddate = (TextView) view.findViewById(R.id.cteateddate);
            environment = (TextView) view.findViewById(R.id.environment);
            listRelt = (RelativeLayout) view.findViewById(R.id.listRelt);
            modifedDate = (TextView) view.findViewById(R.id.modifedDate);
            modifiedTag = (TextView) view.findViewById(R.id.modifiedTag);

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
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View View;


        public FooterViewHolder(View v) {
            super(v);
            View = v;
            progre = (ProgressBar) View.findViewById(R.id.pro);
            // Add your UI Components here
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progre.setVisibility(android.view.View.GONE);
                }
            }, 1000);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.issulelist_rowitem,parent, false);
        return new issule_list_Viewholder(v);*/

        if (viewType == TYPE_ITEM1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issulelist_rowitem, parent, false);
            return new issule_list_Viewholder(view);

        }  else if (viewType == TYPE_FOOTER1) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout,
                    parent, false);
            return new FooterViewHolder(view);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof issule_list_Viewholder) {

            issule_list_Viewholder viewHolder = (issule_list_Viewholder) holder;
            final IssueListBean listItem = issuefilterList.get(position);
            final String ticketid=listItem.getTicketId();
            final String assigneeID=listItem.getAssigneeID();
            final String enterdBy=listItem.getEnterdBy();


//            IssueListBean c = mItemList.get(position);
            viewHolder.issueTitle.setText(listItem.getIssueTitle());
            viewHolder.cteateddate.setText(listItem.getCreatedDate());
            viewHolder.ticketID.setText(listItem.getTicketId());
            viewHolder.assing.setText(listItem.getAssignedName());
            viewHolder.status.setText(listItem.getStatus());
            viewHolder.priority.setText(listItem.getPriority());
            viewHolder.environment.setText(listItem.getEnvironment());

            viewHolder.listRelt.setTag(position);

            String modify=listItem.getModifiedDate();
            if (modify.isEmpty()){
                viewHolder.modifedDate.setVisibility(View.GONE);
                viewHolder.modifiedTag.setVisibility(View.GONE);
            }else {
                viewHolder.modifedDate.setText(listItem.getModifiedDate());
                viewHolder.modifedDate.setVisibility(View.VISIBLE);
                viewHolder.modifiedTag.setVisibility(View.VISIBLE);
            }
            String priorityflag= listItem.getPriority();
            String statusflag= listItem.getStatus();
            String environmentflag= listItem.getEnvironment();
            if (priorityflag.equalsIgnoreCase("Urgent")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.priority.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.urgent));
                viewHolder.priority.setTextColor(Color.WHITE);
            }else if (priorityflag.equalsIgnoreCase("high")){

                GradientDrawable bgShape = (GradientDrawable) viewHolder.priority.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.high));
                viewHolder.priority.setTextColor(Color.WHITE);

            }else if (priorityflag.equalsIgnoreCase("low")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.priority.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.low));
                viewHolder.priority.setTextColor(Color.WHITE);
            }else if (priorityflag.equalsIgnoreCase("medium")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.priority.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.medium));
                viewHolder.priority.setTextColor(Color.WHITE);
            }else {
                viewHolder.priority.setBackgroundColor(view.getContext().getResources().getColor(R.color.medium));
            }
            if (statusflag.equalsIgnoreCase("pending")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.i_pending));
                viewHolder.status.setTextColor(Color.WHITE);
            }else if (statusflag.equalsIgnoreCase("started")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.i_started));
                viewHolder.status.setTextColor(Color.WHITE);
            }else if (statusflag.equalsIgnoreCase("re-opened")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.i_reopend));
                viewHolder.status.setTextColor(Color.WHITE);
            }else if (statusflag.equalsIgnoreCase("closed")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.closed));
                viewHolder.status.setTextColor(Color.WHITE);
            }else if (statusflag.equalsIgnoreCase("fixed")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.fixed));
                viewHolder.status.setTextColor(Color.WHITE);
            }else if (statusflag.equalsIgnoreCase("deferred")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.status.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.deferred));
                viewHolder.status.setTextColor(Color.WHITE);
            }
            else {
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.input));
            }
            if (environmentflag.equalsIgnoreCase("Development")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.environment.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.developement));
                viewHolder.environment.setTextColor(Color.WHITE);
            }else if (environmentflag.equalsIgnoreCase("Production")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.environment.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.production));
                viewHolder.environment.setTextColor(Color.WHITE);
            }else if (environmentflag.equalsIgnoreCase("User Acceptance")){
                GradientDrawable bgShape = (GradientDrawable) viewHolder.environment.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.acceptance));
                viewHolder.environment.setTextColor(Color.WHITE);
            }else {
                GradientDrawable bgShape = (GradientDrawable) viewHolder.environment.getBackground();
                bgShape.setColor(context.getResources().getColor(R.color.developement));
                viewHolder.environment.setTextColor(Color.WHITE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent (v.getContext(), Issue_DetailsActivity.class);
                    SharedPreferences settings =v.getContext().getSharedPreferences(ISSUEtICKET, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("TICKETID", ticketid);
                    editor.commit();
                    v.getContext().startActivity(intent);
                }
            });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        boolean candlt = false;
                        final String[]enterbyId=listItem.getAssigneeID().split(",");
                        for (int i=0;i<enterbyId.length;i++){
                            if (enterbyId[i].matches(TeamId)){
                                candlt =true;
                            }
                        }
                        if (rollid.equals("1")||enterdBy.equals(loginuserid)|| candlt ==true) {
                            final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                            alertbox.setMessage("Are you sure , You want to delete the Ticket !");
                            alertbox.setPositiveButton("Delete",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            // int pos = (int)v.getTag();

                                            Boolean checkinternet2= checkConnection();
                                            if (checkinternet2==true){
                                                // int pos = (int)v.getTag();
                                                issuefilterList.remove(position);
                                                if (context instanceof IssueListActivity) {
                                                    ((IssueListActivity) context).cllDeleteIssue(listItem.getTicketId().toString(), issuefilterList.size());
                                                }
                                                issue_llist_Adapter.this.notifyDataSetChanged();
                                                arg0.cancel();
                                            }else {
                                                Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.conatiner), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

                                                View sbView = snackbar.getView();
                                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                                textView.setTextColor(Color.RED);
                                                snackbar.show();
                                            }




                                        }
                                    });
                            alertbox.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0,
                                                            int arg1) {
                                            arg0.cancel();
                                        }
                                    });
                            alertbox.show();
                        }



                        return false;
                    }
                });

        }else if (holder instanceof FooterViewHolder) {

            //your code here
        }
    }
    private boolean checkConnection() {
        boolean checkintr=true;
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected==true){
            checkintr=true;
            showSnack(isConnected);
        }else {
            checkintr=false;
            showSnack(isConnected);
        }

        return checkintr;
    }

    // Showing the status in Snackbar

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(activity.findViewById(R.id.conatiner), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
    public void Ifilter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                issuefilterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (text.equalsIgnoreCase("clear")) {

                    issuefilterList.addAll(mItemList);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (IssueListBean item : mItemList) {
                        if (item.getPriority().toLowerCase().contains(text.toLowerCase())||
                                item.getStatus().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            issuefilterList.add(item);
                        }
                    }
                }
                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }


    @Override
    public int getItemCount() {
        if (issuefilterList == null || issuefilterList.size() == 0) {
            return 0;
        }
        return issuefilterList.size() + 1;
    }

}