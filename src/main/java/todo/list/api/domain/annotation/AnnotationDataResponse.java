package todo.list.api.domain.annotation;

public record AnnotationDataResponse(Long id, String description, boolean completed, String userName) {

    public AnnotationDataResponse(Annotation annotation) {
        this(annotation.getId(), annotation.getDescription(), annotation.isCompleted(), annotation.getUser().getName());
    }

}
