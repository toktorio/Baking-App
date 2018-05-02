package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;
import com.timotiusoktorio.bakingapp.ui.stepdetail.StepDetailActivity;
import com.timotiusoktorio.bakingapp.ui.stepdetail.StepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends BaseActivity implements
        RecipeDetailFragment.RecipeDetailFragmentListener,
        StepDetailFragment.StepDetailFragmentListener {

    public static final String EXTRA_RECIPE_JSON = "EXTRA_RECIPE_JSON";

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String recipeJson;
    private boolean twoPaneMode;

    public static Intent newIntent(@NonNull Activity activity, @NonNull Recipe recipe) {
        Intent intent = new Intent(activity, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_JSON, recipe.toJson());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        recipeJson = getIntent().getStringExtra(EXTRA_RECIPE_JSON);
        twoPaneMode = getResources().getBoolean(R.bool.two_pane_mode);

        if (toolbar != null) {
            Recipe recipe = Recipe.fromJson(recipeJson);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipe.getName());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if (savedInstanceState == null) {
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipeJson);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepItemClick(Step step, int position) {
        if (twoPaneMode) startStepDetailFragment(step, position);
        else startActivity(StepDetailActivity.newIntent(this, recipeJson, position));
    }

    private void startStepDetailFragment(Step step, int position) {
        StepDetailFragment fragment = StepDetailFragment.newInstance(step.toJson(), position, Integer.MAX_VALUE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onNavigateBefore(int stepPosition, int lastStepPosition) {
        // not implemented. Before FAB will be hidden.
    }

    @Override
    public void onNavigateNext(int stepPosition, int lastStepPosition) {
        // not implemented. Next FAB will be hidden.
    }
}