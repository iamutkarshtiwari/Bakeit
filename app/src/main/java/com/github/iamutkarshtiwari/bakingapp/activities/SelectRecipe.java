package com.github.iamutkarshtiwari.bakingapp.activities;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.adapter.RecipesRViewAdapter;
import com.github.iamutkarshtiwari.bakingapp.database.DatabaseHandler;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;
import com.github.iamutkarshtiwari.bakingapp.utils.UTils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectRecipe extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    ProgressDialog pD;

    RecyclerView rView;
    GridLayoutManager gridLandscape = new GridLayoutManager(this, 3);
    LinearLayoutManager linPortrait = new LinearLayoutManager(this);
    List<RecipeModel> listRecipes = new ArrayList<>();
    public RecipesRViewAdapter recipesRViewAdapter;

    int listPosition = -1;
    public static RecipeModel recipeModel;

    public DatabaseHandler databaseHandler;

    String recipeURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_recipe);

        databaseHandler = new DatabaseHandler(this);
        pD = new ProgressDialog(this);

        recipeURL = getResources().getString(R.string.api_recipes_url);

        recipesRViewAdapter = new RecipesRViewAdapter(this, listRecipes);
        rView = (RecyclerView) findViewById(R.id.rView);
        if(UTils.getScreenOrientation(this)==Configuration.ORIENTATION_PORTRAIT){
            rView.setLayoutManager(linPortrait);
        }else{
            rView.setLayoutManager(gridLandscape);
        }
        rView.setAdapter(recipesRViewAdapter);

        getRecipes(recipeURL);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (listPosition != -1) {
            if(UTils.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT) {
                linPortrait.scrollToPosition(listPosition);
            }else{
                gridLandscape.scrollToPosition(listPosition);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(UTils.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT) {
            outState.putInt("listPosition", linPortrait.findFirstVisibleItemPosition());
        }else{
            outState.putInt("listPosition", gridLandscape.findFirstVisibleItemPosition());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        listPosition = savedInstanceState.getInt("listPosition");
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void getRecipes(String url) {

        //get Cashed
        recipesRViewAdapter.resetRecipes(UTils.getAllRecipeModels(this));

        //get Online
        if (UTils.isOnline(this)) {
            UTils.showProgressDialog(getString(R.string.progress_title), getString(R.string.progress_message), pD);
            StringRequest getTopRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    UTils.hideProgressDialog(pD);
                    try {
                        JSONArray results = new JSONArray(response);
                        databaseHandler.dropTables();
                        for (int i = 0; i < results.length(); i++) {
                            RecipeModel recipeModel = new GsonBuilder().create().fromJson(results.getJSONObject(i).toString(), RecipeModel.class);
                            databaseHandler.addRecipeModel(recipeModel);

                            JSONArray ingredients = results.getJSONObject(i).getJSONArray("ingredients");
                            for (int j = 0; j < ingredients.length(); j++) {
                                RecipeIngrediantModel recipeIngrediantModel = new GsonBuilder().create().fromJson(ingredients.getJSONObject(j).toString(), RecipeIngrediantModel.class);
                                recipeIngrediantModel.setRecipe(recipeModel.getId());
                                databaseHandler.addRecipeIngrediantModel(recipeIngrediantModel);
                            }

                            JSONArray steps = results.getJSONObject(i).getJSONArray("steps");
                            for (int k = 0; k < steps.length(); k++) {
                                RecipeStepsModel recipeStepsModel = new GsonBuilder().create().fromJson(steps.getJSONObject(k).toString(), RecipeStepsModel.class);
                                recipeStepsModel.setRecipe(recipeModel.getId());
                                databaseHandler.addRecipeStepsModel(recipeStepsModel);
                            }
                          }
                        recipesRViewAdapter.resetRecipes(UTils.getAllRecipeModels(SelectRecipe.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    UTils.hideProgressDialog(pD);
                    Toast.makeText(getApplicationContext(), R.string.ServerError, Toast.LENGTH_SHORT).show();
                }
            });

            Volley.newRequestQueue(this).add(getTopRequest);
        } else {
            Toast.makeText(getApplicationContext(), R.string.NoInternet, Toast.LENGTH_LONG).show();
        }
    }


   
}

