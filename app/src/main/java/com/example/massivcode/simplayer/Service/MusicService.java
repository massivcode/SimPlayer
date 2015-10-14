package com.example.massivcode.simplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.Util.MusicInfoUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by massivCode on 2015-10-10.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PLAY_NEXT = "ACTION_PLAY_NEXT";
    public static final String ACTION_PLAY_PREVIOUS = "ACTION_PLAY_PREVIOUS";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_RESUME = "ACTION_RESUME";
    private static final String TAG = MusicService.class.getSimpleName();

    private boolean isReady = false;

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.pause();
        mp.reset();

        try {
            if(mCurrentPosition < getCurrentPlaylistSize()) {
                mp.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition + 1));
            } else {
                mp.setDataSource(getApplicationContext(), mCurrentPlaylist.get(0));
            }

            mp.prepare();
            isReady = true;
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }

    private MediaPlayer mMediaPlayer;
    private String mAction = null;
    private Map<Uri, MusicInfo> mDataMap;
    private List<Uri> mCurrentPlaylist;
    private int mCurrentPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mDataMap = MusicInfoUtil.getAllMusicInfo(getApplicationContext());
            }
        }).start();

        mMediaPlayer.setOnCompletionListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mAction = intent.getAction();

        switch (mAction) {
            case ACTION_PLAY:
                mCurrentPlaylist = intent.getParcelableArrayListExtra("list");
                mCurrentPosition = intent.getIntExtra("position", 0);
                break;
            case ACTION_PLAY_NEXT:
            case ACTION_PLAY_PREVIOUS:
                mCurrentPosition = intent.getIntExtra("position", 0);
                break;
        }


        switch (mAction) {
            case ACTION_START:
                break;
            case ACTION_PLAY:
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;

                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }

                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;
                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ACTION_PAUSE:
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
                break;
            case ACTION_RESUME:
                break;
            case ACTION_PLAY_NEXT:
                if(mMediaPlayer.isPlaying()) {

                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;
                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;
                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case ACTION_PLAY_PREVIOUS:
                if(mMediaPlayer.isPlaying()) {

                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;
                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mCurrentPlaylist.get(mCurrentPosition));
                        mMediaPlayer.prepare();
                        isReady = true;
                        if(communicatorToMainActivity != null) {
                            communicatorToMainActivity.transferData(getCurrentInfo());
                        }
                        if(communicatorToPlayerActivity != null) {
                            communicatorToPlayerActivity.transferData(getCurrentInfo());
                        }
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }




        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public MediaPlayer getMediaPlayer() {
        if(mMediaPlayer != null) {
            return mMediaPlayer;
         } else {
            return null;
        }
    }

    public MusicInfo getCurrentInfo() {
        if(mCurrentPlaylist != null) {
            return  mDataMap.get(mCurrentPlaylist.get(mCurrentPosition));
        } else {
            return null;
        }

    }

    public interface CurrentInfoCommunicator {
        void transferData(MusicInfo info);
    }

    public CurrentInfoCommunicator communicatorToMainActivity = null;
    public CurrentInfoCommunicator communicatorToPlayerActivity = null;

    public void setOnCurrentInfoToMainActivity(CurrentInfoCommunicator listener) {
        communicatorToMainActivity = listener;
    }
    public void setOnCurrentInfoToPlayerActivity(CurrentInfoCommunicator listener) {
        communicatorToPlayerActivity = listener;
    }

    public boolean isReady() {
        return isReady;
    }

    public Map<Uri, MusicInfo> getDataMap() {
        return mDataMap;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public int getCurrentPlaylistSize() {
        return mCurrentPlaylist.size()-1;
    }
}
