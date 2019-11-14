package com.manuh.mudrabook;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout ll_bcard;
    Bitmap bitmap;
    Button btn_share;
    ImageView btn_edit,btn_save;
    String name, email, address, number, about;
    EditText et_name, et_email, et_contact, et_address;
    TextView tv_name, tv_email, tv_contact;
    ImageView iv_back;
    RequestQueue requestQueue;

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
//Get the view’s background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
//has background drawable, then draw it on the canvas
            bgDrawable.draw(c);
        else
//does not have background drawable, then draw white background on the canvas
            c.drawColor(Color.WHITE);
        v.draw(c);
        return b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_contact = findViewById(R.id.et_contact);
        iv_back = findViewById(R.id.iv_back);
        et_address = findViewById(R.id.et_address);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_contact = findViewById(R.id.tv_contact);

// number = "9700936228";
        btn_edit = findViewById(R.id.btn_edit);
        btn_save = findViewById(R.id.btn_save);

        iv_back.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        });


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MUDRABOOK", 0);
        number = pref.getString("number", "");
        btn_edit.setOnClickListener(v ->

        {
            //btn_edit.s("Save Profile");
         //   btn_edit.setImageResource(R.drawable.save);
            btn_edit.setVisibility(View.GONE);
            btn_save.setVisibility(View.VISIBLE);
            et_name.setEnabled(true);
            et_email.setEnabled(true);
            et_address.setEnabled(true);
            et_contact.setEnabled(true);



        });

        btn_save.setOnClickListener(v->{
            btn_edit.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
            if (et_name.getText().toString().length() > 0) {
                if (et_email.getText().toString().length() > 0) {
                    if (et_contact.getText().toString().length() > 0) {
                        name = et_name.getText().toString();
                        email = et_email.getText().toString();
                        address = et_address.getText().toString();
                        saveProfile();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Enter Your Contact", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_name.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_email.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_contact.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(ProfileActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ProfileActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        ll_bcard = findViewById(R.id.ll_bcard);
        btn_share = findViewById(R.id.btn_share);


        /*LAMBDA FUNCTION*/
        //btn_share.setOnClickListener(v -> Toast.makeText(ProfileActivity.this,"LAMBDA Function Testing",Toast.LENGTH_SHORT).show());
        btn_share.setOnClickListener(v -> {
            if (et_name.getText().toString().length() > 0) {
                if (et_email.getText().toString().length() > 0) {
                    if (et_contact.getText().toString().length() > 0) {
                        takeScreenShot(ll_bcard);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Enter Your Contact", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            }
            // takeScreenShot(ll_bcard);

        });

       /* findViewById(R.id.buttonLogout).setOnClickListener(v ->{

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

        });*/

        getProfileData();
    }


    public void getProfileData() {
        //getUserByMobileNo

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://125.62.194.124:8080/mudrabook/api/mudrabook/getUserByMobileNo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    if (j.getString("message").equalsIgnoreCase("User Information")) {
                        JSONObject jdata = j.getJSONObject("data");
                        et_name.setText(jdata.getString("username"));
                        et_address.setText(jdata.getString("address"));
                        et_email.setText(jdata.getString("userEmail"));
                        et_contact.setText(jdata.getString("mobileNo"));
                        et_name.setEnabled(false);
                        et_email.setEnabled(false);
                        et_address.setEnabled(false);
                        et_contact.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNo", number);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void takeScreenShot(View v) {
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "screen.jpg";
// create bitmap screen capture


        ll_bcard.setDrawingCacheEnabled(true);
// bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        bitmap = loadBitmapFromView(ll_bcard, ll_bcard.getWidth(), ll_bcard.getHeight());
        ll_bcard.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(mPath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
            sendScreenShot(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendScreenShot(File imageFile) {
        Uri uri = Uri.fromFile(imageFile);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Screenshot");
        //  shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share card"));
    }

    public void saveProfile() {
        JSONObject j = new JSONObject();
        try {
            j.put("userEmail", email);
            j.put("mobileNo", number);
            j.put("address", address);
            j.put("username", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jo = new JsonObjectRequest(Request.Method.POST, "http://125.62.194.124:8080/mudrabook/api/mudrabook/saveUser", j, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getString("message").equalsIgnoreCase("Saved successfully")) {
                        Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                       // btn_edit.setText("Edit");
                      //  btn_edit.setImageResource(R.drawable.edit);

                        btn_edit.setVisibility(View.VISIBLE);
                        btn_save.setVisibility(View.GONE);
                        et_name.setEnabled(false);
                        et_address.setEnabled(false);
                        et_email.setEnabled(false);
                        et_contact.setEnabled(false);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jo);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();
    }
}