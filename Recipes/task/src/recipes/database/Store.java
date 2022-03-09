package recipes.database;

import recipes.business.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private Map<Long, Recipe> recipes;
    private static Store instance;

    private Store() {
        recipes = new ConcurrentHashMap<>();
    }

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public Recipe addRecipe(Recipe newRecipe){
        recipes.put(newRecipe.getId(), newRecipe);
        return newRecipe;
    }

    public Recipe getRecipe(long id){
        return recipes.get(id);
    }

    public Collection<Recipe> getAllRecipes() {
        Collection<Recipe> list =  recipes.values();
        return list;
    }
}
