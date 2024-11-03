package ufro.dci.filmaffinityfruna.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ufro.dci.filmaffinityfruna.model.entity.ActorEntity;
import ufro.dci.filmaffinityfruna.service.ActorService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ActorRestController.class)
class ActorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ActorRestController(actorService)).build();
    }

    @Test
    void testSearchByName() throws Exception {
        String name = "Robert Downey Jr.";
        ActorEntity actorEntity = new ActorEntity();
        actorEntity.setName(name);

        when(actorService.searchByName(name)).thenReturn(List.of(actorEntity));

        mockMvc.perform(get("/actor/search")
                .param("name", name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));

        verify(actorService, times(1)).searchByName(name);
    }

@Test
void testRegister() throws Exception {
    doNothing().when(actorService).register(any(ActorEntity.class));

    mockMvc.perform(post("/actor/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Chris Hemsworth\", \"nationality\":\"Australian\", \"dateOfBirth\":\"1983-08-11\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Actor registrado correctamente"));

    verify(actorService, times(1)).register(any(ActorEntity.class));
}

    @Test
    void testUpdate() throws Exception {
        Long id = 1L;
        ActorEntity updatedActor = new ActorEntity();
        updatedActor.setName("Chris Evans");

        doNothing().when(actorService).update(anyLong(), any(ActorEntity.class));

        mockMvc.perform(put("/actor/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Chris Evans\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Actor actualizado correctamente"));

        verify(actorService, times(1)).update(anyLong(), any(ActorEntity.class));
    }

    @Test
    void testDeleteActorById() throws Exception {
        Long id = 1L;

        doNothing().when(actorService).deleteActorById(id);

        mockMvc.perform(delete("/actor/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Actor eliminado correctamente"));

        verify(actorService, times(1)).deleteActorById(id);
    }
}