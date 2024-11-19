package todo.list.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.list.api.domain.user.UserDataResponse;
import todo.list.api.domain.user.UserRepository;

@Slf4j
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<UserDataResponse> details(@PathVariable Long id) {
        log.info("[TodoList] Details user");
        var user = repository.getReferenceById(id);
        return ResponseEntity.ok(new UserDataResponse(user));
    }

}
