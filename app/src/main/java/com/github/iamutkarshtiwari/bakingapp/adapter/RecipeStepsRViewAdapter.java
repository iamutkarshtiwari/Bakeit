package com.github.iamutkarshtiwari.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.iamutkarshtiwari.bakingapp.R;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStep;
import com.github.iamutkarshtiwari.bakingapp.activities.SelectRecipeStepDetailContainer;
import com.github.iamutkarshtiwari.bakingapp.fragments.SelectRecipeStepDetail;
import com.github.iamutkarshtiwari.bakingapp.fragments.SelectRecipeStepDetailFragment;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeIngrediantModel;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;

import java.util.List;


/**
 * Created by utkarshtiwari on 05/02/2018.
 */
public class RecipeStepsRViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeStepsModel> listRecipeStepss;
    private List<RecipeIngrediantModel> listRecipeIngrediants;
    private boolean isIngrediantsAdded = false;
    Context con;
    String TAG = this.getClass().getSimpleName();

    final int TYPE_INGREDIENTS = 0;
    final int TYPE_STEPS_HEADER = 1;
    final int TYPE_STEPS = 2;

    public RecipeStepsRViewAdapter(Context con,List<RecipeIngrediantModel> listRecipeIngrediants, List<RecipeStepsModel> listRecipeStepss) {
        this.listRecipeStepss = listRecipeStepss;
        this.listRecipeIngrediants = listRecipeIngrediants;
        this.con = con;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_INGREDIENTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recipe_ingredients, parent, false);
                return new View_Holder_Ingrediant(view);
            case TYPE_STEPS_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recipe_steps, parent, false);
                return new View_Holder_Steps(view);
            case TYPE_STEPS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recipe_step, parent, false);
                return new View_Holder_Step(view);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_INGREDIENTS;
        }else if(position==1){
            return TYPE_STEPS_HEADER;
        }else{
            return TYPE_STEPS;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(position==0){
            if(!isIngrediantsAdded) {
                for (RecipeIngrediantModel ingrediantModel : listRecipeIngrediants) {
                    ((View_Holder_Ingrediant) holder).linIngredients.addView(ingredientView(ingrediantModel));
                }
                isIngrediantsAdded = true;
            }
        }else if(position==1){

        }else{
            final RecipeStepsModel recipeSteps = listRecipeStepss.get(position-2);
            if(SelectRecipeStep.twoPane && ((SelectRecipeStep) con).selectedRecipeModel!=null) {
                if (recipeSteps.getId() == ((SelectRecipeStep) con).selectedRecipeModel.getId()) {
                    ((View_Holder_Step) holder).cardRecipeStep.setCardBackgroundColor(Color.DKGRAY);
                } else {
                    ((View_Holder_Step) holder).cardRecipeStep.setCardBackgroundColor(Color.WHITE);
                }
            }
            ((View_Holder_Step)holder).txtTitle.setText(recipeSteps.getShortDescription());
            ((View_Holder_Step)holder).cardRecipeStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SelectRecipeStep.twoPane){
                        ((SelectRecipeStep)con).selectedRecipeModel = recipeSteps;  notifyDataSetChanged();
                        attachFragment(recipeSteps,position-2);
                    }else {
                        con.startActivity(new Intent(con, SelectRecipeStepDetailContainer.class).putExtra("step", recipeSteps).putExtra("position",position-2));
                    }
                }
            });
        }



    }

    public void addRecipeSteps(RecipeStepsModel recipeSteps){
        listRecipeStepss.add(recipeSteps);
        notifyDataSetChanged();
    }

    public void removeRecipeSteps(RecipeStepsModel recipeSteps){
        listRecipeStepss.remove(recipeSteps);
        notifyDataSetChanged();
    }

    public void addRecipeStepss(List<RecipeStepsModel> recipeStepss){
        listRecipeStepss.addAll(recipeStepss);
        notifyDataSetChanged();
    }

    public void resetRecipeStepss(List<RecipeStepsModel> recipeStepss){
        listRecipeStepss.clear();
        listRecipeStepss.addAll(recipeStepss);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listRecipeStepss.size()+1;
    }

    public class View_Holder_Ingrediant extends RecyclerView.ViewHolder {

        LinearLayout linIngredients;

        View_Holder_Ingrediant(View itemView) {
            super(itemView);
            linIngredients = (LinearLayout) itemView.findViewById(R.id.linIngredients);
        }
    }

    public class View_Holder_Step extends RecyclerView.ViewHolder {

        CardView cardRecipeStep;
        TextView txtTitle;

        View_Holder_Step(View itemView) {
            super(itemView);
            cardRecipeStep = (CardView) itemView.findViewById(R.id.cardRecipeStep);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }


    public class View_Holder_Steps extends RecyclerView.ViewHolder {

        View_Holder_Steps(View itemView) {
            super(itemView);
        }
    }

    public View ingredientView(RecipeIngrediantModel ingrediantModel) {
        View rowView = ((LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_recipe_ingredient, null);

        TextView txtIngredient = (TextView) rowView.findViewById(R.id.txtIngredient);
        TextView txtQuantity = (TextView) rowView.findViewById(R.id.txtQuantity);
        TextView txtMeasure = (TextView) rowView.findViewById(R.id.txtMeasure);

        txtIngredient.setText(ingrediantModel.getIngredient());
        txtQuantity.setText(String .valueOf(ingrediantModel.getQuantity()));
        txtMeasure.setText(ingrediantModel.getMeasure());

        return rowView;
    }

    public void attachFragment(RecipeStepsModel recipeStepsModel,int position){
        Fragment recipeStepsDetail = SelectRecipeStepDetail.newInstance(recipeStepsModel.getRecipe(),position);
        FragmentManager fragmentManager = ((SelectRecipeStep)con).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(SelectRecipeStep.item_detail_container.getId(), recipeStepsDetail);
        fragmentTransaction.commit();
    }
}
