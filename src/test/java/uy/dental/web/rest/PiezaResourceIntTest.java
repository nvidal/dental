package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Pieza;
import uy.dental.repository.PiezaRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PiezaResource REST controller.
 *
 * @see PiezaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class PiezaResourceIntTest {

    private static final Integer DEFAULT_NUMERO_PIEZA = 1;
    private static final Integer UPDATED_NUMERO_PIEZA = 2;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private PiezaRepository piezaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPiezaMockMvc;

    private Pieza pieza;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PiezaResource piezaResource = new PiezaResource(piezaRepository);
        this.restPiezaMockMvc = MockMvcBuilders.standaloneSetup(piezaResource)
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
    public static Pieza createEntity(EntityManager em) {
        Pieza pieza = new Pieza()
            .numeroPieza(DEFAULT_NUMERO_PIEZA)
            .nombre(DEFAULT_NOMBRE);
        return pieza;
    }

    @Before
    public void initTest() {
        pieza = createEntity(em);
    }

    @Test
    @Transactional
    public void createPieza() throws Exception {
        int databaseSizeBeforeCreate = piezaRepository.findAll().size();

        // Create the Pieza
        restPiezaMockMvc.perform(post("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pieza)))
            .andExpect(status().isCreated());

        // Validate the Pieza in the database
        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeCreate + 1);
        Pieza testPieza = piezaList.get(piezaList.size() - 1);
        assertThat(testPieza.getNumeroPieza()).isEqualTo(DEFAULT_NUMERO_PIEZA);
        assertThat(testPieza.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createPiezaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = piezaRepository.findAll().size();

        // Create the Pieza with an existing ID
        pieza.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPiezaMockMvc.perform(post("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pieza)))
            .andExpect(status().isBadRequest());

        // Validate the Pieza in the database
        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroPiezaIsRequired() throws Exception {
        int databaseSizeBeforeTest = piezaRepository.findAll().size();
        // set the field null
        pieza.setNumeroPieza(null);

        // Create the Pieza, which fails.

        restPiezaMockMvc.perform(post("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pieza)))
            .andExpect(status().isBadRequest());

        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = piezaRepository.findAll().size();
        // set the field null
        pieza.setNombre(null);

        // Create the Pieza, which fails.

        restPiezaMockMvc.perform(post("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pieza)))
            .andExpect(status().isBadRequest());

        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPiezas() throws Exception {
        // Initialize the database
        piezaRepository.saveAndFlush(pieza);

        // Get all the piezaList
        restPiezaMockMvc.perform(get("/api/piezas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pieza.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroPieza").value(hasItem(DEFAULT_NUMERO_PIEZA)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getPieza() throws Exception {
        // Initialize the database
        piezaRepository.saveAndFlush(pieza);

        // Get the pieza
        restPiezaMockMvc.perform(get("/api/piezas/{id}", pieza.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pieza.getId().intValue()))
            .andExpect(jsonPath("$.numeroPieza").value(DEFAULT_NUMERO_PIEZA))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPieza() throws Exception {
        // Get the pieza
        restPiezaMockMvc.perform(get("/api/piezas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePieza() throws Exception {
        // Initialize the database
        piezaRepository.saveAndFlush(pieza);
        int databaseSizeBeforeUpdate = piezaRepository.findAll().size();

        // Update the pieza
        Pieza updatedPieza = piezaRepository.findOne(pieza.getId());
        updatedPieza
            .numeroPieza(UPDATED_NUMERO_PIEZA)
            .nombre(UPDATED_NOMBRE);

        restPiezaMockMvc.perform(put("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPieza)))
            .andExpect(status().isOk());

        // Validate the Pieza in the database
        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeUpdate);
        Pieza testPieza = piezaList.get(piezaList.size() - 1);
        assertThat(testPieza.getNumeroPieza()).isEqualTo(UPDATED_NUMERO_PIEZA);
        assertThat(testPieza.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPieza() throws Exception {
        int databaseSizeBeforeUpdate = piezaRepository.findAll().size();

        // Create the Pieza

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPiezaMockMvc.perform(put("/api/piezas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pieza)))
            .andExpect(status().isCreated());

        // Validate the Pieza in the database
        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePieza() throws Exception {
        // Initialize the database
        piezaRepository.saveAndFlush(pieza);
        int databaseSizeBeforeDelete = piezaRepository.findAll().size();

        // Get the pieza
        restPiezaMockMvc.perform(delete("/api/piezas/{id}", pieza.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pieza> piezaList = piezaRepository.findAll();
        assertThat(piezaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pieza.class);
        Pieza pieza1 = new Pieza();
        pieza1.setId(1L);
        Pieza pieza2 = new Pieza();
        pieza2.setId(pieza1.getId());
        assertThat(pieza1).isEqualTo(pieza2);
        pieza2.setId(2L);
        assertThat(pieza1).isNotEqualTo(pieza2);
        pieza1.setId(null);
        assertThat(pieza1).isNotEqualTo(pieza2);
    }
}
