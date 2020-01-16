import com.example.demo.Application;
import com.example.demo.controller.IngredientController;
import com.example.demo.controller.SessionManager;
import com.example.demo.exceptions.RestExceptionHandler;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.PostIngredientDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.service.IngredientService;
import com.example.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import util.TestUtil;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class IngredientControllerIntegrationTest {

    public static final String DEFAULT_INGREDIENT_NAME = "TestIngredient";

    public static final BigDecimal DEFAULT_INGREDIENT_PRICE = new BigDecimal(2);

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    private MockMvc restIngredientMockMvc;

    private PostIngredientDTO postIngredientDTO;

    private Ingredient ingredient;

    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    private MockHttpSession session;


    @Before
    public void initTest() {
        postIngredientDTO = createPostIngredientDTO(em);
        ingredient = createIngredientEntity(em);

        mockHttpServletRequest.setSession(session);
        session = new MockHttpSession();
        session.setAttribute("email", "testMail");

        sessionManager = new SessionManager(userService);
        sessionManager.setSessionAttributes(session, session.getAttribute("email").toString());
        session.setAttribute("isAdmin", true);

    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IngredientController ingredientController = new IngredientController(sessionManager, ingredientService);
        this.restIngredientMockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();


    }

    public Ingredient createIngredientEntity(EntityManager em) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(DEFAULT_INGREDIENT_NAME);
        ingredient.setPrice(DEFAULT_INGREDIENT_PRICE);
        return ingredient;
    }

    public static PostIngredientDTO createPostIngredientDTO(EntityManager em) {
        PostIngredientDTO postIngredientDTO = new PostIngredientDTO();
        postIngredientDTO.setName(DEFAULT_INGREDIENT_NAME);
        postIngredientDTO.setPrice(DEFAULT_INGREDIENT_PRICE);
        return postIngredientDTO;
    }

    public static GetIngredientDTO createGetIngredientDTO(EntityManager em) {
        GetIngredientDTO getIngredientDTO = new GetIngredientDTO();
        getIngredientDTO.setName(DEFAULT_INGREDIENT_NAME);
        getIngredientDTO.setPrice(DEFAULT_INGREDIENT_PRICE);
        return getIngredientDTO;
    }

    @Test
    @Transactional
    public void createIngredient() throws Exception {
        int databaseSizeBeforeCreate = ingredientRepository.findAll().size();

        restIngredientMockMvc.perform(post("/ingredients")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postIngredientDTO)))
                .andExpect(status().isCreated());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeCreate + 1);

        Ingredient testIngredient = ingredientList.get(ingredientList.size() - 1);
        assertThat(testIngredient.getName()).isEqualTo(DEFAULT_INGREDIENT_NAME);
        assertThat(testIngredient.getPrice()).isEqualTo(DEFAULT_INGREDIENT_PRICE);
    }


    @Test
    @Transactional
    public void checkIngredientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();

        postIngredientDTO.setName(null);

        restIngredientMockMvc.perform(post("/ingredients")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postIngredientDTO)))
                .andExpect(status().isUnprocessableEntity());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIngredientPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();

        postIngredientDTO.setPrice(null);

        restIngredientMockMvc.perform(post("/ingredients")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postIngredientDTO)))
                .andExpect(status().isUnprocessableEntity());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void deleteIngredient() throws Exception {
        int databaseSizeBeforeTest = ingredientRepository.findAll().size();
        ingredientRepository.save(ingredient);

        restIngredientMockMvc.perform(delete("/ingredients/{id}", ingredient.getId())
                .session(session)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Ingredient> ingredientList = ingredientRepository.findAll();
        assertThat(ingredientList).hasSize(databaseSizeBeforeTest);
    }
}
