package com.github.iamutkarshtiwari.bakingapp.database;
 
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bakingappDB";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String TABLE_STEPS = "steps";


    private static final String KEY_ID = "id";
    private static final String KEY_RECIPE = "recipe";
    //RECIPES Table
    private static final String KEY_NAME = "name";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_IMAGE = "image";
    //Ingredients Table
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_INGREDIENT = "ingredient";
    //Steps Table
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_VIDEO_URL = "videoURL";
    private static final String KEY_THUMBNAIL_URL = "thumbnailURL";

 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SERVINGS + " TEXT,"
                + KEY_IMAGE + " TEXT"
                + ")";

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_RECIPE + " INTEGER,"
                + KEY_QUANTITY + " DOUBLE,"
                + KEY_MEASURE + " TEXT,"
                + KEY_INGREDIENT + " TEXT"
                +")";

        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_RECIPE + " INTEGER,"
                + KEY_SHORT_DESCRIPTION + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_VIDEO_URL + " TEXT,"
                + KEY_THUMBNAIL_URL + " TEXT"
                + ")";

        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_STEPS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        // Create tables again
        onCreate(db);
    }

    public void dropTables(){

        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_SERVINGS + " TEXT,"
                + KEY_IMAGE + " TEXT"
                + ")";

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_RECIPE + " INTEGER,"
                + KEY_QUANTITY + " DOUBLE,"
                + KEY_MEASURE + " TEXT,"
                + KEY_INGREDIENT + " TEXT"
                +")";

        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_RECIPE + " INTEGER,"
                + KEY_ID + " INTEGER,"
                + KEY_SHORT_DESCRIPTION + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_VIDEO_URL + " TEXT,"
                + KEY_THUMBNAIL_URL + " TEXT"
                + ")";

        this.getWritableDatabase().execSQL(CREATE_RECIPES_TABLE);
        this.getWritableDatabase().execSQL(CREATE_INGREDIENTS_TABLE);
        this.getWritableDatabase().execSQL(CREATE_STEPS_TABLE);
    }

    // Adding new recipeModel
    public void addRecipeModel(RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues valuesRecipe = new ContentValues();
        valuesRecipe.put(KEY_ID, recipeModel.getId());
        valuesRecipe.put(KEY_NAME, recipeModel.getName());
        valuesRecipe.put(KEY_SERVINGS, recipeModel.getServings());
        valuesRecipe.put(KEY_IMAGE, recipeModel.getImage());

        db.insert(TABLE_RECIPES, null, valuesRecipe);
        db.close(); // Closing database connection
    }

    // Adding new recipeIngrediantModel
    public void addRecipeIngrediantModel(RecipeIngrediantModel recipeIngrediantModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesRecipeIngrediants = new ContentValues();
        valuesRecipeIngrediants.put(KEY_RECIPE, recipeIngrediantModel.getRecipe());
        valuesRecipeIngrediants.put(KEY_QUANTITY, recipeIngrediantModel.getQuantity());
        valuesRecipeIngrediants.put(KEY_MEASURE, recipeIngrediantModel.getMeasure());
        valuesRecipeIngrediants.put(KEY_INGREDIENT, recipeIngrediantModel.getIngredient());

        db.insert(TABLE_INGREDIENTS, null, valuesRecipeIngrediants);
        db.close(); // Closing database connection
    }

    // Adding new recipeStepsModel
    public void addRecipeStepsModel (RecipeStepsModel recipeStepsModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesRecipeSteps = new ContentValues();
        valuesRecipeSteps.put(KEY_ID,recipeStepsModel.getId());
        valuesRecipeSteps.put(KEY_SHORT_DESCRIPTION, recipeStepsModel.getShortDescription());
        valuesRecipeSteps.put(KEY_DESCRIPTION, recipeStepsModel.getDescription());
        valuesRecipeSteps.put(KEY_VIDEO_URL, recipeStepsModel.getVideoURL());
        valuesRecipeSteps.put(KEY_THUMBNAIL_URL, recipeStepsModel.getThumbnailURL());
        valuesRecipeSteps.put(KEY_RECIPE, recipeStepsModel.getRecipe());

        db.insert(TABLE_STEPS, null, valuesRecipeSteps);
        db.close(); // Closing database connection
    }
}