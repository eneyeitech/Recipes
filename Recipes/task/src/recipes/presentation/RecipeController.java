package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.business.Recipe;
import recipes.business.RecipeService;
import recipes.helper.IDGenerator;

import java.util.Map;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/api/recipe/new")
    public Object addRecipe(@RequestBody Recipe recipe) {
        recipe.setId(IDGenerator.getId());
        recipeService.add(recipe);
        return Map.of("id",recipe.getId());
    }

    @GetMapping("/api/recipe/{id}")
    public Object getRecipe(@PathVariable long id) {
        Recipe recipe =  recipeService.get(id);
        if(recipe == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return recipe;
    }

}
