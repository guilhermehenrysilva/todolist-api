package todo.list.api.domain.annotation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import todo.list.api.domain.user.User;

import java.util.Date;

@Table(name = "annotations")
@Entity(name = "Annotation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners({AuditingEntityListener.class})
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private boolean completed;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", updatable = false)
    private Date createdDate;

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