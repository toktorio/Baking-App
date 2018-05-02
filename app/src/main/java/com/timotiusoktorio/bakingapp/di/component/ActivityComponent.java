package com.timotiusoktorio.bakingapp.di.component;

import com.timotiusoktorio.bakingapp.di.module.ActivityModule;
import com.timotiusoktorio.bakingapp.di.scope.ActivityScope;
import com.timotiusoktorio.bakingapp.ui.recipedetail.RecipeDetailFragment;
import com.timotiusoktorio.bakingapp.ui.recipes.RecipesFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(RecipesFragment target);

    void inject(RecipeDetailFragment target);
}