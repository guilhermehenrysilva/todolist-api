package todo.list.api.domain.user;

public record SignUpRequest(String email, String password, String name) {
}
