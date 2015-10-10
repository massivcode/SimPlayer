package com.example.massivcode.simplayer.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.massivcode.simplayer.Fragment.MainFragment;
import com.example.massivcode.simplayer.Fragment.PlayerFragment;
import com.example.massivcode.simplayer.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName() ;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    Fragment mMainFragment;
    Fragment mPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mPlayerFragment = new PlayerFragment();
        mMainFragment = new MainFragment();

        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_container, mMainFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mini_player_album_art_iv:
            case R.id.mini_player_artist_tv:
            case R.id.mini_player_title_tv:
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                mFragmentTransaction.replace(R.id.main_container, mPlayerFragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
                break;

            case R.id.mini_player_previous_btn:
                Toast.makeText(MainActivity.this, "이전 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mini_player_play_btn:
                Toast.makeText(MainActivity.this, "재생 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mini_player_next_btn:
                Toast.makeText(MainActivity.this, "다음 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
