package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Presupuesto;
import uy.dental.domain.Paciente;
import uy.dental.repository.PresupuestoRepository;
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
 * Test class for the PresupuestoResource REST controller.
 *
 * @see PresupuestoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class PresupuestoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPresupuestoMockMvc;

    private Presupuesto presupuesto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PresupuestoResource presupuestoResource = new PresupuestoResource(presupuestoRepository);
        this.restPresupuestoMockMvc = MockMvcBuilders.standaloneSetup(presupuestoResource)
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
    public static Presupuesto createEntity(EntityManager em) {
        Presupuesto presupuesto = new Presupuesto()
            .fecha(DEFAULT_FECHA)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        presupuesto.setPaciente(paciente);
        return presupuesto;
    }

    @Before
    public void initTest() {
        presupuesto = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresupuesto() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isCreated());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate + 1);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPresupuesto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPresupuesto.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createPresupuestoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto with an existing ID
        presupuesto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setFecha(null);

        // Create the Presupuesto, which fails.

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setDescripcion(null);

        // Create the Presupuesto, which fails.

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrecioIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setPrecio(null);

        // Create the Presupuesto, which fails.

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPresupuestos() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get all the presupuestoList
        restPresupuestoMockMvc.perform(get("/api/presupuestos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presupuesto.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getPresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", presupuesto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(presupuesto.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPresupuesto() throws Exception {
        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);
        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Update the presupuesto
        Presupuesto updatedPresupuesto = presupuestoRepository.findOne(presupuesto.getId());
        updatedPresupuesto
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO);

        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPresupuesto)))
            .andExpect(status().isOk());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPresupuesto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPresupuesto.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void updateNonExistingPresupuesto() throws Exception {
        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Create the Presupuesto

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isCreated());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);
        int databaseSizeBeforeDelete = presupuestoRepository.findAll().size();

        // Get the presupuesto
        restPresupuestoMockMvc.perform(delete("/api/presupuestos/{id}", presupuesto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presupuesto.class);
        Presupuesto presupuesto1 = new Presupuesto();
        presupuesto1.setId(1L);
        Presupuesto presupuesto2 = new Presupuesto();
        presupuesto2.setId(presupuesto1.getId());
        assertThat(presupuesto1).isEqualTo(presupuesto2);
        presupuesto2.setId(2L);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
        presupuesto1.setId(null);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
    }
}
