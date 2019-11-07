package com.manuh.mudrabook;

import android.Manifest;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout ll_bcard;
    Bitmap bitmap;
    Button btn_share;

    EditText et_name,et_email,et_contact;
    TextView tv_name,tv_email,tv_contact;

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
//Get the view’s background
        Drawable bgDrawable =v.getBackground();
        if (bgDrawable!=null)
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

        et_name = findViewById(R.id.et_name);
        et_email =findViewById(R.id.et_email);
        et_contact = findViewById(R.id.et_contact);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_contact = findViewById(R.id.tv_contact);


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
if(et_name.getText().toString().length()>0){
    if(et_email.getText().toString().length()>0){
        if(et_contact.getText().toString().length()>0){
            takeScreenShot(ll_bcard);
        }else{
            Toast.makeText(ProfileActivity.this,"Enter Your Contact",Toast.LENGTH_SHORT).show();
        }
    }else{
        Toast.makeText(ProfileActivity.this,"Enter Your Email",Toast.LENGTH_SHORT).show();
    }
}else{
    Toast.makeText(ProfileActivity.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
}
           // takeScreenShot(ll_bcard);

        });

        findViewById(R.id.buttonLogout).setOnClickListener(v ->{

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

        });
    }

    public void takeScreenShot(View v){
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
        startActivity(Intent.createChooser(shareIntent, "Share card"));
    }
}