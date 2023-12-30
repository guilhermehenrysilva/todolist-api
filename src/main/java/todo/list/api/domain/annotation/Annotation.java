package todo.list.api.domain.annotation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import todo.list.api.domain.user.User;

@Table(name = "annotations")
@Entity(name = "Annotation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private boolean completed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Annotation(InsertAnnotationRequest data, User user) {
        this.description = data.description();
        this.completed = data.completed();
        this.user = user;
    }

    public void update(UpdateAnnotationRequest data) {
        if (data.description() != null)
            this.description = data.description();

        this.completed = data.completed();
    }

}