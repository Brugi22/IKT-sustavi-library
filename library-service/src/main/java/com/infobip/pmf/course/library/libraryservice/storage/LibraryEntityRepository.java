package com.infobip.pmf.course.library.libraryservice.storage;

import com.infobip.pmf.course.library.libraryservice.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.List;

public interface LibraryEntityRepository extends ListCrudRepository<LibraryEntity, Long> {
    Page<LibraryEntity> findByGroupIdAndArtifactId(String groupId, String artifactId, Pageable pageable);
    Page<LibraryEntity> findByGroupId(String groupId, Pageable pageable);
    Page<LibraryEntity> findByArtifactId(String artifactId, Pageable pageable);
    Page<LibraryEntity> findAll(Pageable pageable);
    boolean existsByGroupIdAndArtifactId(String groupId, String artifactId);
}
