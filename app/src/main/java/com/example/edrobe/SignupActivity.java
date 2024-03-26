package com.example.edrobe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    TextView loginButton, backButton;
    Button signupButton;
    EditText mFullName, mEmail, mPassword, mPhoneNo, mAddress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        loginButton = findViewById(R.id.loginButton);
        backButton  = findViewById(R.id.backButton);
        mFullName       = findViewById(R.id.signupFullName);
        mEmail    = findViewById(R.id.signupEmail);
        mPassword     = findViewById(R.id.signupPassword);
        mPhoneNo       = findViewById(R.id.signupPhoneNo);
        mAddress     = findViewById(R.id.signupAddress);
        signupButton= findViewById(R.id.signupButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = mFullName.getText().toString();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String phoneNo = mPhoneNo.getText().toString();
                String address = mAddress.getText().toString();


                if (TextUtils.isEmpty(mFullName.getText().toString())) {
                    mFullName.setError("Enter Full Name!");
                    return;
                }
                if (TextUtils.isEmpty(mEmail.getText().toString())) {
                    mEmail.setError("Enter Username!");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    mEmail.setError("Enter Valid Email!");
                    return;
                }
                if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mPassword.setError("Enter Password!");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNo.getText().toString())) {
                    mPhoneNo.setError("Enter Phone Number!");
                    return;
                }
                if (TextUtils.isEmpty(mAddress.getText().toString())) {
                    mAddress.setError("Enter Address!");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Account Created Successfully!",
                                            Toast.LENGTH_SHORT).show();
                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Full Name", fullName);
                                    user.put("Email", email);
                                    user.put("Password", password);
                                    user.put("Phone No", phoneNo);
                                    user.put("Address", address);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG, "OnSuccess: User Profile is Created" + userID);
                                        }
                                    });
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignupActivity.this, "Authentication failed!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}