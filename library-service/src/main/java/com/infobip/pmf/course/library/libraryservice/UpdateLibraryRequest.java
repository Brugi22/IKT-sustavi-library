package com.infobip.pmf.course.library.libraryservice;

import jakarta.validation.constraints.NotNull;

public record UpdateLibraryRequest(@NotNull String name, String description) {
}
