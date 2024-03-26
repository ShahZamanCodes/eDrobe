package com.example.edrobe;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edrobe.CartItem;
import com.example.edrobe.CartManager;
import com.example.edrobe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class ItemDetailFragment extends Fragment {

    private TextView itemNameTextView, itemPriceTextView, itemDesriptionTextView;
    private ImageView itemImageView, backButton;
    private Button addToCartButton,buyNowButton;

    private CartItem currentItem;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        itemNameTextView = rootView.findViewById(R.id.itemNameDetail);
        itemPriceTextView = rootView.findViewById(R.id.itemPriceDetail);
        itemImageView = rootView.findViewById(R.id.itemImageDetail);
        addToCartButton = rootView.findViewById(R.id.addToCartButton);
        itemDesriptionTextView = rootView.findViewById(R.id.itemDescriptionDetail);
        buyNowButton = rootView.findViewById(R.id.buyNowButton);
        backButton = rootView.findViewById(R.id.itemDetailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String imageUrl = args.getString("imageUrl");
            String itemName = args.getString("itemName");
            String itemPrice = args.getString("itemPrice");
            String itemDescription = args.getString("itemDescription");

            currentItem = new CartItem(itemName, itemPrice, imageUrl);


            Picasso.get().load(imageUrl).into(itemImageView);
            itemNameTextView.setText(itemName);
            itemPriceTextView.setText(itemPrice);
            itemDesriptionTextView.setText(itemDescription);

            addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCart();
                    }
                });
            buyNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart();
                    Fragment newFragment = new CartFragment();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                    bottomNavigationView.setSelectedItemId(R.id.navCart);
                }
            });
        }
        return rootView;
    }
    private void addToCart() {
        if (currentItem != null) {
            CartManager cartManager = CartManager.getInstance();
            cartManager.addItemToCart(currentItem);
            if (addToCartButton.isPressed()){
                Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No Item Added to Cart", Toast.LENGTH_SHORT).show();
        }
    }
}
