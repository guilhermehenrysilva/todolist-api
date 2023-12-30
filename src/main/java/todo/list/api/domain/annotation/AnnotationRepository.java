package todo.list.api.domain.annotation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import todo.list.api.domain.user.User;


import java.util.List;

public interface AnnotationRepository extends JpaRepository<Annotation, Long> {

    Page<Annotation> findAllByUser(Pageable pageable, User user);

}
