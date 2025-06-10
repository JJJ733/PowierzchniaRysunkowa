package com.example.powierzchniarysunkowa;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BrowseActivity extends AppCompatActivity implements ImageListFragment.ImageSelectedCallback {

    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        ImageListFragment listFragment = (ImageListFragment)
                getSupportFragmentManager().findFragmentById(R.id.imageListFragment);
        if (listFragment != null) {
            listFragment.setCallback(this);
        }
    }

    @Override
    public void onImageSelected(Image image) {
        if (isLandscape) {
            ImageFragment imageFragment = (ImageFragment)
                    getSupportFragmentManager().findFragmentById(R.id.imageViewFragment);
            if (imageFragment != null) imageFragment.setImage(image);
        } else {
            Intent intent = new Intent(this, ViewActivity.class);
            intent.putExtra("image", image);
            startActivity(intent);
        }
    }
}
