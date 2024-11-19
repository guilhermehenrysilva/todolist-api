package todo.list.api.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import todo.list.api.domain.exceptions.AlreadyRegisteredUserException;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User insertOAuth2User(DefaultOAuth2User defaultOAuth2User, AuthenticationProviderEnum providerEnum) {
        String email = defaultOAuth2User.getAttribute("email");
        String name = defaultOAuth2User.getAttribute("name");
        String photoUrl = defaultOAuth2User.getAttribute("picture");

        if(repository.existsByEmailAndIsDifferentFromAuthenticationProvider(email, providerEnum))
            throw new AlreadyRegisteredUserException();

        Optional<User> optionalUser = repository.findByEmailAndAuthenticationProvider(email, providerEnum);
        User user;

        if (optionalUser.isPresent()) {
            log.info("[TodoList] Update User OAuth2 {}", providerEnum.name());
            user = optionalUser.get();
            user.update(name, photoUrl);
        } else {
            log.info("[TodoList] Inserting new User OAuth2 {}", providerEnum.name());
            user = this.userBuilder(email, null, name, providerEnum, photoUrl);
        }

        return repository.save(user);
    }

    public User insertUser(String email, String password, String name) {
        log.info("[TodoList] Inserting new User");
        if(repository.existsByEmail(email))
            throw new AlreadyRegisteredUserException();

        User user = this.userBuilder(email, password, name, AuthenticationProviderEnum.TODOLIST, null);
        return repository.save(user);
    }

    private User userBuilder(String email, String password, String name, AuthenticationProviderEnum authenticationProvider, String photoUrl) {
        return User.builder()
                .email(email)
                .password(password != null ? passwordEncoder.encode(password) : null)
                .name(name)
                .photoUrl(photoUrl)
                .authenticationProvider(authenticationProvider)
                .build();
    }
}
