package com.example.powierzchniarysunkowa;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ImageFragment extends Fragment {
    private ImageView imageView;
    private Image image;

    public void setImage(Image image) {
        this.image = image;
        if (imageView != null) showImage();
    }

    private void showImage() {
        Uri uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                image.id
        );
        imageView.setImageURI(uri);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imageView_full);
        if (image != null) showImage();
    }
}
