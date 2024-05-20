package com.infobip.pmf.course.library.libraryservice;

import jakarta.validation.constraints.NotNull;

public record UpdateVersionRequest(@NotNull boolean deprecated, String description) {
}
