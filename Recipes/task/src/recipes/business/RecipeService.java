package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.IDirectionRepository;
import recipes.persistence.IIngredientRepository;
import recipes.persistence.IRecipeRepository;
import recipes.persistence.RecipeRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    public Recipe update(Recipe updatedRecipe, Recipe oldRecipe){
        //return recipeRepository.add(newRecipe);
        deleteIngredients(oldRecipe);
        deleteDirections(oldRecipe);

        //updatedRecipe = iRecipeRepository.save(updatedRecipe);
        updateDirections(updatedRecipe);
        updateIngredients(updatedRecipe);
        updatedRecipe = iRecipeRepository.save(updatedRecipe);
        return updatedRecipe;
    }

    private void updateIngredients(Recipe recipeToUpdate) {
        List<String> ingredients = recipeToUpdate.getIngredients();
        for (String i: ingredients) {
            //System.out.println(i);
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

    public Collection<Object> allO() {
        Collection<Object> recipeCollection = new ArrayList<>();
        iRecipeRepository.findAll().forEach(recipe -> {
            updateRecipe(recipe);
            recipeCollection.add(Map.of(
                    //"id", recipe.getId(),
                    "name", recipe.getName(),
                    "category", recipe.getCategory(),
                    "description", recipe.getDescription(),
                    "date", recipe.getDate(),
                    "directions",recipe.getDirections(),
                    "ingredients",recipe.getIngredients()
            ));

        });
        return recipeCollection;
    }

    public Collection<Recipe> allR() {
        Collection<Recipe> recipeCollection = new ArrayList<>();
        iRecipeRepository.findAll().forEach(recipe -> {
            updateRecipe(recipe);

            recipeCollection.add(recipe);
        });
        return recipeCollection;
    }

    public List<Recipe> findByCategory(String category) {
        return allR().stream()
                .filter(r -> r.getCategory().equalsIgnoreCase(category))
                .sorted((recipe, t1) -> {
                    if (recipe.getDate().equals(t1.getDate())) {
                        return 0;
                    }
                    if(recipe.getDate().after(t1.getDate())) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());
        //return List.of();
    }

    public List<Recipe> findByName(String name) {
        return allR().stream()
                .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted((recipe, t1) -> {
                    if (recipe.getDate().equals(t1.getDate())) {
                        return 0;
                    }
                    if(recipe.getDate().after(t1.getDate())) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());
    }

    public Collection<Object> convert(boolean c, String search) {
        Collection<Object> recipeCollection = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>();
        if(c) {
            recipes = findByCategory(search);
        } else {
            recipes = findByName(search);
        }
        recipes.forEach(recipe -> {
            recipeCollection.add(Map.of(
                    //"id", recipe.getId(),
                    "name", recipe.getName(),
                    "category", recipe.getCategory(),
                    "description", recipe.getDescription(),
                    "date", recipe.getDate(),
                    "directions",recipe.getDirections(),
                    "ingredients",recipe.getIngredients()
            ));
        });
        return recipeCollection;
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
