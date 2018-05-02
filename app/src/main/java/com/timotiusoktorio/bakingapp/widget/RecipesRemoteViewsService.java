package com.timotiusoktorio.bakingapp.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.timotiusoktorio.bakingapp.data.model.Ingredient;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.data.source.db.AppDatabase;
import com.timotiusoktorio.bakingapp.data.source.db.DbHelper;
import com.timotiusoktorio.bakingapp.data.source.db.RecipeDbHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipesRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this);
    }

    public class ListRemoteViewsFactory implements RemoteViewsFactory {

        private final Context context;
        private final List<Ingredient> ingredients = new ArrayList<>();

        ListRemoteViewsFactory(Context context) {
            this.context = context;
        }

        private void initData() {
            ingredients.clear();
            final AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME).allowMainThreadQueries().build();
            DbHelper dbHelper = new RecipeDbHelper(context, db);
            dbHelper.fetchFavoriteRecipe(new DataManager.FetchRecipeCallback() {
                @Override
                public void onSuccess(Recipe recipe) {
                    ingredients.addAll(recipe.getIngredients());
                    db.close();
                }

                @Override
                public void onFailure(String errorMessage) {
                    db.close();
                }
            });
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            ingredients.clear();
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
            rv.setTextViewText(android.R.id.text1, ingredients.get(position).getFormattedIngredient());
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}