package com.example.massivcode.simplayer.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.massivcode.simplayer.Activity.MainActivity;
import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.R;
import com.example.massivcode.simplayer.Util.MusicInfoUtil;
import com.example.massivcode.simplayer.listener.FragmentCommunicator;

/**
 * Created by massivCode on 2015-10-10.
 */
public class MiniPlayerInfoFragment extends android.support.v4.app.Fragment implements FragmentCommunicator{

    private static final String TAG = MiniPlayerInfoFragment.class.getSimpleName();
    private ImageView mMiniPlayerAlbumArtImageView;
    private TextView mMiniPlayerTitleTextView, mMiniPlayerArtistTextView;
    private MusicInfo mMusicInfo;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Context context = getActivity();
        ((MainActivity)context).currentMusicInfoToMiniPlayerInfo = this;
        Log.d(TAG, "onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mini_player_info, container, false);
        initView(view);
        Log.d(TAG, "onCreateView");
        return view;
    }

    private void initView(View view) {
        mMiniPlayerAlbumArtImageView = (ImageView)view.findViewById(R.id.mini_player_album_art_iv);
        mMiniPlayerTitleTextView = (TextView)view.findViewById(R.id.mini_player_title_tv);
        mMiniPlayerArtistTextView = (TextView)view.findViewById(R.id.mini_player_artist_tv);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMiniPlayerAlbumArtImageView.setOnClickListener((View.OnClickListener) getActivity());
        mMiniPlayerTitleTextView.setOnClickListener((View.OnClickListener) getActivity());
        mMiniPlayerArtistTextView.setOnClickListener((View.OnClickListener) getActivity());

        if(mMusicInfo != null) {
            mMiniPlayerTitleTextView.setText(mMusicInfo.getTitle());
            mMiniPlayerArtistTextView.setText(mMusicInfo.getArtist());
        }




    }


    @Override
    public void passDataToFragment(MusicInfo info) {
        mMusicInfo = info;

        Log.d(TAG, ""+info);
        mMiniPlayerTitleTextView.setText(mMusicInfo.getTitle());
        mMiniPlayerArtistTextView.setText(mMusicInfo.getArtist());
        mMiniPlayerAlbumArtImageView.setImageBitmap(MusicInfoUtil.getBitmap(getActivity(), mMusicInfo.getUri(), 4));
    }



}
