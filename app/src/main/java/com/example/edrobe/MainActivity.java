package com.example.edrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.edrobe.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    CategoriesFragment categoriesFragment = new CategoriesFragment();
    SearchFragment searchFragment = new SearchFragment();
    CartFragment cartFragment = new CartFragment();
    AccountFragment accountFragment = new AccountFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navHome);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navHome){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                } else if (id == R.id.navCategories) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, categoriesFragment).commit();
                } else if (id == R.id.navSearch) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, searchFragment).commit();
                } else if (id == R.id.navCart) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, cartFragment).commit();
                } else if (id == R.id.navAccount) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, accountFragment).commit();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
    public CartFragment getCartFragment() {
        return cartFragment;
    }
}