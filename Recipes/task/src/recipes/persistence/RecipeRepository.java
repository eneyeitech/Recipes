package recipes.persistence;

import org.springframework.stereotype.Repository;
import recipes.business.Recipe;
import recipes.database.Store;

@Repository
public class RecipeRepository {
    private Store store = Store.getInstance();

    public Recipe add(Recipe newRecipe){
        return store.addRecipe(newRecipe);
    }

    public Recipe get(long id){
        return store.getRecipe(id);
    }
}
