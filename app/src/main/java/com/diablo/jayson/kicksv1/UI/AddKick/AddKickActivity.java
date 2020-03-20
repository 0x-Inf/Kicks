package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.R;

public class AddKickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kick);
//        Objects.requireNonNull(getSupportActionBar()).hide();


        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.add_kick_fragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        AddKick1Fragment addKick = new AddKick1Fragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.add_kick_fragment_container, addKick)
                .commit();

    }
}
