package com.timotiusoktorio.bakingapp.ui.recipedetail;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Ingredient;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.model.Step;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;
import com.timotiusoktorio.bakingapp.widget.RecipesAppWidgetProvider;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_RECIPE_JSON = "ARG_RECIPE_JSON";

    @Nullable @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_recipe) ImageView ivRecipe;
    @BindView(R.id.rv_ingredients) RecyclerView rvIngredients;
    @BindView(R.id.rv_steps) RecyclerView rvSteps;
    @BindView(R.id.fab_favorite) FloatingActionButton fabFavorite;

    @Inject RecipeDetailViewModelFactory factory;

    private Unbinder unbinder;
    private RecipeDetailViewModel viewModel;

    public static RecipeDetailFragment newInstance(@NonNull String recipeJson) {
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_JSON, recipeJson);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof RecipeDetailFragmentListener)) {
            throw new IllegalArgumentException(context.getClass().getSimpleName() + " must implements RecipeDetailFragmentListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);

        if (getArguments() == null) {
            throw new IllegalStateException("No arguments sent here");
        }

        Recipe recipe = Recipe.fromJson(getArguments().getString(ARG_RECIPE_JSON));
        viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
        viewModel.setRecipe(recipe);
        viewModel.resetFavoritePreferencesFlag();

        setupActionBar(recipe.getName());
        setupRecipeImageView(recipe.getImage());
        setupIngredientsRecyclerView(recipe.getIngredients());
        setupStepsRecyclerView(recipe.getSteps());
        setupFavoriteFab();
        subscribeToEvents();
    }

    private void setupActionBar(String recipeName) {
        if (toolbar != null) {
            ((BaseActivity) requireActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((BaseActivity) requireActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(recipeName);
        }
    }

    private void setupRecipeImageView(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).fit().centerCrop().into(ivRecipe);
        }
    }

    private void setupIngredientsRecyclerView(List<Ingredient> ingredients) {
        RecipeIngredientsAdapter adapter = new RecipeIngredientsAdapter(ingredients);
        rvIngredients.setAdapter(adapter);
        rvIngredients.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvIngredients.setNestedScrollingEnabled(false);
    }

    private void setupStepsRecyclerView(List<Step> steps) {
        RecipeStepsAdapter adapter = new RecipeStepsAdapter(steps, new RecipeStepsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step, int position) {
                ((RecipeDetailFragmentListener) requireActivity()).onStepItemClick(step, position);
            }
        });
        adapter.setMultiPaneSelectionMode(requireActivity().getResources().getBoolean(R.bool.two_pane_mode));
        rvSteps.setAdapter(adapter);
        rvSteps.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvSteps.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        rvSteps.setNestedScrollingEnabled(false);
    }

    private void setupFavoriteFab() {
        fabFavorite.setEnabled(false);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onFavoriteClick();
            }
        });
    }

    private void subscribeToEvents() {
        viewModel.getFavoriteFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean favorite) {
                if (favorite != null) {
                    fabFavorite.setImageResource(favorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
                    fabFavorite.setEnabled(true);
                    updateAppWidgetProvider();
                }
            }
        });
    }

    private void updateAppWidgetProvider() {
        Intent intent = new Intent(requireActivity(), RecipesAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        ComponentName componentName = new ComponentName(requireActivity().getApplication(), RecipesAppWidgetProvider.class);
        int[] appWidgetI‌​ds = AppWidgetManager.getInstance(requireActivity().getApplication()).getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetI‌​ds);
        requireActivity().sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    interface RecipeDetailFragmentListener {

        void onStepItemClick(Step step, int position);
    }
}