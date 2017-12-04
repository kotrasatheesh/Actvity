package com.example.asutosh.aebug.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.asutosh.aebug.ClickListener;
import com.example.asutosh.aebug.R;

/**
 * Created by Asutosh on 24-08-2017.
 */

public class issule_list_Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final TextView mItemTextView;
    private ClickListener clicklistener = null;
    public issule_list_Viewholder(final View parent, TextView itemTextView) {
        super(parent);
        mItemTextView = itemTextView;
    }

    public static issule_list_Viewholder newInstance(View parent) {
        TextView itemTextView = (TextView) parent.findViewById(R.id.issueTitle);
        return new issule_list_Viewholder(parent, itemTextView);
    }

    public void setItemText(CharSequence text) {
        mItemTextView.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (clicklistener != null) {
            clicklistener.itemClicked(v, getAdapterPosition());
        }
    }

}
