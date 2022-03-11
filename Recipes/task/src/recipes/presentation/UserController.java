package recipes.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.business.User;
import recipes.business.UserService;
import recipes.helper.BCryptEncoderConfig;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    BCryptEncoderConfig b;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody User user) {
        System.out.println("Test User "+user);
        if (userService.hasUser(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(b.getEncoder().encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
        //return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
