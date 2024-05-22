package com.infobip.pmf.course.library.libraryservice;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record Library(
        Long id,
        @NotNull String groupId,
        @NotNull String artifactId,
        @NotNull String name,
        String description,
        List<Version> versions
) { }
