package todo.list.api.domain.user;

public record UserDataResponse(Long id, String email, String name, AuthenticationClientTypeEnum authenticationClientType, int numberOfAnnotation) {

    public UserDataResponse(User user) {
        this(user.getId(), user.getEmail(), user.getName(), user.getAuthClientType(), user.getAnnotations().size());
    }

}
