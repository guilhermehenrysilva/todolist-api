package todo.list.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import todo.list.api.domain.annotation.*;
import todo.list.api.utils.AuthUtils;

@RestController
@RequestMapping("/annotations")
@SecurityRequirement(name = "bearer-key")
public class AnnotationController {

    @Autowired
    private AnnotationRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<AnnotationDataResponse> insert(@RequestBody @Valid InsertAnnotationRequest data, UriComponentsBuilder uriBuilder) {
        var annotation = new Annotation(data, AuthUtils.getLoggedUser());
        repository.save(annotation);

        var uri = uriBuilder.path("/annotations/{id}").buildAndExpand(annotation.getId()).toUri();
        return ResponseEntity.created(uri).body(new AnnotationDataResponse(annotation));
    }

    @GetMapping
    public ResponseEntity<Page<AnnotationDataResponse>> listing(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        var page = repository.findAllByUser(pageable, AuthUtils.getLoggedUser()).map(AnnotationDataResponse::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<AnnotationDataResponse> update(@RequestBody @Valid UpdateAnnotationRequest data) {
        var annotation = repository.getReferenceById(data.id());
        annotation.update(data);

        return ResponseEntity.ok(new AnnotationDataResponse(annotation));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnotationDataResponse> details(@PathVariable Long id) {
        var annotation = repository.getReferenceById(id);
        return ResponseEntity.ok(new AnnotationDataResponse(annotation));
    }

}
