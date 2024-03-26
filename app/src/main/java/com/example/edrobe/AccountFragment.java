package com.example.edrobe;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;
import java.util.concurrent.Executor;


public class AccountFragment extends Fragment {
    TextView mAccFullName, mAccEmail, mAccPhoneNo, mAccAddress;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userId;
    Button logoutBtn;
    ImageView mAccProfilePic, mAccEditProfilePic;
    TextView backBtn;
    StorageReference storageReference;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mAccFullName = view.findViewById(R.id.accFullName);
        mAccEmail = view.findViewById(R.id.accEmail);
        mAccPhoneNo = view.findViewById(R.id.accPhoneNo);
        mAccAddress = view.findViewById(R.id.accAddress);
        mAccProfilePic = view.findViewById(R.id.accProfilePic);
        mAccEditProfilePic = view.findViewById(R.id.accEditProfilePic);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

        logoutBtn = view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        backBtn = view.findViewById(R.id.accBackButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                bottomNavigationView.setSelectedItemId(R.id.navHome);
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        mAccProfilePic.setImageURI(selectedImageUri);
                        uploadImageToFirebaseStorage(selectedImageUri);
                    }
                });
        mAccEditProfilePic.setOnClickListener(v -> openGallery());

        fStore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String fullname = documentSnapshot.getString("Full Name");
                String email = documentSnapshot.getString("Email");
                String phone = documentSnapshot.getString("Phone No");
                String address = documentSnapshot.getString("Address");
                mAccFullName.setText(fullname);
                mAccEmail.setText(email);
                mAccPhoneNo.setText(phone);
                mAccAddress.setText(address);

                String imageUrl = documentSnapshot.getString("profileImageUrl");
                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(mAccProfilePic);
                }
            }
        }).addOnFailureListener(e -> {
            String errorMessage = "Error fetching data: " + e.getMessage();
            mAccFullName.setText(errorMessage);
            mAccEmail.setText(errorMessage);
            mAccPhoneNo.setText(errorMessage);
            mAccAddress.setText(errorMessage);

        });
        return view;
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(downloadUri -> {
                                String imageUrl = downloadUri.toString();
                                updateImageUrlInFirestore(imageUrl);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getActivity(), "Error Getting Image URL", Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error Uploading Image", Toast.LENGTH_SHORT).show();
                });
    }
    private void updateImageUrlInFirestore(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        userRef.update("profileImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Image URL updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error updating image URL", Toast.LENGTH_SHORT).show();
                });
    }
}
