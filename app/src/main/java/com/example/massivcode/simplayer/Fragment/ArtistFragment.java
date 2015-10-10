package com.example.massivcode.simplayer.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.massivcode.simplayer.Adapter.ArtistAdapter;
import com.example.massivcode.simplayer.R;

/**
 * Created by massivCode on 2015-10-10.
 */
public class ArtistFragment extends Fragment {

    private ListView mListView;
    private ArtistAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        mListView = (ListView)view.findViewById(R.id.artist_lv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] projection = new String[] {
                MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, projection, null, null, null);

        mAdapter = new ArtistAdapter(getActivity().getApplicationContext(), cursor, true);


        mListView.setAdapter(mAdapter);
    }
}
