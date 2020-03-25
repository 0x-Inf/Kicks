package com.diablo.jayson.kicksv1.UI.KickSelect;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.R;

public class KickSelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_selected);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Kick kick = (Kick) bundle.get("kick");
        assert kick != null;
        Toast.makeText(this, kick.getKickName(), Toast.LENGTH_LONG).show();
    }
}
