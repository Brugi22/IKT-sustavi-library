package com.infobip.pmf.course.library.libraryservice.storage;

import com.infobip.pmf.course.library.libraryservice.Version;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "versions", schema = "library_service")
public class VersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String semanticVersion;

    @Column
    private String description;

    @Column(nullable = false)
    private boolean deprecated;

    @Column(nullable = false, updatable = false)
    private LocalDateTime releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private LibraryEntity library;

    @PrePersist
    protected void onCreate() {
        releaseDate = LocalDateTime.now();
    }

    public static VersionEntity from(Version version) {
        var versionEntity = new VersionEntity();
        versionEntity.setId(version.id());
        versionEntity.setSemanticVersion(version.semanticVersion());
        versionEntity.setDescription(version.description());
        versionEntity.setDeprecated(version.deprecated());
        versionEntity.setReleaseDate(version.releaseDate());
        return versionEntity;
    }

    public Version asVersion() {
        return new Version(id, description, semanticVersion, deprecated, releaseDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSemanticVersion() {
        return semanticVersion;
    }

    public void setSemanticVersion(String semanticVersion) {
        this.semanticVersion = semanticVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LibraryEntity getLibrary() {
        return library;
    }

    public void setLibrary(LibraryEntity library) {
        this.library = library;
    }
}

