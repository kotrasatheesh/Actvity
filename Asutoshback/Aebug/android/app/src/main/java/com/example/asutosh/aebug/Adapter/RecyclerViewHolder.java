package com.example.asutosh.aebug.Adapter;

/**
 * Created by Asutosh on 06-09-2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asutosh.aebug.R;


/**
 * Created by SONU on 25/09/15.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public ImageView imageview;




    public RecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.title = (TextView) view.findViewById(R.id.title);
        this.imageview = (ImageView) view.findViewById(R.id.image1);


    }



}