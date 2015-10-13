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
import java.util.Map;

/**
 * Created by massivCode on 2015-10-10.
 */
public class MusicService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_RESUME = "ACTION_RESUME";
    private static final String TAG = MusicService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }

    private MediaPlayer mMediaPlayer;
    private Uri mCurrentUri = null;
    private String mAction = null;
    private Map<Uri, MusicInfo> mDataMap;

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


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mAction = intent.getAction();
        if(!mAction.equals(ACTION_PAUSE)) {
            mCurrentUri = intent.getData();
        }

        switch (mAction) {
            case ACTION_START:
                break;
            case ACTION_PLAY:
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mDataMap.get(mCurrentUri).getUri());
                        mMediaPlayer.prepare();

                        if(communicator != null) {
                            communicator.transferData(getCurrentInfo());
                        }

                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(getApplicationContext(), mDataMap.get(mCurrentUri).getUri());
                        mMediaPlayer.prepare();
                        if(communicator != null) {
                            communicator.transferData(getCurrentInfo());
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
        return  mDataMap.get(mCurrentUri);
    }

    public interface CurrentInfoCommunicator {
        public void transferData(MusicInfo info);
    }

    public CurrentInfoCommunicator communicator = null;

    public void setOnCurrentInfoCommunicator(CurrentInfoCommunicator listener) {
        communicator = listener;
    }




}
