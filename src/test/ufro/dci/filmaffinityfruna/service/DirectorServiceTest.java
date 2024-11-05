package ufro.dci.filmaffinityfruna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ufro.dci.filmaffinityfruna.model.entity.DirectorEntity;
import ufro.dci.filmaffinityfruna.repository.DirectorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorServiceTest {

    @InjectMocks
    private DirectorService directorService;

    @Mock
    private DirectorRepository directorRepository;

    private DirectorEntity directorEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        directorEntity = new DirectorEntity();
        directorEntity.setName("Director Test");
        directorEntity.setNationality("Testland");
        directorEntity.setDateOfBirth(LocalDate.parse("1980-01-01"));
        directorEntity.setWikipediaLink("http://test.com");
    }

    @Test
    void testRegistrarDirector_Exito() {
        when(directorRepository.existsByName(directorEntity.getName())).thenReturn(false);

        directorService.register(directorEntity);

        verify(directorRepository, times(1)).save(directorEntity);
    }

    @Test
    void testRegistrarDirector_YaExiste() {
        when(directorRepository.existsByName(directorEntity.getName())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorService.register(directorEntity);
        });

        assertEquals("El nombre del director ya está registrado", exception.getMessage());
    }

    @Test
    void testActualizarDirector_Exito() {
        Long directorId = 1L;
        when(directorRepository.findById(directorId)).thenReturn(Optional.of(directorEntity));
        when(directorRepository.existsByName(directorEntity.getName())).thenReturn(false);

        DirectorEntity directorModificado = new DirectorEntity();
        directorModificado.setName("Director Modificado");
        directorModificado.setNationality("Modifiedland");

        directorService.update(directorId, directorModificado);

        verify(directorRepository, times(1)).save(directorEntity);
        assertEquals("Director Modificado", directorEntity.getName());
        assertEquals("Modifiedland", directorEntity.getNationality());
    }

    @Test
    void testActualizarDirector_NoEncontrado() {
        Long directorId = 1L;
        when(directorRepository.findById(directorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorService.update(directorId, directorEntity);
        });

        assertEquals("Director no encontrado", exception.getMessage());
    }

    @Test
    void testEliminarDirector_Exito() {
        Long directorId = 1L;
        when(directorRepository.existsById(directorId)).thenReturn(true);

        directorService.deleteDirectorById(directorId);

        verify(directorRepository, times(1)).deleteById(directorId);
    }

    @Test
    void testEliminarDirector_NoEncontrado() {
        Long directorId = 1L;
        when(directorRepository.existsById(directorId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorService.deleteDirectorById(directorId);
        });

        assertEquals("Director no encontrado", exception.getMessage());
    }

    @Test
    void testBuscarPorNombre_Exito() {
        String nombre = "Director Test";
        when(directorRepository.existsByName(nombre)).thenReturn(true);
        when(directorRepository.findByName(nombre)).thenReturn(List.of(directorEntity));

        var resultado = directorService.searchByName(nombre);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(directorEntity.getName(), resultado.get(0).getName());
    }

    @Test
    void testBuscarPorNombre_NoEncontrado() {
        String nombre = "Director Inexistente";
        when(directorRepository.existsByName(nombre)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            directorService.searchByName(nombre);
        });

        assertEquals("Director no encontrado", exception.getMessage());
    }
}

