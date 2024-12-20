package todo.list.api.domain.user;

public record SignUpResponse(Long id, String email, String name, AuthenticationProviderEnum authenticationProvider) {

    public SignUpResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName(), user.getAuthenticationProvider());
    }

}
