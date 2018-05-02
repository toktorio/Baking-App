package com.timotiusoktorio.bakingapp.ui.recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;
import com.timotiusoktorio.bakingapp.ui.recipedetail.RecipeDetailActivity;

public class RecipesActivity extends BaseActivity implements RecipesFragment.RecipesFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        RecipesPagerAdapter pagerAdapter = new RecipesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onRecipeClick(@NonNull Recipe recipe) {
        startActivity(RecipeDetailActivity.newIntent(this, recipe));
    }
}