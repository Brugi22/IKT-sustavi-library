package com.infobip.pmf.course.library.libraryservice;

import com.infobip.pmf.course.library.libraryservice.DTO.CustomPageResponse;
import com.infobip.pmf.course.library.libraryservice.DTO.LibraryDTO;
import com.infobip.pmf.course.library.libraryservice.exception.*;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntityRepository;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibraryService {

    private final LibraryEntityRepository libraryEntityRepository;
    private final VersionEntityRepository versionEntityRepository;

    public LibraryService(LibraryEntityRepository libraryEntityRepository, VersionEntityRepository versionRepository) {
        this.libraryEntityRepository = libraryEntityRepository;
        this.versionEntityRepository = versionRepository;
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<LibraryDTO> getAllLibraries(String groupId, String artifactId, Pageable pageable) {
        Page<LibraryEntity> libraryEntityPaginated;

        if (groupId != null && artifactId != null) {
            libraryEntityPaginated = libraryEntityRepository.findByGroupIdAndArtifactId(groupId, artifactId, pageable);
        } else if (groupId != null) {
            libraryEntityPaginated = libraryEntityRepository.findByGroupId(groupId, pageable);
        } else if (artifactId != null) {
            libraryEntityPaginated = libraryEntityRepository.findByArtifactId(artifactId, pageable);
        } else {
            libraryEntityPaginated = libraryEntityRepository.findAll(pageable);
        }

        Page<LibraryDTO> libraryPaginated = libraryEntityPaginated.map(LibraryDTO::toDTO);

        return new CustomPageResponse<>(libraryPaginated);
    }

    @Transactional
    public LibraryEntity createLibrary(LibraryEntity library) {
        if (libraryEntityRepository.existsByGroupIdAndArtifactId(library.getGroupId(), library.getArtifactId())) {
            throw new LibraryAlreadyExistsException("Library with groupId %s and artifactId %s already exists".formatted(library.getGroupId(), library.getArtifactId()),
                                                    "Use POST to create new library");
        }
        return libraryEntityRepository.save(library);
    }

    @Transactional(readOnly = true)
    public LibraryEntity getLibraryById(Long id) {
        return libraryEntityRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                                "Use GET to retrieve existing library"));
    }

    @Transactional(readOnly = true)
    public LibraryEntity getLibraryEntityById(Long id) {
        return libraryEntityRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                                "Use GET to retrieve existing library"));
    }

    @Transactional
    public LibraryEntity updateLibrary(Long id, String name, String description) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library == null) throw new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                               "Use PATCH to update existing library");
        library.setName(name);
        library.setDescription(description);
        return libraryEntityRepository.save(library);
    }

    @Transactional
    public void deleteLibrary(Long id) {
        LibraryEntity library = getLibraryEntityById(id);

        if(library != null) libraryEntityRepository.delete(library);
        else throw new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                "Use DELETE to delete existing library");
    }

    @Transactional(readOnly = true)
    public CustomPageResponse<Version> getAllVersionsOfLibrary(Long id, Pageable pageable) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library != null) {
            Page<Version> versionPaginated = versionEntityRepository.findByLibrary(library, pageable).map(VersionEntity::asVersion);
            return new CustomPageResponse<>(versionPaginated);
        } else throw new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                  "Use GET to retrieve versions of existing library");
    }

    @Transactional
    public VersionEntity createVersionForLibrary(Long id, VersionEntity version) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library != null) {
            version.setLibrary(library);
            if (versionEntityRepository.existsBySemanticVersionAndLibraryId(version.getSemanticVersion(), id)) {
                throw new VersionAlreadyExistsException("Version with semantic version '%s' already exists".formatted(version.getSemanticVersion()),
                                                        "Use POST to create new version for a library");
            }
            return versionEntityRepository.save(version);
        } else throw new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                  "Use POST to create version of existing library");
    }

    @Transactional
    public VersionEntity updateVersionOfLibrary(Long id, Long versionId, String description, Boolean deprecated) {
        libraryEntityRepository.findById(id).orElseThrow(() -> new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                                                            "Use PATCH to update version of existing library"));
        VersionEntity version = versionEntityRepository.findById(versionId).orElseThrow(() -> new VersionNotFoundException("Version with id '%d' doesn't exist".formatted(id),
                                                                                                                           "Use PATCH to update existing version"));
        if(version.isDeprecated()) throw new VersionDeprecatedException("Version is deprecated",
                                                                        "Use PATCH to update non deprecated versions");
        version.setDescription(description);
        version.setDeprecated(deprecated);
        return versionEntityRepository.save(version);
    }

    @Transactional(readOnly = true)
    public VersionEntity getVersionById(Long id, Long versionId) {
        libraryEntityRepository.findById(id).orElseThrow(() -> new LibraryNotFoundException("Library with id '%d' doesn't exist".formatted(id),
                                                                                            "Use GET to retrieve version of existing library"));

        return versionEntityRepository.findById(versionId)
                .orElseThrow(() -> new VersionNotFoundException("Version with id '%d' doesn't exist".formatted(versionId),
                                                                "Use GET to retrieve version of existing library"));
    }
}
