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
                "description", recipe.getDescription(),
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

}
