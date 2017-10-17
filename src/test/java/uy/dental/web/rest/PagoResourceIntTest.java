package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Pago;
import uy.dental.domain.Paciente;
import uy.dental.repository.PagoRepository;
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
 * Test class for the PagoResource REST controller.
 *
 * @see PagoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class PagoResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final Float DEFAULT_MONTO = 1F;
    private static final Float UPDATED_MONTO = 2F;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPagoMockMvc;

    private Pago pago;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PagoResource pagoResource = new PagoResource(pagoRepository);
        this.restPagoMockMvc = MockMvcBuilders.standaloneSetup(pagoResource)
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
    public static Pago createEntity(EntityManager em) {
        Pago pago = new Pago()
            .fecha(DEFAULT_FECHA)
            .observacion(DEFAULT_OBSERVACION)
            .monto(DEFAULT_MONTO);
        // Add required entity
        Paciente paciente = PacienteResourceIntTest.createEntity(em);
        em.persist(paciente);
        em.flush();
        pago.setPaciente(paciente);
        return pago;
    }

    @Before
    public void initTest() {
        pago = createEntity(em);
    }

    @Test
    @Transactional
    public void createPago() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().size();

        // Create the Pago
        restPagoMockMvc.perform(post("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isCreated());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate + 1);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPago.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testPago.getMonto()).isEqualTo(DEFAULT_MONTO);
    }

    @Test
    @Transactional
    public void createPagoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().size();

        // Create the Pago with an existing ID
        pago.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagoMockMvc.perform(post("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setFecha(null);

        // Create the Pago, which fails.

        restPagoMockMvc.perform(post("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setMonto(null);

        // Create the Pago, which fails.

        restPagoMockMvc.perform(post("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPagos() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList
        restPagoMockMvc.perform(get("/api/pagos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.doubleValue())));
    }

    @Test
    @Transactional
    public void getPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get the pago
        restPagoMockMvc.perform(get("/api/pagos/{id}", pago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pago.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPago() throws Exception {
        // Get the pago
        restPagoMockMvc.perform(get("/api/pagos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago
        Pago updatedPago = pagoRepository.findOne(pago.getId());
        updatedPago
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION)
            .monto(UPDATED_MONTO);

        restPagoMockMvc.perform(put("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPago)))
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPago.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testPago.getMonto()).isEqualTo(UPDATED_MONTO);
    }

    @Test
    @Transactional
    public void updateNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Create the Pago

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPagoMockMvc.perform(put("/api/pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pago)))
            .andExpect(status().isCreated());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);
        int databaseSizeBeforeDelete = pagoRepository.findAll().size();

        // Get the pago
        restPagoMockMvc.perform(delete("/api/pagos/{id}", pago.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pago.class);
        Pago pago1 = new Pago();
        pago1.setId(1L);
        Pago pago2 = new Pago();
        pago2.setId(pago1.getId());
        assertThat(pago1).isEqualTo(pago2);
        pago2.setId(2L);
        assertThat(pago1).isNotEqualTo(pago2);
        pago1.setId(null);
        assertThat(pago1).isNotEqualTo(pago2);
    }
}
