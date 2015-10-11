package com.example.massivcode.simplayer.Util;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;

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

}
