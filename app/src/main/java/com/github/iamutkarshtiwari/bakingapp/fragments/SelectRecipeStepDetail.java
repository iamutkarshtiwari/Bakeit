package com.github.iamutkarshtiwari.bakingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStep;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStepDetailContainer;
import com.github.iamutkarshtiwari.bakingapp.adapter.RecipeStepsPagerAdapter;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;
import com.github.iamutkarshtiwari.bakingapp.utils.UTils;

import java.util.ArrayList;
import java.util.List;

public class SelectRecipeStepDetail extends Fragment {

    String TAG = this.getClass().getSimpleName();

    ViewPager viewPager;
    RecipeStepsPagerAdapter recipeStepsPagerAdapter;

    TextView txtPrev,txtNext;

    List<RecipeStepsModel> list = new ArrayList<>();
    int recipe;
    int position = -1;

    public static SelectRecipeStepDetail newInstance(int recipe,int position) {
        SelectRecipeStepDetail selectRecipeStepDetailFragment = new SelectRecipeStepDetail();

        Bundle args = new Bundle();
        args.putInt("recipe", recipe);
        args.putInt("position", position);
        selectRecipeStepDetailFragment.setArguments(args);

        return selectRecipeStepDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = getArguments().getInt("recipe");
        Log.d(TAG,"recipe : "+recipe);
        position = getArguments().getInt("position");
        Log.d(TAG,"position : "+position);
        list = UTils.getAllRecipeStepsModels(mContext,recipe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View llLayout = inflater.inflate(R.layout.select_recipe_step_detail, container, false);

        viewPager = (ViewPager)llLayout.findViewById(R.id.viewPager);
        txtPrev = (TextView) llLayout.findViewById(R.id.txtPrev);
        txtNext = (TextView) llLayout.findViewById(R.id.txtNext);

        recipeStepsPagerAdapter = new RecipeStepsPagerAdapter(getChildFragmentManager(),list);
        viewPager.setAdapter(recipeStepsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG,"position p : "+position);
                if(position==0){
                    txtPrev.setEnabled(false);
                    txtNext.setEnabled(true);
                }else{
                    txtPrev.setEnabled(true);
                    if(position== list.size()-1){
                        txtNext.setEnabled(false);
                    }else{
                        txtNext.setEnabled(true);
                    }
                }
                if(SelectRecipeStep.twoPane){
                    ((SelectRecipeStep)mContext).selectedRecipeModel = list.get(position);
                }else{
                    ((SelectRecipeStepDetailContainer)mContext).recipeStepsModel = list.get(position);
                }
                for(int i=0;i<list.size();i++){
                    if(i!=position){
                        ((SelectRecipeStepDetailFragment)recipeStepsPagerAdapter.getItem(i)).stopPlayer();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        txtPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                viewPager.setCurrentItem(position);
            }
        });
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                viewPager.setCurrentItem(position);
            }
        });

        if (position != -1) viewPager.setCurrentItem(position);
        return llLayout;
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
