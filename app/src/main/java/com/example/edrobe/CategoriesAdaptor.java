package com.example.edrobe;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrobe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.ViewHolder> {

    private List<Categories> categories;
    private Context context;

    public CategoriesAdaptor(List<Categories> categories, Context context) {

        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryImage.setImageResource(category.getImageResource());

        holder.itemView.setOnClickListener(view -> {
            Log.d("CategoriesAdapter", "Item clicked at position: " + holder.getAdapterPosition());
            Fragment newFragment;

            switch (holder.getAdapterPosition()) {
                case 0:
                    newFragment = new MensClothingFragment();
                    break;
                case 1:
                    newFragment = new WomenClothingFragment();
                    break;
                case 2:
                    newFragment = new KidsClothing();
                    break;
                case 3:
                    newFragment = new ShoesFragment();
                    break;
                case 4:
                    newFragment = new WatchesFragment();
                    break;
                default:
                    newFragment = new HomeFragment();
                    break;
            }

            FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        }

    @Override
    public int getItemCount() {

        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
