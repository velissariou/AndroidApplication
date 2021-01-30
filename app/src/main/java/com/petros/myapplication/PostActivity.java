package com.petros.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private static boolean CAMERA_GRANTED = false;
    private static boolean READ_EXTERNAL_STORAGE_GRANTED = false;
    private Button postButton;
    private Button instagramPostButton;
    private ImageButton cameraButton;
    private ImageButton imageGalleryButton;
    private Button facebookStoryButton;
    private Button instagramStoryButton;
    private EditText statusTextView;
    private ShareButton shareButton;
    private ImageView selectedImageView;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String TAG = "Permissions";
    private CallbackManager callbackManager;
    Uri selectedImageUri = null;
    String APP_ID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        callbackManager = CallbackManager.Factory.create();
        shareButton = findViewById(R.id.fb_share_button);
        postButton = findViewById(R.id.postButton);
        instagramPostButton = findViewById(R.id.instagram_share_button);
        facebookStoryButton = findViewById(R.id.facebook_story_button);
        instagramStoryButton = findViewById(R.id.instagram_story_button);
        cameraButton = findViewById(R.id.cameraButton);
        imageGalleryButton = findViewById(R.id.imageGalleryButton);
        statusTextView = findViewById(R.id.statusTextMultiLine);
        selectedImageView = findViewById(R.id.selectedImageView);
        APP_ID = getString(R.string.facebook_app_id);

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
            Toast.makeText(this, "Posting Tweet...", Toast.LENGTH_SHORT).show();
            twitterCalls.postTweet(statusTextView.getText().toString(), selectedImageUri, this);
        });

        instagramPostButton.setOnClickListener(v -> {
            // Create the new Intent using the 'Send' action.
            Intent share = new Intent(Intent.ACTION_SEND);

            // Set the MIME type
            share.setType("image/*");

            // Add the URI to the Intent.
            share.putExtra(Intent.EXTRA_STREAM, selectedImageUri);

            // Broadcast the Intent.
            startActivity(Intent.createChooser(share, "Share to"));
        });

        facebookStoryButton.setOnClickListener(v -> {

            // Instantiate implicit intent with ADD_TO_STORY action
            Intent intent = new Intent("com.facebook.stories.ADD_TO_STORY");
            intent.setDataAndType(selectedImageUri, "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("com.facebook.platform.extra.APPLICATION_ID", APP_ID);

            startActivity(intent);

        });

        instagramStoryButton.setOnClickListener(v -> {
            // Instantiate implicit intent with ADD_TO_STORY action and background asset
            Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
            intent.setDataAndType(selectedImageUri, "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        SharePhoto photo = new SharePhoto.Builder()
                .setImageUrl(selectedImageUri)
                .build();
        SharePhotoContent photoContent = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareButton.setShareContent(photoContent);
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

