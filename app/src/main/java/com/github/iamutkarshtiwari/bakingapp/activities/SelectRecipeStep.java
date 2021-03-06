package com.github.iamutkarshtiwari.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.adapter.RecipeStepsRViewAdapter;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;
import com.github.iamutkarshtiwari.bakingapp.utils.UTils;

import java.util.List;

public class SelectRecipeStep extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();

    RecyclerView rView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    public static List<RecipeStepsModel> listRecipeStepss;
    private List<RecipeIngrediantModel> listRecipeIngrediants;

    public RecipeStepsRViewAdapter recipeStepssRViewAdapter;

    int listPosition = -1;

    public RecipeStepsModel selectedRecipeModel;
    public long [] currentPosition;
    public boolean exoPlayerState;

    public static boolean twoPane = false;
    public static FrameLayout item_detail_container;

    RecipeModel recipeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe_step);

        if (findViewById(R.id.recipe_detail_container) != null) {
            twoPane = true;
            item_detail_container = (FrameLayout) findViewById(R.id.recipe_detail_container);
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recipeModel = (RecipeModel) getIntent().getExtras().getSerializable("recipe");
        listRecipeStepss = UTils.getAllRecipeStepsModels(this,recipeModel.getId());
        if(savedInstanceState!=null){
            Log.d(TAG,"savedInstanceState!=null");
            currentPosition = savedInstanceState.getLongArray("currentPosition");
            exoPlayerState = savedInstanceState.getBoolean("playerState");
        }else{
            Log.d(TAG,"savedInstanceState==null");
            currentPosition = new long[SelectRecipeStep.listRecipeStepss.size()];
            for(int i=0;i<SelectRecipeStep.listRecipeStepss.size();i++){
                currentPosition[i] = -1;
                exoPlayerState = false;
            }
        }

        listRecipeIngrediants = UTils.getAllRecipeIngrediantModels(this,recipeModel.getId());

        recipeStepssRViewAdapter = new RecipeStepsRViewAdapter(this,listRecipeIngrediants, listRecipeStepss);
        rView = (RecyclerView) findViewById(R.id.rView);
        rView.setLayoutManager(linearLayoutManager);
        rView.setAdapter(recipeStepssRViewAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (listPosition != -1) {
            linearLayoutManager.scrollToPosition(listPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("listPosition", linearLayoutManager.findFirstVisibleItemPosition());
        outState.putBoolean("playerState", exoPlayerState);
        outState.putLongArray("currentPosition",currentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        listPosition = savedInstanceState.getInt("listPosition");
        exoPlayerState = savedInstanceState.getBoolean("playerState");
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
    return super.onOptionsItemSelected(item);
    }


   
}

