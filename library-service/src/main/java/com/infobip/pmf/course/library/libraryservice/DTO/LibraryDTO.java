package com.infobip.pmf.course.library.libraryservice.DTO;

import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record LibraryDTO (
    Long id,
    @NotNull String groupId,
    @NotNull String artifactId,
    @NotNull String name,
    String description,
    List<Long> versions
){
    public static LibraryDTO toDTO(LibraryEntity libraryEntity) {
        return new LibraryDTO(
                libraryEntity.getId(),
                libraryEntity.getGroupId(),
                libraryEntity.getArtifactId(),
                libraryEntity.getName(),
                libraryEntity.getDescription(),
                libraryEntity.getVersions() != null ?
                        libraryEntity.getVersions().stream().map(VersionEntity::getId).collect(Collectors.toList()) :
                        Collections.emptyList()
        );
    }
}
