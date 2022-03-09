package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.IDirectionRepository;
import recipes.persistence.IIngredientRepository;
import recipes.persistence.IRecipeRepository;
import recipes.persistence.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IRecipeRepository iRecipeRepository;
    private final IIngredientRepository ingredientRepository;
    private final IDirectionRepository directionRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         IRecipeRepository iRecipeRepository,
                         IIngredientRepository ingredientRepository,
                         IDirectionRepository directionRepository) {
        this.recipeRepository = recipeRepository;
        this.iRecipeRepository = iRecipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.directionRepository = directionRepository;
    }

    public Recipe add(Recipe newRecipe){
        //return recipeRepository.add(newRecipe);
        Recipe savedRecipe = iRecipeRepository.save(newRecipe);
        updateDirections(savedRecipe);
        updateIngredients(savedRecipe);
        return savedRecipe;
    }

    private void updateIngredients(Recipe recipeToUpdate) {
        List<String> ingredients = recipeToUpdate.getIngredients();
        for (String i: ingredients) {
            Ingredient ingredientToSave = new Ingredient();
            ingredientToSave.setIngredient(i);
            ingredientToSave.setRecipe(recipeToUpdate);
            Ingredient savedIngredient = saveIngredient(ingredientToSave);
            recipeToUpdate.addIngredient(savedIngredient);
        }
    }

    private void updateDirections(Recipe recipeToUpdate) {
        List<String> directions = recipeToUpdate.getDirections();
        for (String d: directions) {
            Direction directionToSave = new Direction();
            directionToSave.setDirection(d);
            directionToSave.setRecipe(recipeToUpdate);
            Direction savedDirection = saveDirection(directionToSave);
            recipeToUpdate.addDirection(savedDirection);
        }
    }

    private Ingredient saveIngredient(Ingredient ingredientToSave){
        return ingredientRepository.save(ingredientToSave);
    }

    private Direction saveDirection(Direction directionToSave){
        return directionRepository.save(directionToSave);
    }

    public Recipe get(long id){
        //return recipeRepository.get(id);
        Optional<Recipe> recipe = iRecipeRepository.findById(id);
        Recipe recipeToReturn = recipe.orElse(null);
        if (recipeToReturn != null) {
            updateRecipe(recipeToReturn);
        }
        return recipeToReturn;
    }

    private void updateRecipe(Recipe recipeToUpdate) {
        List<Ingredient> ingredientList = recipeToUpdate.getIngredientList();
        List<Direction> directionList = recipeToUpdate.getDirectionList();

        for (Ingredient i: ingredientList) {
            recipeToUpdate.addIngredient(i.getIngredient());
        }

        for (Direction d: directionList) {
            recipeToUpdate.addDirection(d.getDirection());
        }
    }

    public void delete(long id) {
        Recipe recipeToDelete = get(id);
        deleteIngredients(recipeToDelete);
        deleteDirections(recipeToDelete);
        iRecipeRepository.delete(recipeToDelete);
    }


    public void deleteIngredients(Recipe toDelete){

        List<Ingredient> ingredientList = toDelete.getIngredientList();
        for (Ingredient i: ingredientList) {
            ingredientRepository.delete(i);
        }
    }

    public void deleteDirections(Recipe toDelete){
        List<Direction> directionList = toDelete.getDirectionList();
        for (Direction d: directionList) {
            directionRepository.delete(d);
        }
    }
}
