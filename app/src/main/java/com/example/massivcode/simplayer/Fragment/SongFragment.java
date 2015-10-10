package com.example.massivcode.simplayer.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.massivcode.simplayer.Adapter.SongAdapter;
import com.example.massivcode.simplayer.R;

/**
 * Created by massivCode on 2015-10-10.
 */
public class SongFragment extends Fragment {

    private ListView mListView;
    private SongAdapter mAdapter;
    private LinearLayout mPlayAllButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        mListView = (ListView) view.findViewById(R.id.song_lv);
        mPlayAllButton = (LinearLayout) view.findViewById(R.id.song_play_ll);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] projection = new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        mAdapter = new SongAdapter(getActivity().getApplicationContext(), cursor, true);
        mListView.setAdapter(mAdapter);

    }

}
