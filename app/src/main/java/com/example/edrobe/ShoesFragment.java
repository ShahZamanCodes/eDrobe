package com.example.edrobe;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ShoesFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Item> itemsArrayList;
    ItemAdapter itemsAdapter;
    FirebaseFirestore fstore;
    ImageView backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoes, container, false);

        recyclerView = rootView.findViewById(R.id.itemsRecyclerView3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        fstore = FirebaseFirestore.getInstance();
        itemsArrayList = new ArrayList<Item>();
        itemsAdapter = new ItemAdapter(requireContext(),itemsArrayList);
        recyclerView.setAdapter(itemsAdapter);
        backButton = rootView.findViewById(R.id.shoesBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new CategoriesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
            }
        });

        EventChangeListner();
        return rootView;
    }

    private void EventChangeListner(){
        fstore.collection("shoes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.e("Firestore Error", error.getMessage());
                    return;
                }

                itemsArrayList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    if (doc.exists()) {
                        Log.e("ABC", "ABC");
                        Item data = doc.toObject(Item.class);
                        itemsArrayList.add(data);
                        Log.d("Image URL", data.getImageUrl());
                    } else {
                        Log.e("Firestore Error", "Document does not exist");
                    }
                }
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

}