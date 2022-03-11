package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.business.*;
import recipes.helper.IDGenerator;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @PostMapping("/api/recipe/new")
    public Object addRecipe(@AuthenticationPrincipal UserDetailImpl details, @Valid @RequestBody Recipe recipe) {
        //recipe.setId(IDGenerator.getId());

        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {

            User user = details.getUser();
            System.out.println(user.getEmail());
            recipe.setUser(user);
        }

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
    public Object removeRecipe(@AuthenticationPrincipal UserDetailImpl details, @PathVariable long id) {

        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            User user = details.getUser();
            Recipe recipe = recipeService.get(id);
            if (recipe != null) {
                User userWhomSavedQuiz = userService.findUserById(recipe.getUser().getId());
                if (user.getEmail().equalsIgnoreCase(userWhomSavedQuiz.getEmail())) {
                    recipeService.delete(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/api/recipe/{id}")
    public Object modifyRecipe(@AuthenticationPrincipal UserDetailImpl details, @Valid @RequestBody Recipe recipe, @PathVariable long id) {
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            User user = details.getUser();
            Recipe savedRecipe = recipeService.get(id);
            //if (savedRecipe)
            if (savedRecipe != null) {
                User userWhomSavedQuiz = userService.findUserById(savedRecipe.getUser().getId());
                if (user.getEmail().equalsIgnoreCase(userWhomSavedQuiz.getEmail())) {
                    Recipe searchedRecipe = recipeService.get(id);
                    if(searchedRecipe == null){
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                    recipe.setId(id);
                    recipe.setUser(userWhomSavedQuiz);
                    recipeService.update(recipe, searchedRecipe);
                    //return recipe.getId();
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

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
