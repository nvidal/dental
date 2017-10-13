package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Tratamiento;
import uy.dental.domain.Paciente;
import uy.dental.repository.TratamientoRepository;
import uy.dental.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TratamientoResource REST controller.
 *
 * @see TratamientoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class TratamientoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PROCEDIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_PROCEDIMIENTO = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTratamientoMockMvc;

    private Tratamiento tratamiento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TratamientoResource tratamientoResource = new TratamientoResource(tratamientoRepository);
        this.restTratamientoMockMvc = MockMvcBuilders.standaloneSetup(tratamientoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamiento createEntity(EntityManager em) {
        Tratamiento tratamiento = new Tratamiento()
            .fecha(DEFAULT_FECHA)
            .procedimiento(DEFAULT_PROCEDIMIENTO)
            .precio(DEFAULT_PRECIO);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        tratamiento.setPaciente(paciente);
        return tratamiento;
    }

    @Before
    public void initTest() {
        tratamiento = createEntity(em);
    }

    @Test
    @Transactional
    public void createTratamiento() throws Exception {
        int databaseSizeBeforeCreate = tratamientoRepository.findAll().size();

        // Create the Tratamiento
        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
            .andExpect(status().isCreated());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeCreate + 1);
        Tratamiento testTratamiento = tratamientoList.get(tratamientoList.size() - 1);
        assertThat(testTratamiento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTratamiento.getProcedimiento()).isEqualTo(DEFAULT_PROCEDIMIENTO);
        assertThat(testTratamiento.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createTratamientoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tratamientoRepository.findAll().size();

        // Create the Tratamiento with an existing ID
        tratamiento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setFecha(null);

        // Create the Tratamiento, which fails.

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProcedimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tratamientoRepository.findAll().size();
        // set the field null
        tratamiento.setProcedimiento(null);

        // Create the Tratamiento, which fails.

        restTratamientoMockMvc.perform(post("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
            .andExpect(status().isBadRequest());

        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTratamientos() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList
        restTratamientoMockMvc.perform(get("/api/tratamientos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].procedimiento").value(hasItem(DEFAULT_PROCEDIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", tratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tratamiento.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.procedimiento").value(DEFAULT_PROCEDIMIENTO.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTratamiento() throws Exception {
        // Get the tratamiento
        restTratamientoMockMvc.perform(get("/api/tratamientos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);
        int databaseSizeBeforeUpdate = tratamientoRepository.findAll().size();

        // Update the tratamiento
        Tratamiento updatedTratamiento = tratamientoRepository.findOne(tratamiento.getId());
        updatedTratamiento
            .fecha(UPDATED_FECHA)
            .procedimiento(UPDATED_PROCEDIMIENTO)
            .precio(UPDATED_PRECIO);

        restTratamientoMockMvc.perform(put("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTratamiento)))
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeUpdate);
        Tratamiento testTratamiento = tratamientoList.get(tratamientoList.size() - 1);
        assertThat(testTratamiento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTratamiento.getProcedimiento()).isEqualTo(UPDATED_PROCEDIMIENTO);
        assertThat(testTratamiento.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void updateNonExistingTratamiento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoRepository.findAll().size();

        // Create the Tratamiento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTratamientoMockMvc.perform(put("/api/tratamientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tratamiento)))
            .andExpect(status().isCreated());

        // Validate the Tratamiento in the database
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);
        int databaseSizeBeforeDelete = tratamientoRepository.findAll().size();

        // Get the tratamiento
        restTratamientoMockMvc.perform(delete("/api/tratamientos/{id}", tratamiento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tratamiento> tratamientoList = tratamientoRepository.findAll();
        assertThat(tratamientoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tratamiento.class);
        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        Tratamiento tratamiento2 = new Tratamiento();
        tratamiento2.setId(tratamiento1.getId());
        assertThat(tratamiento1).isEqualTo(tratamiento2);
        tratamiento2.setId(2L);
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
        tratamiento1.setId(null);
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
    }
}
