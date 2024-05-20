package com.infobip.pmf.course.library.libraryservice.api;

import com.infobip.pmf.course.library.libraryservice.*;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libraries")
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public Page<Library> getAllLibraries(
            @RequestParam(required = false) String groupId,
            @RequestParam(required = false) String artifactId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return libraryService.getAllLibraries(groupId, artifactId, PageRequest.of(page, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Library createLibrary(@RequestBody @Valid Library library) {
        return libraryService.createLibrary(Library.toEntity(library)).asLibrary();
    }

    @GetMapping("/{id}")
    public Library getLibraryById(@PathVariable Long id) {
        return libraryService.getLibraryById(id);
    }

    @PatchMapping("/{id}")
    public Library updateLibrary(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLibraryRequest request) {
        return libraryService.updateLibrary(id, request.name(), request.description()).asLibrary();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
    }

    @GetMapping("/{id}/versions")
    public Page<Version> getAllVersionsOfLibrary(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return libraryService.getAllVersionsOfLibrary(id, PageRequest.of(page, size));
    }

    @GetMapping("/{id}/versions/{versionId}")
    public Version getVersion(
            @PathVariable Long id,
            @PathVariable Long versionId
    ) {
        return libraryService.getVersionById(id, versionId).asVersion();
    }

    @PostMapping("/{id}/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public Version createVersionForLibrary(
            @PathVariable Long id,
            @RequestBody @Valid Version version) {
        return libraryService.createVersionForLibrary(id, Version.toEntity(version)).asVersion();
    }

    @PatchMapping("/{id}/versions/{versionId}")
    public Version updateVersionOfLibrary(
            @PathVariable Long id,
            @PathVariable Long versionId,
            @RequestBody @Valid UpdateVersionRequest request) {
        return libraryService.updateVersionOfLibrary(id, versionId, request.description(), request.deprecated()).asVersion();
    }
}
