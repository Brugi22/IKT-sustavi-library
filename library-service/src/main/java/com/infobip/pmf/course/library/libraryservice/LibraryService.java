package com.infobip.pmf.course.library.libraryservice;

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
    public Page<Library> getAllLibraries(String groupId, String artifactId, Pageable pageable) {
        Page<LibraryEntity> libraryEntity;

        if (groupId != null && artifactId != null) {
            libraryEntity = libraryEntityRepository.findByGroupIdAndArtifactId(groupId, artifactId, pageable);
        } else if (groupId != null) {
            libraryEntity = libraryEntityRepository.findByGroupId(groupId, pageable);
        } else if (artifactId != null) {
            libraryEntity = libraryEntityRepository.findByArtifactId(artifactId, pageable);
        } else {
            libraryEntity = libraryEntityRepository.findAll(pageable);
        }

        return libraryEntity.map(LibraryEntity::asLibrary);
    }

    @Transactional
    public LibraryEntity createLibrary(LibraryEntity library) {
        if (libraryEntityRepository.existsByGroupIdAndArtifactId(library.getGroupId(), library.getArtifactId())) {
            throw new LibraryAlreadyExistsException();
        }
        return libraryEntityRepository.save(library);
    }

    @Transactional(readOnly = true)
    public Library getLibraryById(Long id) {
        return libraryEntityRepository.findById(id).map(LibraryEntity::asLibrary)
                .orElseThrow(LibraryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public LibraryEntity getLibraryEntityById(Long id) {
        return libraryEntityRepository.findById(id)
                .orElseThrow(LibraryNotFoundException::new);
    }

    @Transactional
    public LibraryEntity updateLibrary(Long id, String name, String description) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library == null) throw new LibraryNotFoundException();
        library.setName(name);
        library.setDescription(description);
        return libraryEntityRepository.save(library);
    }

    @Transactional
    public void deleteLibrary(Long id) {
        LibraryEntity library = getLibraryEntityById(id);

        if(library != null) libraryEntityRepository.delete(library);
        else throw new LibraryNotFoundException();
    }

    @Transactional(readOnly = true)
    public Page<Version> getAllVersionsOfLibrary(Long id, Pageable pageable) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library != null) {
            Page<VersionEntity> versionEntity = versionEntityRepository.findByLibrary(library, pageable);
            return versionEntity.map(VersionEntity::asVersion);
        } else throw new LibraryNotFoundException();
    }

    @Transactional
    public VersionEntity createVersionForLibrary(Long id, VersionEntity version) {
        LibraryEntity library = getLibraryEntityById(id);
        if(library != null) {
            version.setLibrary(library);
            if (versionEntityRepository.existsBySemanticVersion(version.getSemanticVersion())) {
                throw new VersionAlreadyExistsException();
            }
            return versionEntityRepository.save(version);
        } else throw new LibraryNotFoundException();
    }

    @Transactional
    public VersionEntity updateVersionOfLibrary(Long id, Long versionId, String description, Boolean deprecated) {
        VersionEntity version = versionEntityRepository.findById(versionId).orElseThrow(VersionNotFoundException::new);
        if(version.isDeprecated()) throw new VersionDeprecatedException();
        version.setDescription(description);
        version.setDeprecated(deprecated);
        return versionEntityRepository.save(version);
    }

    @Transactional(readOnly = true)
    public VersionEntity getVersionById(Long libraryId, Long id) {
        libraryEntityRepository.findById(libraryId).orElseThrow(LibraryNotFoundException::new);

        return versionEntityRepository.findById(id)
                .orElseThrow(VersionNotFoundException::new);
    }
}
