package com.diablo.jayson.kicksv1.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.SignUp.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private EditText mEmailAddress;
    private EditText mPassword;
    private TextView signUpTextView, forgotPasswordTextView;
    private ExtendedFloatingActionButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mEmailAddress = findViewById(R.id.email_addresss_edit_text);
        mPassword = findViewById(R.id.passWord_edit_text);
        mSignInButton = findViewById(R.id.sign_in_button);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        signUpTextView = findViewById(R.id.signUpTextView);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = mEmailAddress.getText().toString();
                if (emailAddress.isEmpty()) {
                    mEmailAddress.setError("Input your Email");
                } else {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(getApplicationContext(), "Check your email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailAddress.getText().toString();
                String password = mPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.Check details",
                                        Toast.LENGTH_SHORT).show();
                                // ...
                            }

                            // ...
                        });


            }
        });


    }
}
