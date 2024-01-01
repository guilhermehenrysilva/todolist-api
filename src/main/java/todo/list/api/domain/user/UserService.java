package todo.list.api.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import todo.list.api.domain.exceptions.AlreadyRegisteredUserException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User insertOAuth2User(DefaultOAuth2User defaultOAuth2User) {
        String email = defaultOAuth2User.getAttribute("email");
        String name = defaultOAuth2User.getAttribute("name");

        if(repository.existsByEmailAndIsDifferentFromAuthClientType(email, AuthenticationClientTypeEnum.GOOGLE))
            throw new AlreadyRegisteredUserException();

        Optional<User> entity = repository.findByEmailAndAuthClientType(email, AuthenticationClientTypeEnum.GOOGLE);
        if(entity.isPresent()) {
            entity.get().update(name);
            return repository.save(entity.get());
        } else {
            User user = this.userBuilder(email, null, name, AuthenticationClientTypeEnum.GOOGLE);
            return repository.save(user);
        }
    }

    public User insertUser(String email, String password, String name) {
        if(repository.existsByEmail(email))
            throw new AlreadyRegisteredUserException();

        User user = this.userBuilder(email, password, name, AuthenticationClientTypeEnum.TODOLIST);

        return repository.save(user);
    }

    private User userBuilder(String email, String password, String name, AuthenticationClientTypeEnum authClientType) {
        return User.builder()
                .email(email)
                .password(password != null ? passwordEncoder.encode(password) : null)
                .name(name)
                .authClientType(authClientType)
                .build();
    }

}
