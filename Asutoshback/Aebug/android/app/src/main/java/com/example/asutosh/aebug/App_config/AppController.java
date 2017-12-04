package com.example.asutosh.aebug.App_config;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.asutosh.aebug.ConnectivityReceiver;
import com.example.asutosh.aebug.MainActivity;

import java.util.ArrayList;

import q.rorbin.badgeview.QBadgeView;

import static com.example.asutosh.aebug.MainActivity.v;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

/**
 * Created by Aezion on 1/24/2017.
 */

public class AppController extends Application {
    public  static int statusBadge=0;
    public  static String loginuserid="";
    public  static String userName="";
    public  static String userEmail="";
    public  static String RoleID="";
    public  static String RoleName="";
    public  static int homeback;
    public  static String TeamId="";
    public static ArrayList<String> addedArea =new ArrayList<String>();
    public static ArrayList<String> addedmem =new ArrayList<String>();
    public static ArrayList<String> assignmemberpop =new ArrayList<String>();
    public static ArrayList<String> areapop =new ArrayList<String>();
    public static ArrayList<String> saveareaspin=new ArrayList<String>();
    public static ArrayList<String> qnt_instl=new ArrayList<String>();
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
