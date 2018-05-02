package com.timotiusoktorio.bakingapp.ui.recipes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class RecipesPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;

    RecipesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        RecipesPage recipesPage = RecipesPage.fromPage(position);
        return RecipesFragment.newInstance(recipesPage);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        RecipesPage recipesPage = RecipesPage.fromPage(position);
        return recipesPage.getTitle();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}