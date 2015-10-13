package com.example.massivcode.simplayer.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.Fragment.MainFragment;
import com.example.massivcode.simplayer.R;
import com.example.massivcode.simplayer.Service.MusicService;
import com.example.massivcode.simplayer.Util.MusicInfoUtil;
import com.example.massivcode.simplayer.listener.FragmentCommunicator;
import com.example.massivcode.simplayer.listener.MediaPlayerStateToFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MusicService.CurrentInfoCommunicator {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent mServiceIntent;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    Fragment mMainFragment;
    Fragment mPlayerFragment;

    private MusicService mMusicService;
    private MusicService.CurrentInfoCommunicator mListener;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mMusicService = binder.getService();
            mMusicService.setOnCurrentInfoCommunicator(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public FragmentCommunicator fragmentCommunicator;
    public MediaPlayerStateToFragment mediaPlayerStateToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceIntent = new Intent(MainActivity.this, MusicService.class);
        bindService(mServiceIntent, mConnection, BIND_AUTO_CREATE);

        mFragmentManager = getSupportFragmentManager();
        mMainFragment = new MainFragment();

        if(savedInstanceState == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.add(R.id.main_container, mMainFragment);
            mFragmentTransaction.commit();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mini_player_album_art_iv:
            case R.id.mini_player_artist_tv:
            case R.id.mini_player_title_tv:
//                mFragmentTransaction = mFragmentManager.beginTransaction();
//                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                mFragmentTransaction.replace(R.id.main_container, mPlayerFragment);
//                mFragmentTransaction.addToBackStack(null);
//                mFragmentTransaction.commit();
//                if(mCurrentInfo != null) {
//                    Intent intent = new Intent(this, PlayerActivity.class);
//                    intent.putExtra("info", mCurrentInfo);
//                    startActivity(intent);
//                }
                break;

            case R.id.mini_player_previous_btn:
                Toast.makeText(MainActivity.this, "이전 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mini_player_play_btn:
                Intent pauseIntent = new Intent(MainActivity.this, MusicService.class);
                pauseIntent.setAction(MusicService.ACTION_PAUSE);
                startService(pauseIntent);
                break;
            case R.id.mini_player_next_btn:
                Toast.makeText(MainActivity.this, "다음 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
                break;

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicInfo info = MusicInfoUtil.getSelectedMusicInfo(MainActivity.this, (Cursor) parent.getAdapter().getItem(position));
        Uri uri = info.getUri();
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        intent.setData(uri);
        startService(intent);

        if(mediaPlayerStateToFragment != null) {
            mediaPlayerStateToFragment.passConditionToFragment(mMusicService.getMediaPlayer().isPlaying());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    public void transferData(MusicInfo info) {
        if (fragmentCommunicator != null) {
            fragmentCommunicator.passDataToFragment(info);
        }
    }
}
