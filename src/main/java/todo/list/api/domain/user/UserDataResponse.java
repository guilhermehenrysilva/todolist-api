package todo.list.api.domain.user;

public record UserDataResponse(Long id, String email, String name, AuthenticationProviderEnum authenticationProvider, int numberOfAnnotation) {

    public UserDataResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName(), user.getAuthenticationProvider(), user.getAnnotations().size());
    }

}
