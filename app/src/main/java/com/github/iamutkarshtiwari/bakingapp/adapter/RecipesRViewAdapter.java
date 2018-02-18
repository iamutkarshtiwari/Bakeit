package com.github.iamutkarshtiwari.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStep;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeModel;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by utkarshtiwari on 05/02/2018.
 */
public class RecipesRViewAdapter extends RecyclerView.Adapter<RecipesRViewAdapter.View_Holder> {

    private List<RecipeModel> listRecipes;
    Context con;
    String TAG = this.getClass().getSimpleName();

    public RecipesRViewAdapter(Context con, List<RecipeModel> listRecipes) {
        this.listRecipes = listRecipes;
        this.con = con;
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_recipe, parent, false);
        View_Holder holder = new View_Holder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        final RecipeModel recipe = listRecipes.get(position);

        holder.txtTitle.setText(recipe.getName());
        holder.cardRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.startActivity(new Intent(con,SelectRecipeStep.class).putExtra("recipe",recipe));
            }
        });

        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(con)
                    .load(recipe.getImage())
                    .error(R.drawable.ic_image)
                    .fit()
                    .into(holder.thumbnail);
        } else {
            holder.thumbnail.setVisibility(View.GONE);
        }

    }

    public void addRecipe(RecipeModel recipe){
        listRecipes.add(recipe);
        notifyDataSetChanged();
    }

    public void removeRecipe(RecipeModel recipe){
        listRecipes.remove(recipe);
        notifyDataSetChanged();
    }

    public void addRecipes(List<RecipeModel> recipes){
        listRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public void resetRecipes(List<RecipeModel> recipes){
        listRecipes.clear();
        listRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listRecipes.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cardRecipe;
        TextView txtTitle;
        ImageView thumbnail;


        View_Holder(View itemView) {
            super(itemView);
            cardRecipe = (CardView) itemView.findViewById(R.id.cardRecipe);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

}
