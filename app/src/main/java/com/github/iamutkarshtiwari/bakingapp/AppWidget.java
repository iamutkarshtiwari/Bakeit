package com.github.iamutkarshtiwari.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.github.iamutkarshtiwari.bakingapp.utils.UTils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by utkarshtiwari on 07/02/2018.
 */


public class AppWidget extends AppWidgetProvider {

    RemoteViews views;
    ImageView imPrev,imNext,imData;

    int widget_recipe = 0;
    List<RecipeModel> recipeModels = new ArrayList<>();
    AppParameters p;
    String TAG = getClass().getSimpleName();





    Context cont;
    public static final String WIDGET_IDS_KEY ="AppWidget";
    public static final String WIDGET_IM_NEXT ="AppWidget_imNext";
    public static final String WIDGET_IM_PREV ="AppWidget_imPrev";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"intent action : "+intent.getAction());
        p = new AppParameters(context);
        widget_recipe = p.getInt("widget_recipe",0);
        recipeModels = UTils.getAllRecipeModels(context);
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else {
            if(intent.getAction().matches(WIDGET_IM_NEXT)){
                Log.d(TAG,"intent.getAction().matches(WIDGET_IM_NEXT)");
                if(widget_recipe<recipeModels.size()-1){
                    Log.d(TAG,"widget_recipe<recipeModels.size()-1");
                    widget_recipe++;
                    p.setInt(widget_recipe,"widget_recipe");
                    updateWidgetRecipe(context);
                }else{
                    Log.d(TAG,"widget_recipe>recipeModels.size()-1");
                    Log.d(TAG,"list size : "+recipeModels.size());
                }
            }else if(intent.getAction().matches(WIDGET_IM_PREV)){
                Log.d(TAG,"intent.getAction().matches(WIDGET_IM_PREV)");
                if(widget_recipe>0){
                    Log.d(TAG,"widget_recipe>0");
                    widget_recipe--;
                    p.setInt(widget_recipe,"widget_recipe");
                    updateWidgetRecipe(context);
                }else{
                    Log.d(TAG,"widget_recipe<0");
                }
            }else {
                super.onReceive(context, intent);
            }
            Log.d(TAG,"widget_recipe : "+widget_recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        cont = context;
        p = new AppParameters(context);
        widget_recipe = p.getInt("widget_recipe",0);
        recipeModels = UTils.getAllRecipeModels(context);
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(TAG,"updateAppWidget");
        views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        rowView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.app_widget_image, null);

        views.setImageViewResource(R.id.imPrev,R.drawable.arrow_left);
        views.setImageViewResource(R.id.imNext,R.drawable.arrow_right);

        RecipeModel recipeModel = UTils.getAllRecipeModels(context).get(p.getInt("widget_recipe",0));

       views.setImageViewBitmap(R.id.imData,getData(recipeModel));


        if(widget_recipe == 0){
            views.setViewVisibility(R.id.imPrev,GONE);
            views.setViewVisibility(R.id.imNext,VISIBLE);
        }else if(widget_recipe == (recipeModels.size()-1)){
            views.setViewVisibility(R.id.imNext,GONE);
            views.setViewVisibility(R.id.imPrev,VISIBLE);
        }else{
            views.setViewVisibility(R.id.imNext,VISIBLE);
            views.setViewVisibility(R.id.imPrev,VISIBLE);
        }
        views.setOnClickPendingIntent(R.id.imPrev, getPendingSelfIntent(context,WIDGET_IM_PREV));
        views.setOnClickPendingIntent(R.id.imNext, getPendingSelfIntent(context,WIDGET_IM_NEXT));

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    View rowView;
    private Bitmap getData(RecipeModel recipeModel) {

        List<RecipeIngrediantModel> recipeIngrediantModelList = UTils.getAllRecipeIngrediantModels(cont,recipeModel.getId());

        TextView txtTitle = (TextView)rowView.findViewById(R.id.txtTitle);
        LinearLayout linIngredients = (LinearLayout)rowView.findViewById(R.id.linIngredients);

        txtTitle.setText(recipeModel.getName());
        for(RecipeIngrediantModel recipeIngrediantModel:recipeIngrediantModelList){
            linIngredients.addView(ingredientView(recipeIngrediantModel));
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) cont.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
       rowView.layout(0,0,/*UTils.convertDpToPixel(200,cont)*/ (int) metrics.widthPixels, (int) metrics.heightPixels);
       //  rowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //Get the dimensions of the view so we can re-layout the view at its current size
        //and create a bitmap of the same size
        int width = rowView.getWidth();
        int height = rowView.getHeight();

        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        //Cause the view to re-layout
        rowView.measure(measuredWidth, measuredHeight);
        rowView.layout(0, 0, rowView.getMeasuredWidth(), rowView.getMeasuredHeight());

        //Create a bitmap backed Canvas to draw the view into
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        //Now that the view is laid out and we have a canvas, ask the view to draw itself into the canvas
        rowView.draw(c);

        return b;

    }

    public View ingredientView(RecipeIngrediantModel ingrediantModel) {
        View rowwView = ((LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_recipe_ingredient, null);

        TextView txtIngredient = (TextView) rowwView.findViewById(R.id.txtIngredient);
        TextView txtQuantity = (TextView) rowwView.findViewById(R.id.txtQuantity);
        TextView txtMeasure = (TextView) rowwView.findViewById(R.id.txtMeasure);

        txtIngredient.setText(ingrediantModel.getIngredient());
        txtQuantity.setText(String .valueOf(ingrediantModel.getQuantity()));
        txtMeasure.setText(ingrediantModel.getMeasure());

        return rowwView;
    }


    public void updateWidgetRecipe(Context context) {

        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(new ComponentName(context, AppWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidget.WIDGET_IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}
