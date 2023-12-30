package todo.list.api.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User insertUser(String email, String password, String name) {
        var user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .authClientType(AuthenticationClientTypeEnum.TODOLIST)
                .build();

        return repository.save(user);
    }

}
