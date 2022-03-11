package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.business.Recipe;
import recipes.business.RecipeService;
import recipes.helper.IDGenerator;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/api/recipe/new")
    public Object addRecipe(@Valid @RequestBody Recipe recipe) {
        //recipe.setId(IDGenerator.getId());
        Recipe recipe1 = recipeService.add(recipe);
        return Map.of("id",recipe1.getId());
    }

    @GetMapping("/api/recipe/{id}")
    public Object getRecipe(@PathVariable long id) {
        Recipe recipe = recipeService.get(id);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Map.of(
                //"id", recipe.getId(),
                "name", recipe.getName(),
                "category", recipe.getCategory(),
                "description", recipe.getDescription(),
                "date", recipe.getDate(),
                "directions",recipe.getDirections(),
                "ingredients",recipe.getIngredients()
        ), HttpStatus.OK);
    }

    @DeleteMapping("/api/recipe/{id}")
    public Object removeRecipe(@PathVariable long id) {

        Recipe recipe = recipeService.get(id);

        if (recipe != null) {
            recipeService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/api/recipe/{id}")
    public Object modifyRecipe(@Valid @RequestBody Recipe recipe, @PathVariable long id) {
        Recipe searchedRecipe = recipeService.get(id);
        if(searchedRecipe == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recipe.setId(id);
        recipeService.update(recipe, searchedRecipe);
        //return recipe.getId();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/api/recipe/search")
    public Object searchRecipe(@RequestParam(defaultValue = "") String category,
                               @RequestParam(defaultValue = "") String name) {
        if (!category.isEmpty() && !name.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (category.isEmpty() && name.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!category.isEmpty()) {
            return recipeService.convert(true, category);
        }

        if (!name.isEmpty()) {
            return recipeService.convert(false, name);
        }

        return  name;
    }

}
