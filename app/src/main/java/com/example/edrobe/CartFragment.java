package com.example.edrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List; // Import List

public class CartFragment extends Fragment {
    private ImageView backButton;
    private Button checkoutButton;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView subTotalTextView, deliveryChargesTextView, totalChargesTextView;
    private List<CartItem> cartItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = rootView.findViewById(R.id.cartList);
        backButton = rootView.findViewById(R.id.cartBackButton);
        subTotalTextView = rootView.findViewById(R.id.subTotalTextView);
        deliveryChargesTextView = rootView.findViewById(R.id.deliveryChargesTextView);
        totalChargesTextView = rootView.findViewById(R.id.totalChargesTextView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            }
        });



        checkoutButton = rootView.findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(view -> {
            CartManager cartManager = CartManager.getInstance();
            List<CartItem> cartItems = cartManager.getCartItems();

            if (!cartItems.isEmpty()) {
                double subtotal = calculateSubtotal(cartItems);
                double deliveryCharges = calculateDeliveryCharges();
                double totalCharges = calculateTotalCharges(cartItems);

                Bundle args = new Bundle();
                args.putDouble("SUBTOTAL", subtotal);
                args.putDouble("DELIVERY_CHARGES", deliveryCharges);
                args.putDouble("TOTAL_CHARGES", totalCharges);

                Fragment newFragment = new BuyingPhaseFragment();
                newFragment.setArguments(args);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(requireContext(), "Empty Cart!", Toast.LENGTH_SHORT).show();
            }
        });

        // Retrieve the list of items from the CartManager
        CartManager cartManager = CartManager.getInstance();
        cartItems = cartManager.getCartItems();

        // Set up the RecyclerView with the CartAdapter using the List<CartItem>
        cartAdapter = new CartAdapter(cartItems, subTotalTextView, deliveryChargesTextView, totalChargesTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(layoutManager);


        return rootView;
    }
    private double calculateSubtotal(List<CartItem> cartItems) {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            double numericPrice = Double.parseDouble(item.getItemPrice().replace("$", ""));
            subtotal += numericPrice * item.getQuantity();
        }
        return subtotal;
    }

    private double calculateDeliveryCharges() {
        return 10.0; // Change this to your actual delivery charges calculation
    }

    private double calculateTotalCharges(List<CartItem> cartItems) {
        double subtotal = calculateSubtotal(cartItems);
        double deliveryCharges = calculateDeliveryCharges();
        return subtotal + deliveryCharges;
    }
    public void clearCartItems() {
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
        updateTotalPrices();
    }

    private void updateTotalPrices() {
        double subtotal = calculateSubtotal(cartItems);
        double deliveryCharges = calculateDeliveryCharges();
        double totalCharges = calculateTotalCharges(cartItems);

        subTotalTextView.setText(String.format("$%.2f", subtotal));
        deliveryChargesTextView.setText(String.format("$%.2f", deliveryCharges));
        totalChargesTextView.setText(String.format("$%.2f", totalCharges));
    }

}
