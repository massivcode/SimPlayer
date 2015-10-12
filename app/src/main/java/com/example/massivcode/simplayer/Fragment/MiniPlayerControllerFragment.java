package com.example.massivcode.simplayer.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.massivcode.simplayer.Activity.MainActivity;
import com.example.massivcode.simplayer.R;
import com.example.massivcode.simplayer.listener.MediaPlayerStateToFragment;

/**
 * Created by massivCode on 2015-10-10.
 */
public class MiniPlayerControllerFragment extends android.support.v4.app.Fragment implements MediaPlayerStateToFragment {

    private static final String TAG = MiniPlayerControllerFragment.class.getSimpleName();
    private Button mMiniPlayerPreviousButton, mMiniPlayerPlayButton, mMiniPlayerNextButton;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Context context = getActivity();
        ((MainActivity)context).mediaPlayerStateToFragment = this;
        Log.d(TAG, "onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mini_player_controller, container, false);

        mMiniPlayerPreviousButton = (Button)view.findViewById(R.id.mini_player_previous_btn);
        mMiniPlayerPlayButton = (Button)view.findViewById(R.id.mini_player_play_btn);
        mMiniPlayerNextButton = (Button)view.findViewById(R.id.mini_player_next_btn);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMiniPlayerPreviousButton.setOnClickListener((View.OnClickListener) getActivity());
        mMiniPlayerPlayButton.setOnClickListener((View.OnClickListener)getActivity());
        mMiniPlayerNextButton.setOnClickListener((View.OnClickListener)getActivity());
        Log.d(TAG, "onAc");
    }


    @Override
    public void passConditionToFragment(Boolean mediaIsPlaying) {
        if(mediaIsPlaying) {
            mMiniPlayerPlayButton.setBackgroundResource(R.drawable.ic_av_pause);
        } else {
            mMiniPlayerPlayButton.setBackgroundResource(R.drawable.ic_av_play_arrow);
        }
    }
}
