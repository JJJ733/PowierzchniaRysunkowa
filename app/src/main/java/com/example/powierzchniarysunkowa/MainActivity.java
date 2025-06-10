/*package com.example.powierzchniarysunkowa;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DrawingSurface drawingSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingSurface = findViewById(R.id.drawingSurface);

        findViewById(R.id.button_red).setOnClickListener(v -> drawingSurface.setColor(Color.RED));
        findViewById(R.id.button_green).setOnClickListener(v -> drawingSurface.setColor(Color.GREEN));
        findViewById(R.id.button_blue).setOnClickListener(v -> drawingSurface.setColor(Color.BLUE));
        findViewById(R.id.button_yellow).setOnClickListener(v -> drawingSurface.setColor(Color.YELLOW));
        findViewById(R.id.button_clear).setOnClickListener(v -> drawingSurface.clear());
    }
}*/


package com.example.powierzchniarysunkowa;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.Color;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DrawingSurface drawingSurface;
    private static final int REQUEST_CODE_WRITE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingSurface = findViewById(R.id.drawingSurface);

        // Obsługa przycisków kolorów
        findViewById(R.id.button_red).setOnClickListener(v -> drawingSurface.setColor(Color.RED));
        findViewById(R.id.button_green).setOnClickListener(v -> drawingSurface.setColor(Color.GREEN));
        findViewById(R.id.button_blue).setOnClickListener(v -> drawingSurface.setColor(Color.BLUE));
        findViewById(R.id.button_yellow).setOnClickListener(v -> drawingSurface.setColor(Color.YELLOW));
        findViewById(R.id.button_clear).setOnClickListener(v -> drawingSurface.clear());
    }

    // Menu (Save Image / Browse Images)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            // Android < 10 → prośba o uprawnienia
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE);
            } else {
                saveImage();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_browse) {
            Intent intent = new Intent(this, BrowseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        ContentResolver resolver = getContentResolver();
        Uri imageCollection;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollection = MediaStore.Images.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues imageDetails = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + ".png";

        imageDetails.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        imageDetails.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        Uri imageUri = resolver.insert(imageCollection, imageDetails);

        try (ParcelFileDescriptor pfd =
                     resolver.openFileDescriptor(imageUri, "w", null);
             FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor())) {

            drawingSurface.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd zapisu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.clear();
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(imageUri, imageDetails, null, null);
        }

        Toast.makeText(this, "Zapisano: " + fileName, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(this, "Brak uprawnień do zapisu", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

