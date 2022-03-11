package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(user -> list.add(user));

        Optional<User> matchingUser = list.stream().
                filter(u -> u.getEmail().equalsIgnoreCase(email)).
                findFirst();
        User user = null;
        if (matchingUser.isPresent()){
            user = matchingUser.get();
            user.setEmail(email);
        }
        //return matchingUser.orElse(null);
        return user;
    }

    public User findUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User toSave) {
        return userRepository.save(toSave);
    }

    public boolean hasUser(String email) {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(us -> list.add(us));

        Optional<User> matchingUser = list.stream().
                filter(u -> u.getEmail().equalsIgnoreCase(email)).
                findFirst();

        return matchingUser.isPresent();
    }
}

