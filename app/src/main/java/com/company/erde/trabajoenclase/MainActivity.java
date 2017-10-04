package com.company.erde.trabajoenclase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button bGallery;
    private Button bPhoto;
    private TextView tvUrl;
    private ImageView ivCapture;
    private static  final int REQUEST_INTERNAL_STORAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bGallery = (Button) findViewById(R.id.bGallery);
        bPhoto = (Button) findViewById(R.id.bPhoto);
        tvUrl = (TextView) findViewById(R.id.tvUrl);
        ivCapture = (ImageView) findViewById(R.id.ivCapture);


        bGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission(REQUEST_INTERNAL_STORAGE)){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select"), REQUEST_INTERNAL_STORAGE);
                }else{
                    requestPermissions(REQUEST_INTERNAL_STORAGE);
                }
            }
        });

        bPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission(REQUEST_IMAGE_CAPTURE)){
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }else{
                    requestPermissions(REQUEST_IMAGE_CAPTURE);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivCapture.setImageBitmap(imageBitmap);
                break;

            case REQUEST_INTERNAL_STORAGE:
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                        // Get the path from the Uri
                        String path = getPathFromURI(selectedImageUri);
                        tvUrl.setText(path);
                        Log.d(TAG, "Image Path : " + path);
                    Log.d(TAG, "Image Path1 : " + selectedImageUri);
                        // Set the image in ImageView
                        ivCapture.setImageURI(selectedImageUri);
                    }
                    break;
        }

    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private boolean checkPermission(int requestCode) {
        switch (requestCode){
            case REQUEST_INTERNAL_STORAGE:
                int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

                return permission == PackageManager.PERMISSION_GRANTED;

            case REQUEST_IMAGE_CAPTURE:

                int permissionCamera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

                return permissionCamera == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }



    private void requestPermissions(int requestCode) {
        switch (requestCode){
            case REQUEST_INTERNAL_STORAGE:
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(this,"No se dieron permisos",Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_INTERNAL_STORAGE);
                }

            case REQUEST_IMAGE_CAPTURE:

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    Toast.makeText(this,"No se dieron permisos",Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
                }
        }

    }

}
