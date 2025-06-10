package com.example.powierzchniarysunkowa;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Image image = getIntent().getParcelableExtra("image");
        ImageFragment imageFragment = (ImageFragment)
                getSupportFragmentManager().findFragmentById(R.id.imageViewFragment);
        if (imageFragment != null && image != null) {
            imageFragment.setImage(image);
        }
    }
}
