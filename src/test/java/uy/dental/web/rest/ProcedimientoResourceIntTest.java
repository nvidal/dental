package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Procedimiento;
import uy.dental.domain.Paciente;
import uy.dental.repository.ProcedimientoRepository;
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
 * Test class for the ProcedimientoResource REST controller.
 *
 * @see ProcedimientoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class ProcedimientoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PROCEDIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_PROCEDIMIENTO = "BBBBBBBBBB";

    @Autowired
    private ProcedimientoRepository procedimientoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedimientoMockMvc;

    private Procedimiento procedimiento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedimientoResource procedimientoResource = new ProcedimientoResource(procedimientoRepository);
        this.restProcedimientoMockMvc = MockMvcBuilders.standaloneSetup(procedimientoResource)
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
    public static Procedimiento createEntity(EntityManager em) {
        Procedimiento procedimiento = new Procedimiento()
            .fecha(DEFAULT_FECHA)
            .procedimiento(DEFAULT_PROCEDIMIENTO);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        procedimiento.setPaciente(paciente);
        return procedimiento;
    }

    @Before
    public void initTest() {
        procedimiento = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedimiento() throws Exception {
        int databaseSizeBeforeCreate = procedimientoRepository.findAll().size();

        // Create the Procedimiento
        restProcedimientoMockMvc.perform(post("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedimiento)))
            .andExpect(status().isCreated());

        // Validate the Procedimiento in the database
        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeCreate + 1);
        Procedimiento testProcedimiento = procedimientoList.get(procedimientoList.size() - 1);
        assertThat(testProcedimiento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testProcedimiento.getProcedimiento()).isEqualTo(DEFAULT_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    public void createProcedimientoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedimientoRepository.findAll().size();

        // Create the Procedimiento with an existing ID
        procedimiento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedimientoMockMvc.perform(post("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedimiento)))
            .andExpect(status().isBadRequest());

        // Validate the Procedimiento in the database
        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedimientoRepository.findAll().size();
        // set the field null
        procedimiento.setFecha(null);

        // Create the Procedimiento, which fails.

        restProcedimientoMockMvc.perform(post("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedimiento)))
            .andExpect(status().isBadRequest());

        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProcedimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedimientoRepository.findAll().size();
        // set the field null
        procedimiento.setProcedimiento(null);

        // Create the Procedimiento, which fails.

        restProcedimientoMockMvc.perform(post("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedimiento)))
            .andExpect(status().isBadRequest());

        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedimientos() throws Exception {
        // Initialize the database
        procedimientoRepository.saveAndFlush(procedimiento);

        // Get all the procedimientoList
        restProcedimientoMockMvc.perform(get("/api/procedimientos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedimiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].procedimiento").value(hasItem(DEFAULT_PROCEDIMIENTO.toString())));
    }

    @Test
    @Transactional
    public void getProcedimiento() throws Exception {
        // Initialize the database
        procedimientoRepository.saveAndFlush(procedimiento);

        // Get the procedimiento
        restProcedimientoMockMvc.perform(get("/api/procedimientos/{id}", procedimiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedimiento.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.procedimiento").value(DEFAULT_PROCEDIMIENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcedimiento() throws Exception {
        // Get the procedimiento
        restProcedimientoMockMvc.perform(get("/api/procedimientos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedimiento() throws Exception {
        // Initialize the database
        procedimientoRepository.saveAndFlush(procedimiento);
        int databaseSizeBeforeUpdate = procedimientoRepository.findAll().size();

        // Update the procedimiento
        Procedimiento updatedProcedimiento = procedimientoRepository.findOne(procedimiento.getId());
        updatedProcedimiento
            .fecha(UPDATED_FECHA)
            .procedimiento(UPDATED_PROCEDIMIENTO);

        restProcedimientoMockMvc.perform(put("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedimiento)))
            .andExpect(status().isOk());

        // Validate the Procedimiento in the database
        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeUpdate);
        Procedimiento testProcedimiento = procedimientoList.get(procedimientoList.size() - 1);
        assertThat(testProcedimiento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testProcedimiento.getProcedimiento()).isEqualTo(UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedimiento() throws Exception {
        int databaseSizeBeforeUpdate = procedimientoRepository.findAll().size();

        // Create the Procedimiento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedimientoMockMvc.perform(put("/api/procedimientos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedimiento)))
            .andExpect(status().isCreated());

        // Validate the Procedimiento in the database
        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedimiento() throws Exception {
        // Initialize the database
        procedimientoRepository.saveAndFlush(procedimiento);
        int databaseSizeBeforeDelete = procedimientoRepository.findAll().size();

        // Get the procedimiento
        restProcedimientoMockMvc.perform(delete("/api/procedimientos/{id}", procedimiento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Procedimiento> procedimientoList = procedimientoRepository.findAll();
        assertThat(procedimientoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedimiento.class);
        Procedimiento procedimiento1 = new Procedimiento();
        procedimiento1.setId(1L);
        Procedimiento procedimiento2 = new Procedimiento();
        procedimiento2.setId(procedimiento1.getId());
        assertThat(procedimiento1).isEqualTo(procedimiento2);
        procedimiento2.setId(2L);
        assertThat(procedimiento1).isNotEqualTo(procedimiento2);
        procedimiento1.setId(null);
        assertThat(procedimiento1).isNotEqualTo(procedimiento2);
    }
}
