package com.example.massivcode.simplayer.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by massivCode on 2015-10-11.
 *
 *
 *  private long _id;
    private Uri uri;
    private String artist;
    private String title;
    private String album;
    private byte[] albumArt;
     private String duration;
 *
 */
public class MusicInfoUtil {

    private static final String TAG = MusicInfoUtil.class.getSimpleName();
    public static String[] projection = new String[]{MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION};

    /**
     * 해당 포지션의 cursor를 받아서 뮤직인포를 얻은 후 반환한다.
     *
     * @param context
     * @param cursor
     * @return
     */
    public static MusicInfo getSelectedMusicInfo(Context context, Cursor cursor) {

        MusicInfo musicInfo = null;

        long _id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        Uri uri =  Uri.parse("content://media/external/audio/media/" + _id);
        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);

        byte[] albumArt =  retriever.getEmbeddedPicture();

//        long _id, Uri uri, String artist, String title, String album, byte[] albumArt, String duration
        musicInfo = new MusicInfo(_id, uri, artist, title, album, albumArt, duration);

        return musicInfo;

    }

    public static Map<Uri, MusicInfo> getAllMusicInfo(Context context) {
        Map<Uri, MusicInfo> map = new HashMap<>();
        long startTime = System.currentTimeMillis();
        Log.d(TAG, "메소드 started : " + startTime);
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        while(cursor.moveToNext()) {
//            long _id, Uri uri, String artist, String title, String album, String duration

            long _id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
            Uri uri =  Uri.parse("content://media/external/audio/media/" + _id);
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            MusicInfo musicInfo = new MusicInfo(_id, uri, artist, title, album, duration);
            map.put(uri, musicInfo);

        }

        long endTime = System.currentTimeMillis();
        Log.d(TAG, "메소드 소요시간 : " + (endTime - startTime));
        Log.d(TAG, "메소드 ended : " + endTime);
        cursor.close();

        return map;
    }

    public static Bitmap getBitmap( Context context, byte[] albumArt, int quality) {
        // Bitmap 샘플링
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = quality; // 2의 배수

        Bitmap bitmap = null;
        if (null != albumArt) {
            bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length, options);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image_audiotrack);
        }

        // id 로부터 bitmap 생성
        return bitmap;
    }

    public static String getTime(String duration) {

        long milliSeconds = Long.parseLong(duration);
        int totalSeconds = (int)(milliSeconds / 1000);

        int hour = totalSeconds / 3600;
        int minute = (totalSeconds - (hour * 3600)) / 60;
        int second = (totalSeconds - ((hour * 3600) + (minute * 60)));


        return formattedTime(hour, minute, second);
    }

    private static String formattedTime(int hour, int minute, int second) {
        String result = "";

        if(hour > 0) {
            result = hour + ":";
        }

        if(minute >= 10) {
            result = result + minute + ":";
        } else {
            result = result + "0" + minute + ":";
        }

        if(second >= 10) {
            result = result + second;
        } else {
            result = result + "0" + second;
        }

        return result;
    }

}
