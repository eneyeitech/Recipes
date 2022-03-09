package recipes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;


    @Size(min = 1)
    @Transient
    private List<String> ingredients = new ArrayList<>();
    @Size(min = 1)
    @Transient
    private List<String> directions = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredientList = new ArrayList<>();
    @OneToMany(mappedBy = "recipe")
    private List<Direction> directionList = new ArrayList<>();

    public void addIngredient(Ingredient ingredientToAdd) {
        ingredientList.add(ingredientToAdd);
    }

    public void addDirection(Direction directionToAdd) {
        directionList.add(directionToAdd);
    }

    public void addIngredient(String ing) {
        ingredients.add(ing);
    }

    public void addDirection(String dir) {
        directions.add(dir);
    }
}
