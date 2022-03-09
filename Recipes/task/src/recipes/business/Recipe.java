package recipes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @JsonIgnore
    private long id;
    private String name;
    private String description;
    private ArrayList<String> ingredients;
    private ArrayList<String> directions;
}
