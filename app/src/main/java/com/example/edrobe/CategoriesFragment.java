package com.example.edrobe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private List<Categories> categories;
    private RecyclerView recyclerView;
    private CategoriesAdaptor categoriesAdaptor;
    ImageView imageView;

    public CategoriesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        categories = generateCategories();
        imageView = rootView.findViewById(R.id.searchBackButton);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  requireActivity().getSupportFragmentManager().popBackStack();
                  BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                  bottomNavigationView.setSelectedItemId(R.id.navHome);
            }
        });

        recyclerView = rootView.findViewById(R.id.categoriesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesAdaptor = new CategoriesAdaptor(categories, requireContext());
        recyclerView.setAdapter(categoriesAdaptor);
        return rootView;
    }

    private List<Categories> generateCategories() {
        List<Categories> categories = new ArrayList<>();
        categories.add(new Categories("MEN'S CLOTHING", R.drawable.tshirt));
        categories.add(new Categories("WOMEN'S CLOTHING", R.drawable.womenshirt));
        categories.add(new Categories("KID'S CLOTHING", R.drawable.kidshirt));
        categories.add(new Categories("SHOES", R.drawable.shoes));
        categories.add(new Categories("WATCHES", R.drawable.watch));

        return categories;
    }
}
