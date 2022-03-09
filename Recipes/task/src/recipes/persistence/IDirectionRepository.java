package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.Direction;

@Repository
public interface IDirectionRepository extends CrudRepository<Direction, Long> {
}
