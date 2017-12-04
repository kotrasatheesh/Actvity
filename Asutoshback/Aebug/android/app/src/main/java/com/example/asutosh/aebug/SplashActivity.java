package com.example.asutosh.aebug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.example.asutosh.aebug.App_config.AppController.RoleID;
import static com.example.asutosh.aebug.App_config.AppController.RoleName;
import static com.example.asutosh.aebug.App_config.AppController.TeamId;
import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.userName;


public class SplashActivity extends AppCompatActivity {
    private ProgressBar mProgress;
    public static final String PREF_NAME = "Divid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        InitUI();
    }

    private void InitUI() {
        onIntent();
    }

    private void onIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Boolean checkinternet= isNetworkConnected();
                if (checkinternet==true){
                    SharedPreferences settings = getSharedPreferences(LoginActivity.PREF_NAME, 0);
                    boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
                    loginuserid = settings.getString("USERID", String.valueOf(loginuserid));
                    RoleID = settings.getString("ROLLID", String.valueOf(RoleID));
                    RoleName = settings.getString("ROLLNAME", String.valueOf(RoleName));
                    TeamId = settings.getString("TEAMID", String.valueOf(TeamId));
                  //  RegionID = settings.getString("REGION_ID", String.valueOf(RegionID));
                    if(hasLoggedIn)
                    {
                        //Go directly to main activity.
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }else {
                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }
                }else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(
                            SplashActivity.this).create();

                    // Setting Dialog Title
                    //alertDialog.setTitle("Alert Dialog");

                    // Setting Dialog Message
                    alertDialog.setMessage("No internet connection !");

                    // Setting Icon to Dialog
                    //  alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (checkinternet==true){
                                SharedPreferences settings = getSharedPreferences(LoginActivity.PREF_NAME, 0);
                                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn",false);
                                loginuserid = settings.getString("USERID", String.valueOf(loginuserid));
                                userName = settings.getString("USERNAME", String.valueOf(userName));
                               // RegionID = settings.getString("REGION_ID", String.valueOf(RegionID));
                                if(hasLoggedIn)
                                {
                                    //Go directly to main activity.
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                }else {
                                    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(i);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"Check you Internet!",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            // Write your code here to execute after dialog closed

                        }
                    });
                    alertDialog.show();
                    // Showing Alert Message


                }

               /* startActivity(new Intent(SplashActivity.this, LoginActivity.class));
             SplashActivity.this.finish();*/
            }
        }, 2000);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
