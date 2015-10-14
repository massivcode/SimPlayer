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
import android.util.Log;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, MusicService.CurrentInfoCommunicator {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent mServiceIntent;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    Fragment mMainFragment;

    private MusicService mMusicService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mMusicService = binder.getService();
            mMusicService.setOnCurrentInfoToMainActivity(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public FragmentCommunicator currentMusicInfoToMiniPlayerInfo;
    public MediaPlayerStateToFragment currentMediaplayerStateToMiniPlayerController;

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
                if(mMusicService.getCurrentInfo() != null) {
                    Intent intent = new Intent(this, PlayerActivity.class);
                    intent.putExtra("info", mMusicService.getCurrentInfo());
                    startActivity(intent);
                }
                break;

            case R.id.mini_player_previous_btn:
                Toast.makeText(MainActivity.this, "이전 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();

                if(mMusicService != null & mMusicService.isReady()) {

                    int position = mMusicService.getCurrentPosition();

                    if(position > 0) {
                        position -= 1;
                    } else {
                        position = mMusicService.getCurrentPlaylistSize();
                    }

                    Intent nextIntent = new Intent(MainActivity.this, MusicService.class);
                    nextIntent.setAction(MusicService.ACTION_PLAY_PREVIOUS);
                    nextIntent.putExtra("position", position);
                    startService(nextIntent);
                }


                break;
            case R.id.mini_player_play_btn:
                if(currentMediaplayerStateToMiniPlayerController != null) {
                    if (mMusicService != null & mMusicService.isReady()) {
                        if(mMusicService.getMediaPlayer().isPlaying()) {
                            currentMediaplayerStateToMiniPlayerController.passConditionToFragment(false);
                        } else {
                            currentMediaplayerStateToMiniPlayerController.passConditionToFragment(true);
                        }

                    }
                }

                if(mMusicService != null & mMusicService.isReady()) {
                    Intent pauseIntent = new Intent(MainActivity.this, MusicService.class);
                    pauseIntent.setAction(MusicService.ACTION_PAUSE);
                    startService(pauseIntent);
                }
                break;
            case R.id.mini_player_next_btn:

                if(mMusicService != null & mMusicService.isReady()) {

                    int position = mMusicService.getCurrentPosition();

                    Log.d(TAG, "list size = " + mMusicService.getCurrentPlaylistSize());
                    if(position < mMusicService.getCurrentPlaylistSize()) {
                        position += 1;
                    } else {
                        position = 0;
                    }

                    Intent nextIntent = new Intent(MainActivity.this, MusicService.class);
                    nextIntent.setAction(MusicService.ACTION_PLAY_NEXT);
                    nextIntent.putExtra("position", position);
                    startService(nextIntent);
                }

                break;
            case R.id.song_play_ll:
                Intent playAllIntent = new Intent(MainActivity.this, MusicService.class);
                playAllIntent.setAction(MusicService.ACTION_PLAY);
                playAllIntent.putExtra("list", MusicInfoUtil.makePlaylist(mMusicService.getDataMap()));
                playAllIntent.putExtra("position", 0);
                startService(playAllIntent);

                if(currentMediaplayerStateToMiniPlayerController != null) {
                    if (mMusicService != null) {
                        currentMediaplayerStateToMiniPlayerController.passConditionToFragment(true);

                    }
                }

                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicInfo info = MusicInfoUtil.getSelectedMusicInfo(MainActivity.this, (Cursor) parent.getAdapter().getItem(position));
        ArrayList<Uri> list = MusicInfoUtil.makePlaylist(info);
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        intent.putExtra("list", list);
        intent.putExtra("position", 0);
        startService(intent);

        if(currentMediaplayerStateToMiniPlayerController != null) {
            if (mMusicService != null) {
                currentMediaplayerStateToMiniPlayerController.passConditionToFragment(true);

            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    public void transferData(MusicInfo info) {
        if (currentMusicInfoToMiniPlayerInfo != null) {
            currentMusicInfoToMiniPlayerInfo.passDataToFragment(info);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(currentMediaplayerStateToMiniPlayerController != null) {
            if(mMusicService != null) {

                currentMediaplayerStateToMiniPlayerController.passConditionToFragment(mMusicService.getMediaPlayer().isPlaying());
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentMediaplayerStateToMiniPlayerController != null) {
            if(mMusicService != null) {
                currentMediaplayerStateToMiniPlayerController.passConditionToFragment(mMusicService.getMediaPlayer().isPlaying());

            }

        }
    }
}
