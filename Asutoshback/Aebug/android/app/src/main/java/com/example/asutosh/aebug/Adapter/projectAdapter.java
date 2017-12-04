package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.IssueListActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.projectbean;
import com.example.asutosh.aebug.fragments.home_fragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.asutosh.aebug.Issue_DetailsActivity.CommnetListV;

/**
 * Created by Asutosh on 23-08-2017.
 */

public class projectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ClickListener clicklistener = null;
    private List<projectbean> projectlist;
    private List<projectbean> filterList;
    public static final String PROJECT_NAME = "PRO" ;
    private Activity activity;
    private String rollid="";
    private Context context;
    private home_fragment fragment;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public static ProgressBar prog;

    @Override
    public int getItemViewType(int position) {
       if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    /**
     * View holder class
     * */

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView projectname;
        public TextView memberCount;
        public TextView firstname;
        public TextView lastname;
        public TextView ownerName;
        public TextView issueCount;
        public RelativeLayout rountInitial;

        public MyViewHolder(View view) {
            super(view);
            projectname = (TextView) view.findViewById(R.id.proj_name);
            memberCount = (TextView) view.findViewById(R.id.members);
            firstname = (TextView) view.findViewById(R.id.fn);
            ownerName = (TextView) view.findViewById(R.id.ownername);
            issueCount = (TextView) view.findViewById(R.id.issues);
            rountInitial = (RelativeLayout) view.findViewById(R.id.proNameLay);
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
    public projectAdapter(home_fragment fragment,Activity activity, Context context, List<projectbean> projectlist, String rollid) {
        this.rollid=rollid;
        this.fragment=fragment;
        this.projectlist = projectlist;
        this.activity=activity;
        this.filterList = new ArrayList<projectbean>();
        this.context=context;
        this.filterList.addAll(this.projectlist);
    }
    @Override
    public int getItemCount() {
        if (filterList == null || filterList.size() == 0) {
            return 0;
        }
        return filterList.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent, false);
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new MyViewHolder(view);

        }  else if (viewType == TYPE_FOOTER) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout,
                    parent, false);
            return new FooterViewHolder(view);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


         if (holder instanceof MyViewHolder) {
             MyViewHolder viewHolder = (MyViewHolder) holder;
             final projectbean listItem = filterList.get(position);
//             projectbean c = projectlist.get(position);
             String data=listItem.getName();
             final String projectId=listItem.getProjectId();
             final String projectname=listItem.getName();
             final String ticketCount=listItem.getTicketCount();
             char ch=data.charAt(0);
             viewHolder.firstname.setText(String.valueOf(ch));
             viewHolder.projectname.setText(listItem.getName());
             viewHolder.memberCount.setText(String.valueOf(listItem.getTeamCount()));
             viewHolder.ownerName.setText(String.valueOf(listItem.getOnwerName()));
             viewHolder.issueCount.setText(String.valueOf(listItem.getTicketCount()));
             if (rollid.equals("1")){
                 holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View v) {
                         final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());

                         if (listItem.getTicketCount().matches("0")){
                             alertbox.setMessage("Are you sure , You want to delete the Project ?");
                         }else if (listItem.getTicketCount().matches("1")){
                             alertbox.setMessage("The Project is associated with "+listItem.getTicketCount()+" Ticket.Are you sure you want to Delete the Project?");
                         }else {
                             alertbox.setMessage("The Project is associated with "+listItem.getTicketCount()+" Tickets.Are you sure you want to Delete the Project?");
                         }

                         alertbox.setPositiveButton("Delete",
                                 new DialogInterface.OnClickListener() {

                                     public void onClick(DialogInterface arg0,
                                                         int arg1) {


                                         Boolean checkinternet2= checkConnection();
                                         if (checkinternet2==true){
                                             // int pos = (int)v.getTag();
                                             filterList.remove(position);
                                             fragment.callFromProjectAdapter(listItem.getProjectId());
                                             projectAdapter.this.notifyDataSetChanged();
                                             arg0.cancel();
                                         }else {
                                             Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.container), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

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
                         return false;
                     }
                 });
             }else {

             }

             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Bundle dataBundle = new Bundle();
                     Intent intent = new Intent (v.getContext(), IssueListActivity.class);
                     SharedPreferences settings = activity.getSharedPreferences(PROJECT_NAME, 0);
                     SharedPreferences.Editor editor = settings.edit();
                     editor.putString("PROJECTID", projectId);
                     editor.putString("PROJETCTNAME", projectname);
                     editor.putString("TICKETCOUNT", ticketCount);
                     editor.commit();
                     v.getContext().startActivity(intent);
                 }
             });
             switch (ch){
                 case 'A':GradientDrawable bgShape = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape.setColor(Color.parseColor("#FF8286"));
                     bgShape.setStroke(1,Color.parseColor("#FF8286"));
                     break;
                 case 'a':GradientDrawable bgShapee = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee.setColor(Color.parseColor("#FF8286"));
                     bgShapee.setStroke(1,Color.parseColor("#FF8286"));
                     break;
                 case 'B':
                     GradientDrawable bgShape1 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape1.setColor(Color.parseColor("#FA6367"));
                     bgShape1.setStroke(1,Color.parseColor("#FA6367"));
                     break;
                 case 'b':
                     GradientDrawable bgShapee1 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee1.setColor(Color.parseColor("#FA6367"));
                     bgShapee1.setStroke(1,Color.parseColor("#FA6367"));
                     break;
                 case 'C':GradientDrawable bgShape4 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape4.setColor(Color.parseColor("#CD6B97"));
                     bgShape4.setStroke(1,Color.parseColor("#CD6B97"));
                     break;
                 case 'c':GradientDrawable bgShapee4 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee4.setColor(Color.parseColor("#CD6B97"));
                     bgShapee4.setStroke(1,Color.parseColor("#CD6B97"));
                     break;
                 case 'D':
                 case 'E':
                 case 'F':GradientDrawable bgShape7 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape7.setColor(Color.parseColor("#FF7055"));
                     bgShape7.setStroke(1,Color.parseColor("#FF7055"));
                     break;
                 case 'f':GradientDrawable bgShapee7 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee7.setColor(Color.parseColor("#FF7055"));
                     bgShapee7.setStroke(1,Color.parseColor("#FF7055"));
                     break;
                 case 'G':
                 case 'H':
                 case 'I':GradientDrawable bgShape10 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape10.setColor(Color.parseColor("#DC626F"));
                     bgShape10.setStroke(1,Color.parseColor("#DC626F"));
                     break;
                 case 'i':GradientDrawable bgShapee10 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee10.setColor(Color.parseColor("#DC626F"));
                     bgShapee10.setStroke(1,Color.parseColor("#DC626F"));
                     break;
                 case 'J':

                 case 'K':
                 case 'L':
                 case 'M':
                     GradientDrawable bgShape6 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape6.setColor(Color.parseColor("#FF789E"));
                     bgShape6.setStroke(1,Color.parseColor("#FF789E"));
                     break;
                 case 'm':
                     GradientDrawable bgShapee6 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee6.setColor(Color.parseColor("#FF789E"));
                     bgShapee6.setStroke(1,Color.parseColor("#FF789E"));
                     break;
                 case 'N':
                 case 'O':
                 case 'P':GradientDrawable bgShape3 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape3.setColor(Color.parseColor("#e74c3c"));
                     bgShape3.setStroke(1,Color.parseColor("#e74c3c"));
                     break;
                 case 'Q':
                 case 'R':GradientDrawable bgShape2 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape2.setColor(Color.parseColor("#FAA5AD"));
                     bgShape2.setStroke(1,Color.parseColor("#FAA5AD"));
                     break;
                 case 'r':GradientDrawable bgShapee2 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee2.setColor(Color.parseColor("#FAA5AD"));
                     bgShapee2.setStroke(1,Color.parseColor("#FAA5AD"));
                     break;
                 case 'S':GradientDrawable bgShape9 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape9.setColor(Color.parseColor("#E26688"));
                     bgShape9.setStroke(1,Color.parseColor("#E26688"));
                     break;
                 case 's':GradientDrawable bgShapee9 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee9.setColor(Color.parseColor("#E26688"));
                     bgShapee9.setStroke(1,Color.parseColor("#E26688"));
                     break;
                 case 'T':GradientDrawable bgShape5 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape5.setColor(Color.parseColor("#E26688"));
                     bgShape5.setStroke(1,Color.parseColor("#E26688"));
                     break;
                 case 't':GradientDrawable bgShapee5 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee5.setColor(Color.parseColor("#E26688"));
                     bgShapee5.setStroke(1,Color.parseColor("#E26688"));
                     break;
                 case 'U':
                 case 'V':
                 case 'W':GradientDrawable bgShape8 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShape8.setColor(Color.parseColor("#FC575E"));
                     bgShape8.setStroke(1,Color.parseColor("#FC575E"));
                     break;
                 case 'w':GradientDrawable bgShapee8 = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShapee8.setColor(Color.parseColor("#FC575E"));
                     bgShapee8.setStroke(1,Color.parseColor("#FC575E"));
                     break;
                 case 'X':
                 case 'Y':
                 case 'Z':
                 default:GradientDrawable bgShaped = (GradientDrawable)viewHolder.rountInitial.getBackground();
                     bgShaped.setColor(Color.parseColor("#e74c3c"));
                     bgShaped.setStroke(1,Color.parseColor("#e74c3c"));
                     break;
             }


        }else if (holder instanceof FooterViewHolder) {

            //your code here
        }


    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View View;


        public FooterViewHolder(View v) {
            super(v);
            View = v;
            prog = (ProgressBar) View.findViewById(R.id.pro);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    prog.setVisibility(android.view.View.GONE);
                }
            }, 1000);
            // Add your UI Components here
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
                    .make(activity.findViewById(R.id.container), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(projectlist);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (projectbean item : projectlist) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())||item.getTicketCount().contains(text.toLowerCase())
                        ||item.getTeamCount().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
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

}