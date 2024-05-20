package com.infobip.pmf.course.library.libraryservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infobip.pmf.course.library.libraryservice.*;
import com.infobip.pmf.course.library.libraryservice.storage.LibraryEntity;
import com.infobip.pmf.course.library.libraryservice.storage.VersionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LibraryController.class)
@AutoConfigureMockMvc
public class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        when(libraryService.getAllLibraries(any(), any(), any())).thenReturn(
                new PageImpl<>(Collections.emptyList())
        );
        when(libraryService.createLibrary(any())).thenReturn(LibraryEntity.from(new Library(1L, "test", "test", "test", "test", Collections.emptyList())));
        when(libraryService.getLibraryById(1L)).thenReturn(new Library(1L, "test", "test", "test", "test", Collections.emptyList()));
        when(libraryService.updateLibrary(any(), any(), any())).thenReturn(LibraryEntity.from(new Library(1L, "test", "test", "test", "test", Collections.emptyList())));
        when(libraryService.getAllVersionsOfLibrary(1L, PageRequest.of(0, 20))).thenReturn(
                new PageImpl<>(Collections.emptyList())
        );
        when(libraryService.createVersionForLibrary(any(), any())).thenReturn(VersionEntity.from(new Version(1L, "test", "1.0.0", false, null)));
        when(libraryService.getVersionById(1L, 1L)).thenReturn(VersionEntity.from(new Version(1L, "test", "1.0.0", false, null)));
        when(libraryService.updateVersionOfLibrary(any(), any(), any(), anyBoolean())).thenReturn(VersionEntity.from(new Version(1L, "test", "1.0.0", false, null)));
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testGetAllLibraries() throws Exception {
        mockMvc.perform(get("/libraries"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testCreateLibrary() throws Exception {
        Library library = new Library(null, "test", "test", "test", "test", null);
        mockMvc.perform(post("/libraries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(library)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testGetLibraryById() throws Exception {
        mockMvc.perform(get("/libraries/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testUpdateLibrary() throws Exception {
        UpdateLibraryRequest request = new UpdateLibraryRequest("test", "test");
        mockMvc.perform(patch("/libraries/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testDeleteLibrary() throws Exception {
        mockMvc.perform(delete("/libraries/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testGetAllVersionsOfLibrary() throws Exception {
        mockMvc.perform(get("/libraries/{id}/versions", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testGetVersion() throws Exception {
        mockMvc.perform(get("/libraries/{id}/versions/{versionId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testCreateVersionForLibrary() throws Exception {
        Version version = new Version(null, "test", "1.0.0", false, null);
        mockMvc.perform(post("/libraries/{id}/versions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(version)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "API_KEY_APP la9psd71atbpgeg7fvvx")
    void testUpdateVersionOfLibrary() throws Exception {
        UpdateVersionRequest request = new UpdateVersionRequest(false, "test");
        mockMvc.perform(patch("/libraries/{id}/versions/{versionId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

