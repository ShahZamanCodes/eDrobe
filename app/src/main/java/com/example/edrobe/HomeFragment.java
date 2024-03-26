package com.example.edrobe;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {
    private TextView homepageName;
    private ImageView homepageProfileImage;
    private ViewPager2 viewPager;
    private ImageSliderAdapter sliderAdapter;
    private List<Integer> imageList = new ArrayList<>();
    private Timer timer;
    private int currentPage = 0;
    private de.hdodenhof.circleimageview.CircleImageView profilePic;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardItem> cardItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homepageName = rootView.findViewById(R.id.homepageName);
        homepageProfileImage = rootView.findViewById(R.id.homepageProfileImage);
        profilePic = rootView.findViewById(R.id.homepageProfileImage);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navAccount);
                Fragment newFragment = new AccountFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        TextView mensFashion = rootView.findViewById(R.id.mensFashion);
        TextView womensFashion = rootView.findViewById(R.id.womensFashion);
        TextView kidsFashion = rootView.findViewById(R.id.kidsFashion);
        TextView shoesButton = rootView.findViewById(R.id.shoesButton);
        TextView watchesButton = rootView.findViewById(R.id.watchesButton);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

        viewPager = rootView.findViewById(R.id.viewPager);
        sliderAdapter = new ImageSliderAdapter(imageList);
        viewPager.setAdapter(sliderAdapter);
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner2);
        imageList.add(R.drawable.banner3);
        startAutoSlider();

        recyclerView = rootView.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2)); // 2 columns
        cardItems = new ArrayList<>();
        cardAdapter = new CardAdapter(requireContext(), cardItems);
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CardItem cardItem) {

                ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("itemName", cardItem.getName());
                bundle.putString("imageUrl", cardItem.getImageUrl());
                bundle.putString("itemPrice", cardItem.getPrice());
                bundle.putString("itemDescription", cardItem.getDescription());
                itemDetailFragment.setArguments(bundle);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, itemDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        fetchRandomCardItemsFromFirestore();

        fStore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String fullname = documentSnapshot.getString("Full Name");
                homepageName.setText(fullname);

                String imageUrl = documentSnapshot.getString("profileImageUrl");
                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(homepageProfileImage);
                } else {
                    Toast.makeText(requireContext(), "Error fetching image!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            String errorMessage = "Error fetching data: " + e.getMessage();
            homepageName.setText(errorMessage);
        });

        mensFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
            }
        });
        womensFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
                Fragment newFragment = new WomenClothingFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        kidsFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
                Fragment newFragment = new KidsClothing();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        shoesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
                Fragment newFragment = new ShoesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        watchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navCategories);
                Fragment newFragment = new WatchesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

    private void startAutoSlider() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imageList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 3000); // Delay 3 seconds, repeat every 3 seconds
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void fetchRandomCardItemsFromFirestore() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        String[] collectionNames = {"mensclothing", "womensclothing", "kidsclothing", "shoes", "watches"};
        List<String> fetchedItems = new ArrayList<>();

        Random random = new Random();

        for (String collectionName : collectionNames) {
            fStore.collection(collectionName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<QueryDocumentSnapshot> documents = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            documents.add((QueryDocumentSnapshot) document); // Cast to QueryDocumentSnapshot
                        }

                        // Shuffle the documents randomly
                        Collections.shuffle(documents, random);

                        int numDocuments = documents.size();

                        // Fetch 2 random items from the collection, and add them to the cardItems list
                        for (int i = 0; i < 2; i++) {
                            if (i < numDocuments) {
                                QueryDocumentSnapshot document = documents.get(i);
                                String name = document.getString("Name");
                                String imageUrl = document.getString("ImageUrl");

                                if (name != null && imageUrl != null && !fetchedItems.contains(imageUrl)) {
                                    String price = document.getString("Price") != null ? document.getString("Price") : "";
                                    String description = document.getString("Description") != null ? document.getString("Description") : "";
                                    cardItems.add(new CardItem(name, imageUrl, price, description));
                                    fetchedItems.add(imageUrl);
                                }
                            }
                        }

                        cardAdapter.notifyDataSetChanged(); // Notify adapter after adding items
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        Log.e("Firestore Error", "Error fetching data from " + collectionName + ": " + e.getMessage());
                    });
        }
    }
}

