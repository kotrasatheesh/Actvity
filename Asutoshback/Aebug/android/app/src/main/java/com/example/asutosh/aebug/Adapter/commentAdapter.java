package com.example.asutosh.aebug.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
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

import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.Edit_issue_details;
import com.example.asutosh.aebug.Issue_DetailsActivity;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.commentBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.Edit_issue_details.areaListview;
import static com.example.asutosh.aebug.Issue_DetailsActivity.CommnetListV;

/**
 * Created by Asutosh on 06-09-2017.
 */

public class commentAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    Context context;
    private List<commentBean> plist;
    private ClickListener clicklistener = null;


    public commentAdapter(Context context,Activity activity, List<commentBean> plist) {
        this.activity = activity;
        this.plist = plist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return plist.size();
    }

    @Override
    public Object getItem(int position) {
        return plist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.comment_row_item, null);
            holder.rltv_item = (RelativeLayout) convertView.findViewById(R.id.relt);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.ref = position;
        CircleImageView pic = (CircleImageView) convertView.findViewById(R.id.commnetID_proPic);
        TextView name = (TextView) convertView.findViewById(R.id.by_name);
        TextView comment = (TextView) convertView.findViewById(R.id.commentgiven);
        TextView date = (TextView) convertView.findViewById(R.id.commentDate);

        final commentBean m = plist.get(position);
        name.setText(String.valueOf(m.getCommentedBy()));
        comment.setText(String.valueOf(m.getCommentText()));
        date.setText(String.valueOf(m.getCommentedOn()));
        String s=String.valueOf(m.getProfilePhoto());

        holder.rltv_item.setTag(position);
        holder.rltv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicklistener != null) {
                    clicklistener.itemClicked(v, position);
                }
            }
        });

        if (m.getCommentedById().matches(loginuserid)){
            holder.rltv_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                    alertbox.setMessage("Are you sure , You want to delete the Comment !");
                    alertbox.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {

                                    Boolean checkinternet2= checkConnection();
                                    if (checkinternet2==true){
                                        int pos = (int)v.getTag();
                                        plist.remove(pos);
                                        setListViewHeightBasedOnChildren(CommnetListV);
                                        if(context instanceof Issue_DetailsActivity){
                                            ((Issue_DetailsActivity)context).cllgetComment(m.getCommentId().toString());
                                        }
                                        commentAdapter.this.notifyDataSetChanged();
                                        arg0.cancel();
                                    }else {
                                        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.issueDetailLay), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);

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
            //do nothing
        }

        Picasso.with(activity).invalidate(s);
        Picasso.with(activity)
                .load(s)
                .placeholder(R.mipmap.default_profile)
                .fit()
                .centerCrop().into(pic);

        return convertView;
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
                    .make(activity.findViewById(R.id.issueDetailLay), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


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
    private class ViewHolder {
        RelativeLayout rltv_item;
        EditText editText1;
        int ref;
        String id;
    }
}

