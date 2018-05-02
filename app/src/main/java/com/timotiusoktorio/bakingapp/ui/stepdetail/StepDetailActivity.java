package com.timotiusoktorio.bakingapp.ui.stepdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;

import java.util.List;

public class StepDetailActivity extends BaseActivity implements StepDetailFragment.StepDetailFragmentListener {

    public static final String EXTRA_RECIPE_JSON = "EXTRA_RECIPE_JSON";
    public static final String EXTRA_STEP_POSITION = "EXTRA_STEP_POSITION";

    private List<Step> steps;

    public static Intent newIntent(@NonNull Activity activity, @NonNull String recipeJson, int stepPosition) {
        Intent intent = new Intent(activity, StepDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_JSON, recipeJson);
        intent.putExtra(EXTRA_STEP_POSITION, stepPosition);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Recipe recipe = Recipe.fromJson(getIntent().getStringExtra(EXTRA_RECIPE_JSON));
        setTitle(recipe.getName());

        steps = recipe.getSteps();
        int stepPosition = getIntent().getIntExtra(EXTRA_STEP_POSITION, 0);
        int lastStepPosition = steps.size() - 1;

        if (savedInstanceState == null) {
            String stepJson = steps.get(stepPosition).toJson();
            StepDetailFragment fragment = StepDetailFragment.newInstance(stepJson, stepPosition, lastStepPosition);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
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
    public void onNavigateBefore(int stepPosition, int lastStepPosition) {
        if (stepPosition > 0) {
            stepPosition--;
            replaceFragment(stepPosition, lastStepPosition);
        }
    }

    @Override
    public void onNavigateNext(int stepPosition, int lastStepPosition) {
        if (stepPosition < lastStepPosition) {
            stepPosition++;
            replaceFragment(stepPosition, lastStepPosition);
        }
    }

    private void replaceFragment(int stepPosition, int lastStepPosition) {
        String stepJson = steps.get(stepPosition).toJson();
        StepDetailFragment fragment = StepDetailFragment.newInstance(stepJson, stepPosition, lastStepPosition);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}