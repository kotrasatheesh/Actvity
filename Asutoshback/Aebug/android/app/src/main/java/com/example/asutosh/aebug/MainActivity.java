package com.example.asutosh.aebug;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.asutosh.aebug.fragments.notification_frag;
import com.example.asutosh.aebug.fragments.profile_fragment;
import com.example.asutosh.aebug.fragments.home_fragment;
import com.example.asutosh.aebug.fragments.status_chart_fragment;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;

import static com.example.asutosh.aebug.App_config.AppController.statusBadge;
import static com.example.asutosh.aebug.fragments.home_fragment.STATUS_PRE;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public static View v;
    public static QBadgeView qBadgeView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        v=(View)findViewById( R.id.navigation_notification);
        // number of menu from left
       /* SharedPreferences settings = getSharedPreferences(STATUS_PRE, 0);
        int count=settings.getInt("COUNT",0);
        if (count!=0){
            new QBadgeView(MainActivity.this).bindTarget(v).setBadgeNumber(count);
        }*/
       qBadgeView=new QBadgeView(this);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(STATUS_PRE, 0);
        SharedPreferences.Editor editor = settings.edit();
        int count=settings.getInt("COUNT",0);
        statusBadge=count;
        qBadgeView.setVisibility(View.VISIBLE);
        qBadgeView.bindTarget(v).setBadgeNumber(statusBadge);
      //  disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
                        switch (item.getItemId()) {
                            case  R.id.navigation_dashboard:
                                selectedFragment = status_chart_fragment.newInstance();
                                break;

                            case  R.id.navigation_home:
                                selectedFragment = home_fragment.newInstance();
                                break;
                            case R.id.navigation_notification:
                                selectedFragment = notification_frag.newInstance();
                                break;
                            case R.id.navigation_profile:
                                selectedFragment = profile_fragment.newInstance();
                                break;

                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content, selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, status_chart_fragment.newInstance());
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        transaction.commit();

    }
   /* public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
           *//* for (int i = 0; i < menuView.getChildCount(); i++) {
           //     BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
           //     item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
         //       item.setChecked(item.getItemData().isChecked());
            }*//*
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }*/
 /*  @Override
   public void onBackPressed() {

       FragmentManager fragmentManager = getSupportFragmentManager();
       for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
           String ide = fragmentManager.getBackStackEntryAt(entry).getName();
           Log.i("TAG", "Found fragment: " + ide);
       }
       if (fragmentManager.getBackStackEntryCount() > 1) {
           fragmentManager.popBackStackImmediate();

           //

       } else  {
           super.onBackPressed();
       }
   }*/
    @Override
    public void onBackPressed() {

            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 0) {
                manager.popBackStackImmediate();
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if(currentFragment instanceof status_chart_fragment){
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                }
                else if(currentFragment instanceof home_fragment){
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                }
                else if(currentFragment instanceof notification_frag){
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                }
                else if(currentFragment instanceof profile_fragment){
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                }
            }else  {
                super.onBackPressed();
            }
        }


}
