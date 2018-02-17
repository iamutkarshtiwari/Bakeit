package com.github.iamutkarshtiwari.bakingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipe;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStep;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStepDetailContainer;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;
import com.github.iamutkarshtiwari.bakingapp.utils.UTils;

public class SelectRecipeStepDetailFragment extends Fragment {

    String TAG = this.getClass().getSimpleName();
    private SimpleExoPlayer exoPlayer;

    SimpleExoPlayerView simpleExoPlayer;
    TextView txtDesc;
    ImageView imThumbnail;

    RecipeStepsModel recipeStepsModel;
    int position = -1;

    public static SelectRecipeStepDetailFragment newInstance(RecipeStepsModel recipeStepsModel,int position) {
        SelectRecipeStepDetailFragment selectRecipeStepDetailFragment = new SelectRecipeStepDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable("step", recipeStepsModel);
        args.putSerializable("position",position);
        selectRecipeStepDetailFragment.setArguments(args);

        return selectRecipeStepDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeStepsModel = (RecipeStepsModel) getArguments().getSerializable("step");
        position =  getArguments().getInt("position");
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!recipeStepsModel.getVideoURL().isEmpty()) {
            if(SelectRecipeStep.twoPane){
                ((SelectRecipeStep) mContext).currentPosition[position] = exoPlayer.getCurrentPosition();
            }else{
                ((SelectRecipeStepDetailContainer) mContext).currentPosition[position] = exoPlayer.getCurrentPosition();
            }
            stopPlayer();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View llLayout = inflater.inflate(R.layout.select_recipe_step_detail_fragment, container, false);

        simpleExoPlayer = (SimpleExoPlayerView) llLayout.findViewById(R.id.simpleExoPlayer);
        txtDesc = (TextView) llLayout.findViewById(R.id.txtDesc);
        imThumbnail = (ImageView) llLayout.findViewById(R.id.imThumbnail);


        txtDesc.setText(recipeStepsModel.getDescription());

        if (recipeStepsModel.getVideoURL().isEmpty()) {
            txtDesc.setVisibility(View.VISIBLE);
            if (recipeStepsModel.getThumbnailURL().isEmpty()) {
                simpleExoPlayer.setVisibility(View.GONE);
                imThumbnail.setVisibility(View.GONE);
            } else {
                simpleExoPlayer.setVisibility(View.GONE);
                imThumbnail.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(recipeStepsModel.getThumbnailURL()).into(imThumbnail);
            }
        } else {
            imThumbnail.setVisibility(View.GONE);
            simpleExoPlayer.setVisibility(View.VISIBLE);
            intializePlayer(recipeStepsModel.getVideoURL());
        }


        return llLayout;
    }

    public void intializePlayer(String mediaUrl) {
        simpleExoPlayer.setBackgroundColor(Color.BLACK);

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

        Uri videoUri = Uri.parse(mediaUrl);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ExoPlayerDemo");
        ExtractorsFactory extractor = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractor, null, null);
        exoPlayer.prepare(videoSource);
        if(SelectRecipeStep.twoPane){
            if(((SelectRecipeStep) mContext).currentPosition[position] != -1)
                exoPlayer.seekTo(((SelectRecipeStep) mContext).currentPosition[position]);
        }else{
            if(((SelectRecipeStepDetailContainer) mContext).currentPosition[position] != -1)
                exoPlayer.seekTo(((SelectRecipeStepDetailContainer) mContext).currentPosition[position]);
        }


        simpleExoPlayer.setPlayer(exoPlayer);
    }

    public void stopPlayer(){
        try {
            if(!recipeStepsModel.getVideoURL().isEmpty()){
                exoPlayer.stop();
                exoPlayer.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Activity mContext;

    // called for API equal or above 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    /*
    * Deprecated on API 23
    */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }


}
