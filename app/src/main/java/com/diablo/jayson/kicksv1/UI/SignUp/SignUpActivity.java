package com.diablo.jayson.kicksv1.UI.SignUp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;

public class SignUpActivity extends AppCompatActivity implements SignUpOne.onNextClickedListener, SignUpTwo.onNext2ClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.signupfragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        SignUpOne secondSignUp = new SignUpOne();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.signupfragment_container, secondSignUp)
                .commit();


    }

    @Override
    public void onItemsAdded(User user) {
        SignUpTwo secondFragment = new SignUpTwo();

        Bundle args = new Bundle();
        args.putSerializable(SignUpTwo.EXTRA_INFO, user);
        secondFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.signupfragment_container, secondFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onUserItemsAdded(String amail, String password) {
        startActivity(new Intent(this, MainActivity.class));

    }
}
