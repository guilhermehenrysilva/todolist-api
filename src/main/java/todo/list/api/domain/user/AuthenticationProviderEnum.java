package todo.list.api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthenticationProviderEnum {
    TODOLIST("todolist"),
    GOOGLE("google");

    private final  String description;

    public static AuthenticationProviderEnum getProvider(String description) {
        return Arrays.stream(AuthenticationProviderEnum.values())
                .filter((provider) -> provider.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }
}
