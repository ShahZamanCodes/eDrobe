package com.example.edrobe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> itemsArrayList;

    public ItemAdapter(Context context, ArrayList<Item> itemsArrayList) {
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }


    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_lists, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        Item items = itemsArrayList.get(position);
        holder.Name.setText(items.Name);
        holder.Description.setText(items.Description);
        holder.Price.setText(items.Price);
        Picasso.get().load(items.getImageUrl()).into(holder.ImageUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemDetailsFragment(items);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name, Description, Price;
        ImageView ImageUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.itemName);
            Description = itemView.findViewById(R.id.itemDescription);
            Price = itemView.findViewById(R.id.itemPrice);
            ImageUrl = itemView.findViewById(R.id.itemImage);
        }
    }

    private void openItemDetailsFragment(Item item) {
        Bundle args = new Bundle();
        args.putString("imageUrl", item.getImageUrl());
        args.putString("itemName", item.getName());
        args.putString("itemPrice", item.getPrice());
        args.putString("itemDescription", item.getDescription());

        ItemDetailFragment itemDetailsFragment = new ItemDetailFragment();
        itemDetailsFragment.setArguments(args);

        FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, itemDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
