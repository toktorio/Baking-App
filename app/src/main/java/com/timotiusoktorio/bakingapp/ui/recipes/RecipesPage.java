package com.timotiusoktorio.bakingapp.ui.recipes;

public enum RecipesPage {

    RECIPES(0, "Recipes"),
    FAVORITE_RECIPES(1, "Favorites");

    private final int page;
    private final String title;

    RecipesPage(int page, String title) {
        this.page = page;
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public String getTitle() {
        return title;
    }

    public static RecipesPage fromPage(int page) {
        for (RecipesPage recipesPage : RecipesPage.values()) {
            if (recipesPage.getPage() == page) {
                return recipesPage;
            }
        }
        return RecipesPage.RECIPES;
    }
}