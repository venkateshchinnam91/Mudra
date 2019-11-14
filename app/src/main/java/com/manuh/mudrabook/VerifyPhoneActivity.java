package com.manuh.mudrabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class VerifyPhoneActivity extends AppCompatActivity {

String phonenumber,str_otp;
    private String verificationId;
    DataServices dataServices;
    private EditText editText;
    RequestQueue requestQueue;
    //private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    TextView textView1;
ImageView iv_back;
    private OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        requestQueue = Volley.newRequestQueue(VerifyPhoneActivity.this);
     //   mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);

        dataServices = APIUtils.getAPIService();

        textView1 = findViewById(R.id.textView1);


         phonenumber = getIntent().getStringExtra("phonenumber");
        textView1.append("  "+phonenumber);
        sendVerificationCode(phonenumber);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v-> {

            startActivity(new Intent(VerifyPhoneActivity.this,LoginActivity.class));

        });



        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                str_otp = otp;
                // do Stuff
                Log.d("onOtpCompleted=>", otp);
            }
        });
        /*otpView.setListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {
                str_otp = otp;
                // do Stuff
                Log.d("onOtpCompleted=>", otp);
            }
        });*/
        findViewById(R.id.buttonSignIn).setOnClickListener(v -> {

                String code = str_otp;//editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 4) {

                    Toast.makeText(VerifyPhoneActivity.this,"Enter Valid OTP", Toast.LENGTH_SHORT).show();
                  /*  editText.setError("Enter code...");
                    editText.requestFocus();*/
                    return;
                }
                verifyCode(code);

        });

    }



    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.GONE);


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

       /* Call<POST> call2 = dataServices.verifyOTP(phonenumber,str_otp);

        call2.enqueue(new Callback<POST>() {
            @Override
            public void onResponse(Call<POST> call, Response<POST> response) {
              //  System.out.println(response.body().toString());
                Toast.makeText(VerifyPhoneActivity.this,response.body().toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jo =  new JSONObject(response.body().toString());
                    if(jo.getString("message").equalsIgnoreCase("Valid")){
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MUDRABOOK", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("number", phonenumber);
                        editor.putString("login","TRUE");

                        editor.commit(); // commit changes
                        startActivity(new Intent(VerifyPhoneActivity.this,ProfileActivity.class));
                        finish();
                    }else{
                        Toast.makeText(VerifyPhoneActivity.this,"INVALID OTP",Toast.LENGTH_SHORT).show();
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
                        finish();
                    }else{
                        Toast.makeText(VerifyPhoneActivity.this,"INVALID OTP",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {

                error.printStackTrace();

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params =  new HashMap<>();
                params.put("mobileNo",phonenumber);
                params.put("otp",code);
                return params;
            }
        }
        ;
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VerifyPhoneActivity.this,LoginActivity.class));
        finish();
    }
}
