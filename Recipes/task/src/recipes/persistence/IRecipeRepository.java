package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.Recipe;

@Repository
public interface IRecipeRepository extends CrudRepository<Recipe, Long> {
}
