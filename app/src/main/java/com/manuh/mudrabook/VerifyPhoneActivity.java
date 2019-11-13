package com.manuh.mudrabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class VerifyPhoneActivity extends AppCompatActivity {

String phonenumber;
    private String verificationId;
    DataServices dataServices;
    private EditText editText;
    RequestQueue requestQueue;
    //private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        requestQueue = Volley.newRequestQueue(VerifyPhoneActivity.this);
     //   mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        dataServices = APIUtils.getAPIService();

         phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        findViewById(R.id.buttonSignIn).setOnClickListener(v -> {


                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 4) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);

        });

    }



    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);


    /*    StringRequest stringRequest =  new StringRequest(Request.Method.POST, "http://125.62.194.124:8080/mudrabook/api/mudrabook/sendOTP", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("mobileNo", number);
                return params;
            }
        };
        requestQueue.add(stringRequest);*/





       Call<POST> call1 =  dataServices.getOTP(number);
        call1.enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {
               System.out.println( response.body().toString());
               progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<POST> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }
    public void verifyCode(String code){

       /* Call<POST> call2 = dataServices.verifyOTP(phonenumber,code);

        call2.enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {
                System.out.println(response.body().toString());
                Toast.makeText(VerifyPhoneActivity.this,response.body().toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jo =  new JSONObject(response.body().toString());

                    if(jo.getString("message").equalsIgnoreCase("Valid")){
                        startActivity(new Intent(VerifyPhoneActivity.this,HomeActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<POST> call, Throwable t) {
                    t.printStackTrace();
                Toast.makeText(VerifyPhoneActivity.this,"ERROR ",Toast.LENGTH_SHORT).show();

            }
        });*/

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, "http://125.62.194.124:8080/mudrabook/api/mudrabook/validateOTPWithMobile", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo =  new JSONObject(response);
                    if(jo.getString("message").equalsIgnoreCase("Valid")){



                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MUDRABOOK", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("number", phonenumber);
                        editor.putString("login","TRUE");

                        editor.commit(); // commit changes
                        startActivity(new Intent(VerifyPhoneActivity.this,ProfileActivity.class));
                    }else{
                        Toast.makeText(VerifyPhoneActivity.this,"INVALID OTP",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put("mobileNo",phonenumber);
                params.put("otp",code);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}
