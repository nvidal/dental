package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Saldo;
import uy.dental.domain.Paciente;
import uy.dental.repository.SaldoRepository;
import uy.dental.service.SaldoService;
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
 * Test class for the SaldoResource REST controller.
 *
 * @see SaldoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class SaldoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Float DEFAULT_DEBE = 1F;
    private static final Float UPDATED_DEBE = 2F;

    private static final Float DEFAULT_HABER = 1F;
    private static final Float UPDATED_HABER = 2F;

    @Autowired
    private SaldoRepository saldoRepository;

    @Autowired
    private SaldoService saldoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaldoMockMvc;

    private Saldo saldo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaldoResource saldoResource = new SaldoResource(saldoService);
        this.restSaldoMockMvc = MockMvcBuilders.standaloneSetup(saldoResource)
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
    public static Saldo createEntity(EntityManager em) {
        Saldo saldo = new Saldo()
            .fecha(DEFAULT_FECHA)
            .descripcion(DEFAULT_DESCRIPCION)
            .debe(DEFAULT_DEBE)
            .haber(DEFAULT_HABER);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        saldo.setPaciente(paciente);
        return saldo;
    }

    @Before
    public void initTest() {
        saldo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaldo() throws Exception {
        int databaseSizeBeforeCreate = saldoRepository.findAll().size();

        // Create the Saldo
        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isCreated());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeCreate + 1);
        Saldo testSaldo = saldoList.get(saldoList.size() - 1);
        assertThat(testSaldo.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testSaldo.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testSaldo.getDebe()).isEqualTo(DEFAULT_DEBE);
        assertThat(testSaldo.getHaber()).isEqualTo(DEFAULT_HABER);
    }

    @Test
    @Transactional
    public void createSaldoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saldoRepository.findAll().size();

        // Create the Saldo with an existing ID
        saldo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setFecha(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = saldoRepository.findAll().size();
        // set the field null
        saldo.setDescripcion(null);

        // Create the Saldo, which fails.

        restSaldoMockMvc.perform(post("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isBadRequest());

        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSaldos() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get all the saldoList
        restSaldoMockMvc.perform(get("/api/saldos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saldo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].debe").value(hasItem(DEFAULT_DEBE.doubleValue())))
            .andExpect(jsonPath("$.[*].haber").value(hasItem(DEFAULT_HABER.doubleValue())));
    }

    @Test
    @Transactional
    public void getSaldo() throws Exception {
        // Initialize the database
        saldoRepository.saveAndFlush(saldo);

        // Get the saldo
        restSaldoMockMvc.perform(get("/api/saldos/{id}", saldo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saldo.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.debe").value(DEFAULT_DEBE.doubleValue()))
            .andExpect(jsonPath("$.haber").value(DEFAULT_HABER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSaldo() throws Exception {
        // Get the saldo
        restSaldoMockMvc.perform(get("/api/saldos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaldo() throws Exception {
        // Initialize the database
        saldoService.save(saldo);

        int databaseSizeBeforeUpdate = saldoRepository.findAll().size();

        // Update the saldo
        Saldo updatedSaldo = saldoRepository.findOne(saldo.getId());
        updatedSaldo
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .debe(UPDATED_DEBE)
            .haber(UPDATED_HABER);

        restSaldoMockMvc.perform(put("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSaldo)))
            .andExpect(status().isOk());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeUpdate);
        Saldo testSaldo = saldoList.get(saldoList.size() - 1);
        assertThat(testSaldo.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testSaldo.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testSaldo.getDebe()).isEqualTo(UPDATED_DEBE);
        assertThat(testSaldo.getHaber()).isEqualTo(UPDATED_HABER);
    }

    @Test
    @Transactional
    public void updateNonExistingSaldo() throws Exception {
        int databaseSizeBeforeUpdate = saldoRepository.findAll().size();

        // Create the Saldo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaldoMockMvc.perform(put("/api/saldos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saldo)))
            .andExpect(status().isCreated());

        // Validate the Saldo in the database
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaldo() throws Exception {
        // Initialize the database
        saldoService.save(saldo);

        int databaseSizeBeforeDelete = saldoRepository.findAll().size();

        // Get the saldo
        restSaldoMockMvc.perform(delete("/api/saldos/{id}", saldo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Saldo> saldoList = saldoRepository.findAll();
        assertThat(saldoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Saldo.class);
        Saldo saldo1 = new Saldo();
        saldo1.setId(1L);
        Saldo saldo2 = new Saldo();
        saldo2.setId(saldo1.getId());
        assertThat(saldo1).isEqualTo(saldo2);
        saldo2.setId(2L);
        assertThat(saldo1).isNotEqualTo(saldo2);
        saldo1.setId(null);
        assertThat(saldo1).isNotEqualTo(saldo2);
    }
}
