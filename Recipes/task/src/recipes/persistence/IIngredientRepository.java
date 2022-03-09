package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.Ingredient;

@Repository
public interface IIngredientRepository extends CrudRepository<Ingredient, Long> {
}
