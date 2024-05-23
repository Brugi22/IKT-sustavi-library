package com.infobip.pmf.course.library.libraryservice;

import com.infobip.pmf.course.library.libraryservice.DTO.CustomPageResponse;
import com.infobip.pmf.course.library.libraryservice.DTO.LibraryDTO;
import com.infobip.pmf.course.library.libraryservice.exception.*;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntityRepository;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LibraryServiceTest {

    @Mock
    private LibraryEntityRepository libraryEntityRepository;

    @Mock
    private VersionEntityRepository versionEntityRepository;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllLibraries() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<LibraryEntity> libraryEntities = new PageImpl<>(List.of(libraryEntity), pageable, 1);

        when(libraryEntityRepository.findAll(pageable)).thenReturn(libraryEntities);

        CustomPageResponse<LibraryDTO> libraries = libraryService.getAllLibraries(null, null, pageable);

        assertNotNull(libraries);
        assertEquals(1, libraries.getTotalResults());
        assertEquals(1, libraries.getTotalPages());
        assertEquals(0, libraries.getPage());
        assertEquals(10, libraries.getSize());
        verify(libraryEntityRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testCreateLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setGroupId("org.test");
        libraryEntity.setArtifactId("test-artifact");

        when(libraryEntityRepository.existsByGroupIdAndArtifactId(any(), any())).thenReturn(false);
        when(libraryEntityRepository.save(any(LibraryEntity.class))).thenReturn(libraryEntity);

        LibraryEntity createdLibrary = libraryService.createLibrary(libraryEntity);

        assertNotNull(createdLibrary);
        assertEquals("org.test", createdLibrary.getGroupId());
        verify(libraryEntityRepository, times(1)).save(libraryEntity);
    }

    @Test
    public void testCreateLibrary_AlreadyExists() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setGroupId("org.test");
        libraryEntity.setArtifactId("test-artifact");

        when(libraryEntityRepository.existsByGroupIdAndArtifactId(any(), any())).thenReturn(true);

        assertThrows(LibraryAlreadyExistsException.class, () -> libraryService.createLibrary(libraryEntity));
    }

    @Test
    public void testGetLibraryById() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));

        LibraryEntity library = libraryService.getLibraryById(1L);

        assertNotNull(library);
        assertEquals(1L, library.getId());
        verify(libraryEntityRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetLibraryById_NotFound() {
        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> libraryService.getLibraryById(1L));
    }

    @Test
    public void testUpdateLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);
        libraryEntity.setName("Old Name");
        libraryEntity.setDescription("Old Description");

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(libraryEntityRepository.save(any(LibraryEntity.class))).thenReturn(libraryEntity);

        LibraryEntity updatedLibrary = libraryService.updateLibrary(1L, "New Name", "New Description");

        assertNotNull(updatedLibrary);
        assertEquals("New Name", updatedLibrary.getName());
        assertEquals("New Description", updatedLibrary.getDescription());
        verify(libraryEntityRepository, times(1)).findById(1L);
        verify(libraryEntityRepository, times(1)).save(libraryEntity);
    }

    @Test
    public void testDeleteLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        doNothing().when(libraryEntityRepository).delete(libraryEntity);

        libraryService.deleteLibrary(1L);

        verify(libraryEntityRepository, times(1)).findById(1L);
        verify(libraryEntityRepository, times(1)).delete(libraryEntity);
    }

    @Test
    public void testGetAllVersionsOfLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setId(1L);
        versionEntity.setSemanticVersion("1.0.0");

        Pageable pageable = PageRequest.of(0, 10);
        Page<VersionEntity> versionEntities = new PageImpl<>(List.of(versionEntity), pageable, 1);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.findByLibrary(any(LibraryEntity.class), any(Pageable.class))).thenReturn(versionEntities);

        CustomPageResponse<Version> versions = libraryService.getAllVersionsOfLibrary(1L, pageable);

        assertNotNull(versions);
        assertEquals(1, versions.getTotalResults());
        assertEquals("1.0.0", versions.getResults().getFirst().semanticVersion());
        verify(libraryEntityRepository, times(1)).findById(1L);
        verify(versionEntityRepository, times(1)).findByLibrary(libraryEntity, pageable);
    }

    @Test
    public void testGetAllVersionsOfLibrary_LibraryNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> libraryService.getAllVersionsOfLibrary(1L, pageable));
        verify(libraryEntityRepository, times(1)).findById(1L);
        verify(versionEntityRepository, never()).findByLibrary(any(LibraryEntity.class), any(Pageable.class));
    }

    @Test
    public void testCreateVersionForLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setSemanticVersion("1.0.0");

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.existsBySemanticVersionAndLibraryId(any(), any())).thenReturn(false);
        when(versionEntityRepository.save(any(VersionEntity.class))).thenReturn(versionEntity);

        VersionEntity createdVersion = libraryService.createVersionForLibrary(1L, versionEntity);

        assertNotNull(createdVersion);
        assertEquals("1.0.0", createdVersion.getSemanticVersion());
        verify(versionEntityRepository, times(1)).save(versionEntity);
    }

    @Test
    public void testCreateVersionForLibrary_LibraryNotFound() {
        VersionEntity versionEntity = new VersionEntity();

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> libraryService.createVersionForLibrary(1L, versionEntity));
    }

    @Test
    public void testUpdateVersionOfLibrary() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setId(1L);
        versionEntity.setSemanticVersion("1.0.0");
        versionEntity.setDeprecated(false);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.findById(1L)).thenReturn(Optional.of(versionEntity));
        when(versionEntityRepository.save(any(VersionEntity.class))).thenReturn(versionEntity);

        VersionEntity updatedVersion = libraryService.updateVersionOfLibrary(1L, 1L, "New Description", true);

        assertNotNull(updatedVersion);
        assertEquals("New Description", updatedVersion.getDescription());
        assertTrue(updatedVersion.isDeprecated());
        verify(versionEntityRepository, times(1)).findById(1L);
        verify(versionEntityRepository, times(1)).save(versionEntity);
    }

    @Test
    public void testUpdateVersionOfLibrary_VersionNotFound() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VersionNotFoundException.class, () -> libraryService.updateVersionOfLibrary(1L, 1L, "New Description", true));
    }

    @Test
    public void testGetVersionById() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setId(1L);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.findById(1L)).thenReturn(Optional.of(versionEntity));

        VersionEntity foundVersion = libraryService.getVersionById(1L, 1L);

        assertNotNull(foundVersion);
        assertEquals(1L, foundVersion.getId());
        verify(versionEntityRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetVersionById_VersionNotFound() {
        LibraryEntity libraryEntity = new LibraryEntity();
        libraryEntity.setId(1L);

        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.of(libraryEntity));
        when(versionEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VersionNotFoundException.class, () -> libraryService.getVersionById(1L, 1L));
    }

    @Test
    public void testGetVersionById_LibraryNotFound() {
        when(libraryEntityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LibraryNotFoundException.class, () -> libraryService.getVersionById(1L, 1L));
    }
}
