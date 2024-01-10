package mate.academy.bookstore.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.CartItemResponseDto;
import mate.academy.bookstore.dto.cartitem.QuantityRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.service.shoppingcart.ShoppingCartService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
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
                            "databases/shoppingcarts/"
                                    + "remove-all-shopcarts-and-cartitems-from-sc-and-ci-table.sql"
                    )
            );
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Add cart item to the shopping cart")
    void addCartItem_Success() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(10L, 5);
        mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(shoppingCartService).addCartItem(any(Authentication.class),
                any(CartItemRequestDto.class));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Getting users shopping cart")
    void getUsersShoppingCart_Success() throws Exception {
        Set<CartItemResponseDto> items = new HashSet<>();
        items.add(new CartItemResponseDto()
                .setId(1L)
                .setBookTitle("Top Gear")
                .setBookId(5L)
                .setQuantity(6));
        ShoppingCartResponseDto responseDto = new ShoppingCartResponseDto()
                .setUserId(1L)
                .setId(2L)
                .setCartItems(items);
        when(shoppingCartService.getUsersShoppingCart(any(Authentication.class)))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Updating quantity of items in cart")
    void updateQuantity_Success() throws Exception {
        Long cartItemId = 1L;
        QuantityRequestDto quantityRequestDto = new QuantityRequestDto()
                .setQuantity(3);

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/cart-items/{cartItemId}", cartItemId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(quantityRequestDto)))
                .andExpect(status().isOk());

        verify(shoppingCartService).updateQuantityOfBookById(cartItemId, quantityRequestDto);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Remove item from cart")
    void removeFromCart_Success() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/cart-items/{cartItemId}", cartItemId))
                .andExpect(status().isNoContent());

        verify(shoppingCartService).removeItemFromCartById(cartItemId);
    }
}
