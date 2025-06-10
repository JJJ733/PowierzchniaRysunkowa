package com.example.powierzchniarysunkowa;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {
    private List<Image> imageList = new ArrayList<>();
    private ImageListAdapter adapter;
    private ImageSelectedCallback callback;

    public interface ImageSelectedCallback {
        void onImageSelected(Image image);
    }

    public void setCallback(ImageSelectedCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_images);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ImageListAdapter(imageList, image -> {
            if (callback != null) callback.onImageSelected(image);
        });
        recyclerView.setAdapter(adapter);
        loadImages();
    }

    private void loadImages() {
        Uri collection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                : MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        String selection = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? MediaStore.Images.Media.OWNER_PACKAGE_NAME + " = ?"
                : null;

        String[] selectionArgs = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? new String[]{requireContext().getPackageName()}
                : null;

        try (Cursor cursor = requireContext().getContentResolver().query(
                collection, projection, selection, selectionArgs,
                MediaStore.Images.Media.DISPLAY_NAME + " ASC")) {

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            imageList.clear();

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                imageList.add(new Image(id, name));
            }

            adapter.setImageList(imageList);
        }
    }
}
