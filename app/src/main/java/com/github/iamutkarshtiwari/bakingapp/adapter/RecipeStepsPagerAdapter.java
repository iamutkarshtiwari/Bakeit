package com.github.iamutkarshtiwari.bakingapp.adapter;

/**
 * Created by utkarshtiwari on 06/02/2018.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.github.iamutkarshtiwari.bakingapp.fragments.SelectRecipeStepDetail;
import com.github.iamutkarshtiwari.bakingapp.fragments.SelectRecipeStepDetailFragment;
import com.github.iamutkarshtiwari.bakingapp.model.RecipeStepsModel;

import java.util.ArrayList;
import java.util.List;


public class RecipeStepsPagerAdapter extends FragmentStatePagerAdapter {

    List<SelectRecipeStepDetailFragment> list = new ArrayList();
    List<RecipeStepsModel> recipeStepsModelListt;

    public RecipeStepsPagerAdapter(FragmentManager fm, List<RecipeStepsModel> recipeStepsModellist) {
        super(fm);
        this.recipeStepsModelListt = recipeStepsModellist;
        for(int i=0;i<recipeStepsModellist.size();i++){
            this.list.add(SelectRecipeStepDetailFragment.newInstance(recipeStepsModellist.get(i),i));
        }

    }

    @Override
    public Fragment getItem(int position) {

        return list.get(position);
}

    @Override
    public int getCount() {
        return list.size();
    }

}