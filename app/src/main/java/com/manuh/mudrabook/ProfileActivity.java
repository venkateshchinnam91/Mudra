package com.manuh.mudrabook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout ll_bcard;
    Bitmap bitmap;
    Button btn_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
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
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        ll_bcard = findViewById(R.id.ll_bcard);
        btn_share = findViewById(R.id.btn_share);
        ll_bcard.setDrawingCacheEnabled(true);
        ll_bcard.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        ll_bcard.layout(0, 0, ll_bcard.getMeasuredWidth(), ll_bcard.getMeasuredHeight());
        ll_bcard.buildDrawingCache(true);

        /*LAMBDA FUNCTION*/
        //btn_share.setOnClickListener(v -> Toast.makeText(ProfileActivity.this,"LAMBDA Function Testing",Toast.LENGTH_SHORT).show());
        btn_share.setOnClickListener(v->{

                Bitmap b = Bitmap.createBitmap(ll_bcard.getDrawingCache());
                ll_bcard.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "bc.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        });



       /* final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        //final File photoFile = new File(getFilesDir(), "foo.jpg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(ProfileActivity.this,b));
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
*/

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        });
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
