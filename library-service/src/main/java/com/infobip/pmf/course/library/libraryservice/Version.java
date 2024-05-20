package com.infobip.pmf.course.library.libraryservice;

import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import com.infobip.pmf.course.library.libraryservice.validation.ValidSemanticVersion;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record Version (

        Long id,
        String description,
        @NotNull @ValidSemanticVersion String semanticVersion,
        @NotNull Boolean deprecated,
        LocalDateTime releaseDate
) {

    public Version {
        if (deprecated == null) {
            deprecated = false;
        }
    }
    public static VersionEntity toEntity(Version version) {
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setId(version.id());
        versionEntity.setSemanticVersion(version.semanticVersion());
        versionEntity.setDescription(version.description());
        versionEntity.setDeprecated(version.deprecated());
        versionEntity.setReleaseDate(version.releaseDate());
        return versionEntity;
    }
}
