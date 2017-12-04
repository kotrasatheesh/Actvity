package com.example.asutosh.aebug;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asutosh.aebug.App_config.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.asutosh.aebug.App_config.AppController.loginuserid;
import static com.example.asutosh.aebug.App_config.AppController.userEmail;
import static com.example.asutosh.aebug.App_config.AppController.userName;


public class forgotPassword extends AppCompatActivity {

    private ProgressDialog pDialog;
    JSONObject jsonBody;
    EditText email;
    Button submit;
    RelativeLayout forgotlay;
    private static final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgotpass_layout);
        email=(EditText)findViewById(R.id.enterEmail);
        submit=(Button) findViewById(R.id.EmailLink);
        forgotlay=(RelativeLayout) findViewById(R.id.forgotlay);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaillink=email.getText().toString();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (!emaillink.matches(emailPattern) || emaillink.isEmpty()) {
                            email.setError("Invalid username !");
                            return;
                        }
                        else if (!emaillink.isEmpty()){
                            Boolean checkinternet= isNetworkConnected();
                            if (checkinternet==true){
                                Forgotpass(emaillink);
                            }else {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        forgotPassword.this).create();

                                // Setting Dialog Title
                                //alertDialog.setTitle("Alert Dialog");

                                // Setting Dialog Message
                                alertDialog.setMessage("No internet connection !");

                                // Setting Icon to Dialog
                                //  alertDialog.setIcon(R.drawable.tick);

                                // Setting OK Button
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog closed

                                    }
                                });
                                alertDialog.show();
                                // Showing Alert Message


                            }

                        }
            }
        });



               //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
// Ui Parameters ID's
private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
        }
    private void Forgotpass(String email) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        {
            try {
                jsonBody = new JSONObject();
                jsonBody.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AppUtils.ForgotPassword, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                JSONObject data = new JSONObject(String.valueOf(response));
                                String msg=data.getString("Message");
                                String status=data.getString("Status");

                                if (status.matches("0")){
                                    Snackbar snackbar1 = Snackbar.make(forgotlay, msg, Snackbar.LENGTH_INDEFINITE);
                                    snackbar1.show();
                                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);
                                }else {
                                    Snackbar snackbar1 = Snackbar.make(forgotlay, msg, Snackbar.LENGTH_INDEFINITE);
                                    snackbar1.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            //   System.out.println("id..." + loginuserid);
                            pDialog.hide();

                        /*    SharedPreferences setting = getApplicationContext().getSharedPreferences(
                                    MyPREFERENCES, 0);
                            SharedPreferences.Editor editor = setting.edit();
                            editor.putString("wid", String.valueOf(loginuserid));
                            // editor.putString("ptus", String.valueOf(pstatus));
                            editor.commit();*/
                            // System.out.println("id..." + Wo);

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }

            };


            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    }

}
