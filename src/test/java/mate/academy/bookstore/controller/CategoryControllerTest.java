package mate.academy.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.category.CategoryDto;
import mate.academy.bookstore.dto.category.CreateCategoryRequestDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "databases/categories/add-two-categories-to-categories-table.sql"
                    )
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    private static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "databases/categories/remove-all-categories-from-categories-table.sql"
                    )
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new category")
    @Sql(scripts = {
            "classpath:databases/categories/delete-poem-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Poem")
                .setDescription("less interesting utopia");

        CategoryDto expected = new CategoryDto()
                .setName(createCategoryRequestDto.getName())
                .setDescription(createCategoryRequestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(createCategoryRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        CategoryDto actual = objectMapper.readValue(contentAsString, CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get all categories")
    void getAll_GivenCategories_ShouldReturnAllCategories() throws Exception {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto()
                .setId(1L)
                .setName("Anti-utopia")
                .setDescription("interested books"));
        expected.add(new CategoryDto()
                .setId(2L)
                .setName("Fiction")
                .setDescription("interested books"));

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                CategoryDto[].class);
        assertEquals(2, actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @DisplayName("Get category by id")
    @WithMockUser(username = "user")
    void getCategoryById2_CategoryAvailable_ShouldReturnActualCategory() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(2L)
                .setName("Fiction")
                .setDescription("interested books");
        MvcResult result = mockMvc.perform(
                        get("/categories/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete category by id")
    @Sql(scripts = {
            "classpath:databases/categories/add-poem-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/categories/delete-poem-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_WithAvailableId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        delete("/categories/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        int expected = 204;
        int actual = result.getResponse().getStatus();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update category by id")
    @Sql(scripts = {
            "classpath:databases/categories/add-poem-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/categories/delete-poem-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_WithAvailableId_ShouldReturnUpdateCategory() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setId(3L)
                .setName("Poem")
                .setDescription("UPDATE BOOKS");

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                        put("/categories/3")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        CategoryDto actual = objectMapper.readValue(contentAsString, CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
