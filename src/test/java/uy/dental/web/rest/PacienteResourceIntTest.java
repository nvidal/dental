package uy.dental.web.rest;

import uy.dental.DentalApp;

import uy.dental.domain.Paciente;
import uy.dental.repository.PacienteRepository;
import uy.dental.service.PacienteService;
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
 * Test class for the PacienteResource REST controller.
 *
 * @see PacienteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DentalApp.class)
public class PacienteResourceIntTest {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_CEDULA = "AAAAAAAAAA";
    private static final String UPDATED_CEDULA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ALERGICO = "AAAAAAAAAA";
    private static final String UPDATED_ALERGICO = "BBBBBBBBBB";

    private static final String DEFAULT_DIABETES = "AAAAAAAAAA";
    private static final String UPDATED_DIABETES = "BBBBBBBBBB";

    private static final String DEFAULT_PRESION_ALTA = "AAAAAAAAAA";
    private static final String UPDATED_PRESION_ALTA = "BBBBBBBBBB";

    private static final String DEFAULT_TIROIDES = "AAAAAAAAAA";
    private static final String UPDATED_TIROIDES = "BBBBBBBBBB";

    private static final String DEFAULT_CICATRIZACION = "AAAAAAAAAA";
    private static final String UPDATED_CICATRIZACION = "BBBBBBBBBB";

    private static final String DEFAULT_CARDIACA = "AAAAAAAAAA";
    private static final String UPDATED_CARDIACA = "BBBBBBBBBB";

    private static final String DEFAULT_FARMACOS = "AAAAAAAAAA";
    private static final String UPDATED_FARMACOS = "BBBBBBBBBB";

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PacienteResource pacienteResource = new PacienteResource(pacienteService);
        this.restPacienteMockMvc = MockMvcBuilders.standaloneSetup(pacienteResource)
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
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .fecha(DEFAULT_FECHA)
            .nombres(DEFAULT_NOMBRES)
            .apellidos(DEFAULT_APELLIDOS)
            .cedula(DEFAULT_CEDULA)
            .telefono(DEFAULT_TELEFONO)
            .celular(DEFAULT_CELULAR)
            .direccion(DEFAULT_DIRECCION)
            .mail(DEFAULT_MAIL)
            .alergico(DEFAULT_ALERGICO)
            .diabetes(DEFAULT_DIABETES)
            .presionAlta(DEFAULT_PRESION_ALTA)
            .tiroides(DEFAULT_TIROIDES)
            .cicatrizacion(DEFAULT_CICATRIZACION)
            .cardiaca(DEFAULT_CARDIACA)
            .farmacos(DEFAULT_FARMACOS);
        return paciente;
    }

    @Before
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testPaciente.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testPaciente.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testPaciente.getCedula()).isEqualTo(DEFAULT_CEDULA);
        assertThat(testPaciente.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testPaciente.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testPaciente.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testPaciente.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testPaciente.getAlergico()).isEqualTo(DEFAULT_ALERGICO);
        assertThat(testPaciente.getDiabetes()).isEqualTo(DEFAULT_DIABETES);
        assertThat(testPaciente.getPresionAlta()).isEqualTo(DEFAULT_PRESION_ALTA);
        assertThat(testPaciente.getTiroides()).isEqualTo(DEFAULT_TIROIDES);
        assertThat(testPaciente.getCicatrizacion()).isEqualTo(DEFAULT_CICATRIZACION);
        assertThat(testPaciente.getCardiaca()).isEqualTo(DEFAULT_CARDIACA);
        assertThat(testPaciente.getFarmacos()).isEqualTo(DEFAULT_FARMACOS);
    }

    @Test
    @Transactional
    public void createPacienteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente with an existing ID
        paciente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombresIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setNombres(null);

        // Create the Paciente, which fails.

        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidosIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setApellidos(null);

        // Create the Paciente, which fails.

        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCedulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = pacienteRepository.findAll().size();
        // set the field null
        paciente.setCedula(null);

        // Create the Paciente, which fails.

        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc.perform(get("/api/pacientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES.toString())))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS.toString())))
            .andExpect(jsonPath("$.[*].cedula").value(hasItem(DEFAULT_CEDULA.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL.toString())))
            .andExpect(jsonPath("$.[*].alergico").value(hasItem(DEFAULT_ALERGICO.toString())))
            .andExpect(jsonPath("$.[*].diabetes").value(hasItem(DEFAULT_DIABETES.toString())))
            .andExpect(jsonPath("$.[*].presionAlta").value(hasItem(DEFAULT_PRESION_ALTA.toString())))
            .andExpect(jsonPath("$.[*].tiroides").value(hasItem(DEFAULT_TIROIDES.toString())))
            .andExpect(jsonPath("$.[*].cicatrizacion").value(hasItem(DEFAULT_CICATRIZACION.toString())))
            .andExpect(jsonPath("$.[*].cardiaca").value(hasItem(DEFAULT_CARDIACA.toString())))
            .andExpect(jsonPath("$.[*].farmacos").value(hasItem(DEFAULT_FARMACOS.toString())));
    }

    @Test
    @Transactional
    public void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES.toString()))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS.toString()))
            .andExpect(jsonPath("$.cedula").value(DEFAULT_CEDULA.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL.toString()))
            .andExpect(jsonPath("$.alergico").value(DEFAULT_ALERGICO.toString()))
            .andExpect(jsonPath("$.diabetes").value(DEFAULT_DIABETES.toString()))
            .andExpect(jsonPath("$.presionAlta").value(DEFAULT_PRESION_ALTA.toString()))
            .andExpect(jsonPath("$.tiroides").value(DEFAULT_TIROIDES.toString()))
            .andExpect(jsonPath("$.cicatrizacion").value(DEFAULT_CICATRIZACION.toString()))
            .andExpect(jsonPath("$.cardiaca").value(DEFAULT_CARDIACA.toString()))
            .andExpect(jsonPath("$.farmacos").value(DEFAULT_FARMACOS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaciente() throws Exception {
        // Initialize the database
        pacienteService.save(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findOne(paciente.getId());
        updatedPaciente
            .fecha(UPDATED_FECHA)
            .nombres(UPDATED_NOMBRES)
            .apellidos(UPDATED_APELLIDOS)
            .cedula(UPDATED_CEDULA)
            .telefono(UPDATED_TELEFONO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .mail(UPDATED_MAIL)
            .alergico(UPDATED_ALERGICO)
            .diabetes(UPDATED_DIABETES)
            .presionAlta(UPDATED_PRESION_ALTA)
            .tiroides(UPDATED_TIROIDES)
            .cicatrizacion(UPDATED_CICATRIZACION)
            .cardiaca(UPDATED_CARDIACA)
            .farmacos(UPDATED_FARMACOS);

        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaciente)))
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testPaciente.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testPaciente.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testPaciente.getCedula()).isEqualTo(UPDATED_CEDULA);
        assertThat(testPaciente.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testPaciente.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testPaciente.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testPaciente.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testPaciente.getAlergico()).isEqualTo(UPDATED_ALERGICO);
        assertThat(testPaciente.getDiabetes()).isEqualTo(UPDATED_DIABETES);
        assertThat(testPaciente.getPresionAlta()).isEqualTo(UPDATED_PRESION_ALTA);
        assertThat(testPaciente.getTiroides()).isEqualTo(UPDATED_TIROIDES);
        assertThat(testPaciente.getCicatrizacion()).isEqualTo(UPDATED_CICATRIZACION);
        assertThat(testPaciente.getCardiaca()).isEqualTo(UPDATED_CARDIACA);
        assertThat(testPaciente.getFarmacos()).isEqualTo(UPDATED_FARMACOS);
    }

    @Test
    @Transactional
    public void updateNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Create the Paciente

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaciente() throws Exception {
        // Initialize the database
        pacienteService.save(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Get the paciente
        restPacienteMockMvc.perform(delete("/api/pacientes/{id}", paciente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = new Paciente();
        paciente1.setId(1L);
        Paciente paciente2 = new Paciente();
        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);
        paciente2.setId(2L);
        assertThat(paciente1).isNotEqualTo(paciente2);
        paciente1.setId(null);
        assertThat(paciente1).isNotEqualTo(paciente2);
    }
}
