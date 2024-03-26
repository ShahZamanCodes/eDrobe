package com.example.edrobe;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrobe.CategoriesFragment;
import com.example.edrobe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private List<Integer> imageList;

    public ImageSliderAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        int imageResId = imageList.get(position);
        holder.bindImage(imageResId);

        holder.imageView.setOnClickListener(view -> {
            Fragment categoriesFragment = new CategoriesFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, categoriesFragment).commit();
            BottomNavigationView bottomNavigationView = ((AppCompatActivity) view.getContext()).findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.navCategories);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bindImage(int imageResId) {
            // Load the image drawable
            Drawable originalDrawable = ContextCompat.getDrawable(imageView.getContext(), imageResId);
            Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

            // Create a rounded bitmap drawable
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), originalBitmap);
            roundedDrawable.setCornerRadius(20);
            roundedDrawable.setAntiAlias(true);

            imageView.setImageDrawable(roundedDrawable);
        }
    }
}
