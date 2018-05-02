package com.timotiusoktorio.bakingapp.ui.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.databinding.FragmentRecipesBinding;
import com.timotiusoktorio.bakingapp.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

public class RecipesFragment extends Fragment {

    private static final String ARG_RECIPES_PAGE = "ARG_RECIPES_PAGE";

    @Inject RecipesViewModelFactory factory;

    private FragmentRecipesBinding binding;
    private RecipesViewModel viewModel;
    private RecipesAdapter adapter;

    public static RecipesFragment newInstance(@NonNull RecipesPage recipesPage) {
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPES_PAGE, recipesPage.getPage());
        RecipesFragment fragment = new RecipesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecipesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof RecipesFragmentListener)) {
            throw new IllegalArgumentException(context.getClass().getSimpleName() + " must implements RecipesFragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);

        if (getArguments() == null) {
            throw new IllegalStateException("No arguments sent here");
        }

        int recipesPage = getArguments().getInt(ARG_RECIPES_PAGE);
        viewModel = ViewModelProviders.of(this, factory).get(RecipesViewModel.class);
        viewModel.setRecipesPage(recipesPage);
        binding.setViewModel(viewModel);

        setupRecyclerView();
        setupRefreshButton();
        subscribeToEvents();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.fetchRecipes(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshRecipes();
    }

    private void setupRecyclerView() {
        adapter = new RecipesAdapter(new RecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Recipe recipe) {
                ((RecipesFragmentListener) requireActivity()).onRecipeClick(recipe);
            }
        });
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireActivity(), getResources().getInteger(R.integer.grid_columns)));
        binding.rvRecipes.setHasFixedSize(true);
    }

    private void setupRefreshButton() {
        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fetchRecipes(true);
            }
        });
    }

    private void subscribeToEvents() {
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    adapter.updateData(recipes);
                }
            }
        });
        viewModel.getSnackbarMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                if (message != null) {
                    Snackbar.make(binding.mainContent, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    interface RecipesFragmentListener {

        void onRecipeClick(@NonNull Recipe recipe);
    }
}