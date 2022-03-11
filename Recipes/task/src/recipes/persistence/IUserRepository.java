package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import recipes.business.User;

public interface IUserRepository extends CrudRepository<User, Long> {
}
