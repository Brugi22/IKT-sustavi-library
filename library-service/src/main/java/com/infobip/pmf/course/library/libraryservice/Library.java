package com.infobip.pmf.course.library.libraryservice;

import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Library(
        Long id,
        @NotNull String groupId,
        @NotNull String artifactId,
        @NotNull String name,
        String description,
        List<Version> versions
) {
    public static LibraryEntity toEntity(Library library) {
        var libraryEntity = new LibraryEntity();
        libraryEntity.setId(library.id());
        libraryEntity.setGroupId(library.groupId());
        libraryEntity.setArtifactId(library.artifactId());
        libraryEntity.setName(library.name());
        libraryEntity.setDescription(library.description());
        if(library.versions() != null) libraryEntity.setVersions(library.versions().stream().map(Version::toEntity).toList());
        return libraryEntity;
    }
}
