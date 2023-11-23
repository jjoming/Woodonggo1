package com.example.woodonggo;


import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    // Leave this empty for now
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        // Register Firebase Storage model loader
        registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }
}
