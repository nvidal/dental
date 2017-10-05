package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Cuenta;
import uy.dental.domain.Paciente;
import uy.dental.repository.CuentaRepository;
import uy.dental.service.CuentaService;
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
 * Test class for the CuentaResource REST controller.
 *
 * @see CuentaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class CuentaResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_DEBE = 1F;
    private static final Float UPDATED_DEBE = 2F;

    private static final Float DEFAULT_HABER = 1F;
    private static final Float UPDATED_HABER = 2F;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCuentaMockMvc;

    private Cuenta cuenta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CuentaResource cuentaResource = new CuentaResource(cuentaService);
        this.restCuentaMockMvc = MockMvcBuilders.standaloneSetup(cuentaResource)
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
    public static Cuenta createEntity(EntityManager em) {
        Cuenta cuenta = new Cuenta()
            .fecha(DEFAULT_FECHA)
            .descripcion(DEFAULT_DESCRIPCION)
            .debe(DEFAULT_DEBE)
            .haber(DEFAULT_HABER);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        cuenta.setPaciente(paciente);
        return cuenta;
    }

    @Before
    public void initTest() {
        cuenta = createEntity(em);
    }

    @Test
    @Transactional
    public void createCuenta() throws Exception {
        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();

        // Create the Cuenta
        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isCreated());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate + 1);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testCuenta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCuenta.getDebe()).isEqualTo(DEFAULT_DEBE);
        assertThat(testCuenta.getHaber()).isEqualTo(DEFAULT_HABER);
    }

    @Test
    @Transactional
    public void createCuentaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();

        // Create the Cuenta with an existing ID
        cuenta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setFecha(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setDescripcion(null);

        // Create the Cuenta, which fails.

        restCuentaMockMvc.perform(post("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCuentas() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList
        restCuentaMockMvc.perform(get("/api/cuentas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].debe").value(hasItem(DEFAULT_DEBE.doubleValue())))
            .andExpect(jsonPath("$.[*].haber").value(hasItem(DEFAULT_HABER.doubleValue())));
    }

    @Test
    @Transactional
    public void getCuenta() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get the cuenta
        restCuentaMockMvc.perform(get("/api/cuentas/{id}", cuenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cuenta.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.debe").value(DEFAULT_DEBE.doubleValue()))
            .andExpect(jsonPath("$.haber").value(DEFAULT_HABER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCuenta() throws Exception {
        // Get the cuenta
        restCuentaMockMvc.perform(get("/api/cuentas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCuenta() throws Exception {
        // Initialize the database
        cuentaService.save(cuenta);

        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Update the cuenta
        Cuenta updatedCuenta = cuentaRepository.findOne(cuenta.getId());
        updatedCuenta
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .debe(UPDATED_DEBE)
            .haber(UPDATED_HABER);

        restCuentaMockMvc.perform(put("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCuenta)))
            .andExpect(status().isOk());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testCuenta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCuenta.getDebe()).isEqualTo(UPDATED_DEBE);
        assertThat(testCuenta.getHaber()).isEqualTo(UPDATED_HABER);
    }

    @Test
    @Transactional
    public void updateNonExistingCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Create the Cuenta

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCuentaMockMvc.perform(put("/api/cuentas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cuenta)))
            .andExpect(status().isCreated());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCuenta() throws Exception {
        // Initialize the database
        cuentaService.save(cuenta);

        int databaseSizeBeforeDelete = cuentaRepository.findAll().size();

        // Get the cuenta
        restCuentaMockMvc.perform(delete("/api/cuentas/{id}", cuenta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuenta.class);
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setId(1L);
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setId(cuenta1.getId());
        assertThat(cuenta1).isEqualTo(cuenta2);
        cuenta2.setId(2L);
        assertThat(cuenta1).isNotEqualTo(cuenta2);
        cuenta1.setId(null);
        assertThat(cuenta1).isNotEqualTo(cuenta2);
    }
}
