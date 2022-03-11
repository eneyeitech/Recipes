package recipes.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
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
    @NotBlank
    private String category;

    @Column(nullable = true, updatable = true, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @LastModifiedDate
    private Date date;


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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    @PrePersist
    private void onCreate() {
        date = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        date = new Date();
    }
}
