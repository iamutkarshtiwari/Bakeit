package com.github.iamutkarshtiwari.bakingapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.github.iamutkarshtiwari.bakingapp.database.RecipesContentProvider;
import com.github.iamutkarshtiwari.bakingapp.database.DatabaseHandler;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by utkarshtiwari on 06/02/2018.
 */
public class UTils {

    public static boolean isOnline(Context context) {
        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    connected = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    connected = true;
        }
        return connected;
    }

    public static   int convertDpToPixel(int dp,Context cont){
        Resources resources = cont.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static  float convertPxToDP(int px,Context cont){
        Resources resources = cont.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = (float) (px / (metrics.densityDpi / 160f));
        return dp;
    }

    public static void openUrl(Activity activity, String Url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Url));
        activity.startActivity(i);
    }

    public static void shareText(Activity activity, String text, String title) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, title));
    }


    public static void showProgressDialog(String title, String message, ProgressDialog progressDialog) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog(ProgressDialog progressDialog) {
        progressDialog.hide();
    }

    public static int getScreenOrientation(Activity activity)
    {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
    // Getting All RecipeModels
    public static List<RecipeModel> getAllRecipeModels(Context context) {
        Uri contentUri = Uri.withAppendedPath(RecipesContentProvider.CONTENT_URI, DatabaseHandler.TABLE_RECIPES);
        Cursor cursor = context.getContentResolver().query(contentUri,null, null, null,null);

        List<RecipeModel> recipeModelList = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecipeModel recipeModel = new RecipeModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                Log.d("UTils",(cursor.getInt(0)+"-"+
                        cursor.getString(1)+"-"+
                        cursor.getString(2)+"-"+
                        cursor.getString(3)));
                // Adding recipeModel to list
                recipeModelList.add(recipeModel);
            } while (cursor.moveToNext());
        }

        // return recipeModel list
        return recipeModelList;
    }

    // Getting All RecipeIngrediantModels
    public static List<RecipeIngrediantModel> getAllRecipeIngrediantModels(Context context,int recipe) {
        Uri contentUri = Uri.withAppendedPath(RecipesContentProvider.CONTENT_URI, DatabaseHandler.TABLE_INGREDIENTS);
        Cursor cursor = context.getContentResolver().query(contentUri,null, "recipe =?", new String[]{String.valueOf(recipe)},null);

        List<RecipeIngrediantModel> recipeIngrediantModels = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecipeIngrediantModel recipeIngrediantModel = new RecipeIngrediantModel(cursor.getInt(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3));
                // Adding recipeIngrediantModel to list
                recipeIngrediantModels.add(recipeIngrediantModel);
            } while (cursor.moveToNext());
        }

        // return recipeIngrediantModel list
        return recipeIngrediantModels;
    }

    // Getting All RecipeStepsModels
    public static List<RecipeStepsModel> getAllRecipeStepsModels(Context context, int recipe) {
        Uri contentUri = Uri.withAppendedPath(RecipesContentProvider.CONTENT_URI, DatabaseHandler.TABLE_STEPS);
        Cursor cursor = context.getContentResolver().query(contentUri,null, "recipe =?", new String[]{String.valueOf(recipe)},null);

        List<RecipeStepsModel> recipeStepsModels = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecipeStepsModel recipeStepsModel = new RecipeStepsModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                // Adding recipeStepsModel to list
                recipeStepsModels.add(recipeStepsModel);
            } while (cursor.moveToNext());
        }

        // return recipeStepsModels list
        return recipeStepsModels;
    }
}
