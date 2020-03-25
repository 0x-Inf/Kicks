package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.R;

public class KickSeeAllActivity extends AppCompatActivity {

    private KickSelectViewModel viewModel;
    private static final String TAG = KickSeeAllActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_see_all);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String categoryId = bundle.getString("categoryId");
        String categoryName = bundle.getString("categoryName");

        viewModel = new ViewModelProvider(this).get(KickSelectViewModel.class);

        viewModel.setCategoryId(categoryId);
        viewModel.setCategoryName(categoryName);


        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.attend_activity_fragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        KicksSeeAllFragment seeAllFragment = new KicksSeeAllFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.kicks_see_all_fragment_container, seeAllFragment, "findThisFragment")
                .commit();
    }
}
