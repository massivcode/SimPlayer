package com.example.massivcode.simplayer.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.Fragment.PlayerFragment;
import com.example.massivcode.simplayer.R;

/**
 * Created by junsuk on 2015. 10. 12..
 */
public class PlayerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        MusicInfo info = null;
        if (getIntent() != null) {
            info = getIntent().getParcelableExtra("info");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlayerFragment.getInstance(info))
                .commit();
    }
}
