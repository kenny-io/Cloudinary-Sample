package com.example.ekene.cloudinarsample;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int INTENT_REQUEST_CODE = 222;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.imageView);

        fab = findViewById(R.id.fab);

        MediaManager.init(this, new BackendServerSignatureProvider());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromDevice();
            }
        });

    }

    public void chooseImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }
    public String getUrlForMaxWidth(String imageId) {
        int width = Utils.getScreenWidth(this);
        return MediaManager.get().getCloudinary().url().transformation(new Transformation().width(width)).generate(imageId);
    }

    private void showSnackBar(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == INTENT_REQUEST_CODE && data != null && data.getData() != null) {
            MediaManager.get().upload(data.getData()).callback(new UploadCallback() {

                @Override
                public void onStart(String requestId) {
                    showSnackBar("Upload started...");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Picasso.with(MainActivity.this).load(getUrlForMaxWidth(resultData.get("public_id").toString())).into(imageView);
                    showSnackBar("Upload complete!");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    showSnackBar("Upload error: " + error.getDescription());
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    showSnackBar("Upload rescheduled.");
                }
            }).dispatch();
        }}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}