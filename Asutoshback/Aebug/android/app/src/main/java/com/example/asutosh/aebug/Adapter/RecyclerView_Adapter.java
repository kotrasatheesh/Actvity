package com.example.asutosh.aebug.Adapter;

/**
 * Created by Asutosh on 06-09-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.asutosh.aebug.AppWebViewClients;
import com.example.asutosh.aebug.R;
import com.example.asutosh.aebug.bean.recylcer_data_model;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;


public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private List<recylcer_data_model> arrayList;
    private ArrayList<recylcer_data_model> imageslide;
    private Activity activity;

    public RecyclerView_Adapter(Activity activity, List<recylcer_data_model> arrayList, ArrayList<recylcer_data_model> imageslide) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.imageslide = imageslide;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final recylcer_data_model model = arrayList.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder

        //  final Bitmap image = model.getImage();
        String profilePhoto = model.getImageURl();
        if (model.getImageURl().contains(".docx")){
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.docs);
            mainHolder.imageview.setImageBitmap(icon);
        }else if (model.getImageURl().contains(".pdf")){
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.pdf);
            mainHolder.imageview.setImageBitmap(icon);
        }else if (model.getImageURl().contains(".xlsx")||model.getImageURl().contains(".xls")){
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.xlsx);
            mainHolder.imageview.setImageBitmap(icon);
        }else if (model.getImageURl().contains(".txt")){
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.txt);
            mainHolder.imageview.setImageBitmap(icon);
        } else {
            Picasso.with(activity)
                    .load(profilePhoto)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.warn_pic)
                    .fit()
                    .centerInside().into(mainHolder.imageview);
        }

        // This will convert drawbale image into
        // bitmap

        // setting title
//        mainHolder.title.setText(model.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (model.getImageURl().contains(".docx")||model.getImageURl().contains(".pdf")||model.getImageURl().contains(".ppt")
                        ||model.getImageURl().contains(".xls")||model.getImageURl().contains(".pptx")||model.getImageURl().contains(".xlsx")
                        ||model.getImageURl().contains(".txt")){

                    WebView_activity webview = new WebView_activity(activity);
                    activity.setContentView(webview);
                    String myUrl =model.getImageURl();
                    //   String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
                    Log.i(TAG, "Opening PDF: " + myUrl);
                    webview.setWebViewClient(new AppWebViewClients());
                    webview.getSettings().setJavaScriptEnabled(true);
                    // webview.getSettings().setUseWideViewPort(true);
                    webview.loadUrl("http://docs.google.com/gview?embedded=true&url="
                            + myUrl);

                }else {
                    //do nothing
                }*/
            }
        });

        //   mainHolder.imageview.setImageBitmap(image);


    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.recycler_item_row, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }
}


