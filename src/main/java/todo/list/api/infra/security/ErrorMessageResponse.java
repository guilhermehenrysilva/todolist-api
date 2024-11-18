package todo.list.api.infra.security;

import lombok.Builder;

@Builder
public record ErrorMessageResponse(
        String message,
        Integer status
) {
}
