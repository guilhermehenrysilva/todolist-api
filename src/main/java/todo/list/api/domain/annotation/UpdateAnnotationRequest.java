package todo.list.api.domain.annotation;

public record UpdateAnnotationRequest(Long id, String description, boolean completed) {
}
