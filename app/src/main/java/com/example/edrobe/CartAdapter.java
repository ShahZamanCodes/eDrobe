package com.example.edrobe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private TextView subTotalTextView, deliveryChargesTextView, totalChargesTextView;
    public CartAdapter(List<CartItem> cartItems, TextView subTotalTextView, TextView deliveryChargesTextView, TextView totalChargesTextView) {
        this.cartItems = cartItems;
        this.subTotalTextView = subTotalTextView;
        this.deliveryChargesTextView = deliveryChargesTextView;
        this.totalChargesTextView = totalChargesTextView;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);

        if (currentItem != null) {
            holder.itemNameTextView.setText(currentItem.getItemName());
            holder.itemPriceTextView.setText(currentItem.getItemPrice());
            Picasso.get().load(currentItem.getItemImageUrl()).into(holder.itemImageView);
        }
        holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));

        holder.increaseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setQuantity(currentItem.getQuantity() + 1);
                holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));

                double numericPrice = Double.parseDouble(currentItem.getItemPrice().replace("$", ""));
                double totalItemPrice = numericPrice * currentItem.getQuantity();
                holder.totalItemPriceTextView.setText("$" + String.format("%.2f", totalItemPrice));
                updateTotalPrices();
            }
        });

        holder.decreaseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = currentItem.getQuantity() - 1;
                if (newQuantity >= 0) {
                    currentItem.setQuantity(newQuantity);
                    holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));

                    double numericPrice = Double.parseDouble(currentItem.getItemPrice().replace("$", ""));
                    double totalItemPrice = numericPrice * currentItem.getQuantity();
                    holder.totalItemPriceTextView.setText("$" + String.format("%.2f", totalItemPrice));
                    updateTotalPrices();
                }
            }
        });
        holder.removeItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = holder.getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    cartItems.remove(itemPosition);
                    notifyItemRemoved(itemPosition);
                    updateTotalPrices();
                }
            }
        });

        double numericPrice = Double.parseDouble(currentItem.getItemPrice().replace("$", ""));
        double totalItemPrice = numericPrice * currentItem.getQuantity();
        holder.totalItemPriceTextView.setText("$" + String.format("%.2f", totalItemPrice));

        updateTotalPrices();
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, itemPriceTextView, quantityTextView;
        ImageView itemImageView, increaseImageView, decreaseImageView, removeItemImageView;
        TextView totalItemPriceTextView, subTotalTextView, deliveryChargesTextView, totalChargesTextView;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.cartItemName);
            itemPriceTextView = itemView.findViewById(R.id.cartItemPrice);
            itemImageView = itemView.findViewById(R.id.cartItemImage);
            increaseImageView = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseImageView = itemView.findViewById(R.id.decreaseQuantityButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalItemPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            removeItemImageView = itemView.findViewById(R.id.removeItemImageView);
            subTotalTextView = itemView.findViewById(R.id.subTotalTextView); // Add this line
            deliveryChargesTextView = itemView.findViewById(R.id.deliveryChargesTextView); // Add this line
            totalChargesTextView = itemView.findViewById(R.id.totalChargesTextView); // Add this line
        }
    }

    public void updateTotalPrices() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += Double.parseDouble(item.getItemPrice().replace("$", "")) * item.getQuantity();

        }
        if (cartItems.isEmpty()) {
            subTotalTextView.setText("$0.00");
            deliveryChargesTextView.setText("$0.00");
            totalChargesTextView.setText("$0.00");
        } else if (subtotal == 0){
            subTotalTextView.setText("$0.00");
            deliveryChargesTextView.setText("$0.00");
            totalChargesTextView.setText("$0.00");
        } else {
            double deliveryCharges = 10.0;
            double grandTotal = subtotal + deliveryCharges;
            subTotalTextView.setText(String.format("$%.2f", subtotal));
            deliveryChargesTextView.setText(String.format("$%.2f", deliveryCharges));
            totalChargesTextView.setText(String.format("$%.2f", grandTotal));
        }
    }
    public void updateCartItems(List<CartItem> updatedCartItems) {
        cartItems = updatedCartItems;
        notifyDataSetChanged();
        updateTotalPrices();
    }

}
