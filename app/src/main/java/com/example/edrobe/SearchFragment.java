package com.example.edrobe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private EditText searchEditText;
    private ImageView searchBackButton;
    private ImageView searchButton;
    private List<ItemSearch> searchResults;
    private FirebaseFirestore firestore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults = new ArrayList<>();
        adapter = new SearchResultAdapter(getContext(), searchResults);
        recyclerView.setAdapter(adapter);
        searchBackButton = view.findViewById(R.id.searchBackButton);
        searchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navHome);
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        adapter.setOnItemClickListener(item -> {
            ItemDetailFragment newFragment = new ItemDetailFragment();
            Bundle args = new Bundle();
            args.putString("imageUrl", item.getImageUrl());
            args.putString("itemName", item.getName());
            args.putString("itemPrice", item.getPrice());
            args.putString("itemDescription", item.getDescription());
            newFragment.setArguments(args);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            Toast.makeText(getContext(), "Clicked on: " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString().trim();
            if (!searchText.isEmpty()) {
                performSearch(searchText);
            }
        });

        return view;
    }

    private void performSearch(String searchText) {
        Log.d("SearchFragment", "Performing search for: " + searchText);
        searchResults.clear(); // Clear previous results before starting the new search

        List<String> collections = Arrays.asList("mensclothing", "womensclothing", "kidsclothing", "watches", "shoes");

        // Counter to keep track of completed queries
        AtomicInteger completedQueries = new AtomicInteger(0);

        for (String collectionName : collections) {
            queryCollectionForSearch(collectionName, searchText, completedQueries, collections.size());
        }
    }

    private void queryCollectionForSearch(String collectionName, @NonNull String searchText, AtomicInteger completedQueries, int totalQueries) {
        Log.d("SearchFragment", "Querying collection: " + collectionName + " for: " + searchText);
        firestore.collection(collectionName)
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ItemSearch item = document.toObject(ItemSearch.class);
                            if (containsKeyword(item.getName().toLowerCase(), searchText.toLowerCase())) {
                                searchResults.add(item);
                                Log.d("SearchFragment", "Added item: " + item.getName());
                            }
                        }

                        // Check if all queries are completed
                        if (completedQueries.incrementAndGet() == totalQueries) {
                            Log.d("SearchFragment", "Total items added: " + searchResults.size());
                            adapter.notifyDataSetChanged(); // Notify after all queries are completed
                        }
                    } else {
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean containsKeyword(String itemName, String keyword) {
        return itemName.toLowerCase().contains(keyword.toLowerCase());
    }
}
