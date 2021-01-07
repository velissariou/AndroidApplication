package com.petros.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private static boolean CAMERA_GRANTED = false;
    private static boolean READ_EXTERNAL_STORAGE_GRANTED = false;
    private Button postButton;
    private ImageButton cameraButton;
    private ImageButton imageGalleryButton;
    private EditText statusTextView;
    private CheckBox twitterCheckBox;
    private CheckBox facebookCheckBox;
    private CheckBox instagramCheckBox;
    private ImageView selectedImageView;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String TAG = "Permissions";
    Uri selectedImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postButton = (Button) findViewById(R.id.postButton);
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        imageGalleryButton = (ImageButton) findViewById(R.id.imageGalleryButton);
        statusTextView =  (EditText) findViewById(R.id.statusTextMultiLine);
        selectedImageView = (ImageView) findViewById(R.id.selectedImageView);
        twitterCheckBox = (CheckBox) findViewById(R.id.twitterCheckBox);
        facebookCheckBox = (CheckBox) findViewById(R.id.facebookCheckBox);
        instagramCheckBox = (CheckBox) findViewById(R.id.instagramCheckBox);

        TwitterCalls twitterCalls = new TwitterCalls();

        imageGalleryButton.setOnClickListener(v -> {
            int hasReadStoragePermission =
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.d(TAG, "Has Read Storage Permissions " + hasReadStoragePermission);

            if (hasReadStoragePermission == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                READ_EXTERNAL_STORAGE_GRANTED = true;
            } else {
                Log.d(TAG, "Requesting Permission...");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
            if(READ_EXTERNAL_STORAGE_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(this, "Could not access gallery.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        cameraButton.setOnClickListener(v -> {
            int hasCameraPermission =
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CAMERA);
            Log.d(TAG, "Has Read Storage Permissions " + hasCameraPermission);

            if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                CAMERA_GRANTED = true;
            } else {
                Log.d(TAG, "Requesting Permission...");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            }
            if(CAMERA_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Camera not found.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        postButton.setOnClickListener(v -> {
            if(twitterCheckBox.isChecked()){
                Toast.makeText(this, "Posting Tweet...", Toast.LENGTH_SHORT).show();
                twitterCalls.postTweet(statusTextView.getText().toString(), selectedImageUri, this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageUri = getImageUri(this, imageBitmap);
            Picasso.get().load(selectedImageUri).into(selectedImageView);
            //selectedImageView.setImageBitmap(imageBitmap);

        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            Picasso.get().load(selectedImageUri).into(selectedImageView);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted!");
                    READ_EXTERNAL_STORAGE_GRANTED = true;
                } else {
                    Log.d(TAG, "Permission Denied.");
                }

            }
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission Granted!");
                    CAMERA_GRANTED = true;
                } else {
                    Log.d(TAG, "Permission Denied.");
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, new Date().toString(), null);
        Log.d("Path is:", path);
        return Uri.parse(path);
    }
}

// /sdcard/Pictures/Title (1).jpg
// /sdcard/Pictures/Tue Jan 05 00_14_37 GMT+02_00 2021.jpg