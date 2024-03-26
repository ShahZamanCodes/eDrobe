package com.example.edrobe;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BuyingPhaseFragment extends Fragment {
    private Button confirmButton;
    private RadioButton paymentMethodRadio;
    private TextView customerFullName, customerEmail, customerContact, customerAddress, checkoutSubTotal, checkoutDelivery, checkoutTotal;
    String userId;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    private CartItem currentItem;
    private List<CartItem>cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buying_phase, container, false);

        customerFullName  = rootView.findViewById(R.id.customerInformationFullName);
        customerEmail  = rootView.findViewById(R.id.customerInformationEmail);
        customerContact  = rootView.findViewById(R.id.customerInformationContact);
        customerAddress  = rootView.findViewById(R.id.customerInformationAddress);
        confirmButton = rootView.findViewById(R.id.confirmPurchaseButton);
        paymentMethodRadio = rootView.findViewById(R.id.paymentMethodRadioButton);
        checkoutSubTotal  = rootView.findViewById(R.id.subTotalTextView);
        checkoutDelivery = rootView.findViewById(R.id.deliveryChargesTextView);
        checkoutTotal = rootView.findViewById(R.id.totalChargesTextView);
        cartItems = new ArrayList<>();
        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentMethodRadio.isChecked()){
                    if (getActivity() instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        CartFragment cartFragment = mainActivity.getCartFragment();
                        if (cartFragment != null) {
                            cartFragment.clearCartItems();
                        }
                    }
                    Fragment newFragment = new ThankYouFragment();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else{
                    Toast.makeText(requireContext(), "Select a Payment Method!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            double subtotal = args.getDouble("SUBTOTAL", 0.0);
            double deliveryCharges = args.getDouble("DELIVERY_CHARGES", 0.0);
            double totalCharges = args.getDouble("TOTAL_CHARGES", 0.0);

            checkoutSubTotal.setText(String.format("$%.2f", subtotal));
            checkoutDelivery.setText(String.format("$%.2f", deliveryCharges));
            checkoutTotal.setText(String.format("$%.2f", totalCharges));
        }

        fStore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String fullname = documentSnapshot.getString("Full Name");
                String email = documentSnapshot.getString("Email");
                String phone = documentSnapshot.getString("Phone No");
                String address = documentSnapshot.getString("Address");
                customerFullName.setText(fullname);
                customerEmail.setText(email);
                customerContact.setText(phone);
                customerAddress.setText(address);
            }
        }).addOnFailureListener(e -> {
            String errorMessage = "Error fetching data: " + e.getMessage();
            customerFullName.setText(errorMessage);
            customerEmail.setText(errorMessage);
            customerContact.setText(errorMessage);
            customerAddress.setText(errorMessage);
        });
        return rootView;
    }
}