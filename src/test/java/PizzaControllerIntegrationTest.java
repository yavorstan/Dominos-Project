import com.example.demo.Application;
import com.example.demo.controller.PizzaController;
import com.example.demo.controller.SessionManager;
import com.example.demo.exceptions.RestExceptionHandler;
import com.example.demo.model.dto.GetIngredientDTO;
import com.example.demo.model.dto.GetPizzaDTO;
import com.example.demo.model.dto.PostPizzaDTO;
import com.example.demo.model.entity.Ingredient;
import com.example.demo.model.entity.Pizza;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.PizzaRepository;
import com.example.demo.service.PizzaService;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PizzaControllerIntegrationTest {

    public static final String DEFAULT_PIZZA_NAME = "TestPizza";

    public static final BigDecimal DEFAULT_PIZZA_PRICE = new BigDecimal(12);

    public static final String DEFAULT_FIRST_INGREDIENT_NAME = "TestIngredient";

    public static final BigDecimal DEFAULT_INGREDIENT_PRICE = new BigDecimal(2);

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private IngredientRepository ingredientRepository;

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

    private MockMvc restPizzaMockMvc;

    private PostPizzaDTO postPizzaDTO;

    private Ingredient ingredient;

    private Pizza pizza;

    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    private MockHttpSession session;


    @Before
    public void initTest() {
        ingredient = createIngredient(em);
        postPizzaDTO = createPostPizzaDTO(em, ingredient);
        pizza = createPizzaEntity(em);
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
        final PizzaController pizzaController = new PizzaController(sessionManager, pizzaService);
        this.restPizzaMockMvc = MockMvcBuilders.standaloneSetup(pizzaController)
                .setControllerAdvice(new RestExceptionHandler())
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    public Pizza createPizzaEntity(EntityManager em) {
        Pizza pizza = new Pizza();
        pizza.setName(DEFAULT_PIZZA_NAME);
        pizza.setPrice(DEFAULT_PIZZA_PRICE);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        pizza.setIngredients(ingredients);
        return pizza;
    }

    public static PostPizzaDTO createPostPizzaDTO(EntityManager em, Ingredient ingredient) {
        PostPizzaDTO postPizzaDTO = new PostPizzaDTO();
        postPizzaDTO.setName(DEFAULT_PIZZA_NAME);
        postPizzaDTO.setPrice(DEFAULT_PIZZA_PRICE);

        ArrayList<Long> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getId());
        postPizzaDTO.setIngredients(ingredients);
        return postPizzaDTO;
    }

    public static GetPizzaDTO createGetPizzaDTO(EntityManager em) {
        GetPizzaDTO getPizzaDTO = new GetPizzaDTO();
        getPizzaDTO.setName(DEFAULT_PIZZA_NAME);
        getPizzaDTO.setPrice(DEFAULT_PIZZA_PRICE);

        GetIngredientDTO ingredient = createGetIngredientDTO(em);

        ArrayList<GetIngredientDTO> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        getPizzaDTO.setIngredients(ingredients);
        return getPizzaDTO;
    }

    public static GetIngredientDTO createGetIngredientDTO(EntityManager em) {
        GetIngredientDTO getIngredientDTO = new GetIngredientDTO();
        getIngredientDTO.setName(DEFAULT_FIRST_INGREDIENT_NAME);
        getIngredientDTO.setPrice(DEFAULT_INGREDIENT_PRICE);
        return getIngredientDTO;
    }

    private static Ingredient createIngredient(EntityManager em) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(DEFAULT_FIRST_INGREDIENT_NAME);
        ingredient.setPrice(DEFAULT_INGREDIENT_PRICE);
        return ingredient;
    }

    @Test
    @Transactional
    public void createPizza() throws Exception {
        int databaseSizeBeforeCreate = pizzaRepository.findAll().size();

        Ingredient ingredient = createIngredient(em);
        ingredient = ingredientRepository.saveAndFlush(ingredient);

        ArrayList<Long> ingredients = new ArrayList<>();
        ingredients.add(ingredient.getId());
        postPizzaDTO.setIngredients(ingredients);

        restPizzaMockMvc.perform(post("/pizza")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postPizzaDTO)))
                .andExpect(status().isCreated());

        List<Pizza> pizzaList = pizzaRepository.findAll();
        assertThat(pizzaList).hasSize(databaseSizeBeforeCreate + 1);

        Pizza testPizza = pizzaList.get(pizzaList.size() - 1);
        assertThat(testPizza.getName()).isEqualTo(DEFAULT_PIZZA_NAME);
        assertThat(testPizza.getPrice()).isEqualTo(DEFAULT_PIZZA_PRICE);
        assertThat(testPizza.getIngredients().get(0).getName()).isEqualTo(DEFAULT_FIRST_INGREDIENT_NAME);
    }


    @Test
    @Transactional
    public void checkPizzaNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pizzaRepository.findAll().size();
        postPizzaDTO.setName(null);

        restPizzaMockMvc.perform(post("/pizza")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postPizzaDTO)))
                .andExpect(status().isUnprocessableEntity());

        List<Pizza> pizzaList = pizzaRepository.findAll();
        assertThat(pizzaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPizzaPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = pizzaRepository.findAll().size();
        postPizzaDTO.setPrice(null);

        restPizzaMockMvc.perform(post("/pizza")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postPizzaDTO)))
                .andExpect(status().isUnprocessableEntity());

        List<Pizza> pizzaList = pizzaRepository.findAll();
        assertThat(pizzaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPizzaIngredientsAreRequired() throws Exception {
        int databaseSizeBeforeTest = pizzaRepository.findAll().size();
        postPizzaDTO.setIngredients(null);

        restPizzaMockMvc.perform(post("/pizza")
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(postPizzaDTO)))
                .andExpect(status().isUnprocessableEntity());

        List<Pizza> pizzaList = pizzaRepository.findAll();
        assertThat(pizzaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void deletePizza() throws Exception {
        int databaseSizeBeforeTest = pizzaRepository.findAll().size();
        pizzaRepository.save(pizza);

        restPizzaMockMvc.perform(delete("/pizza/{id}", pizza.getId())
                .session(session)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Pizza> pizzaList = pizzaRepository.findAll();
        assertThat(pizzaList).hasSize(databaseSizeBeforeTest);
    }
}