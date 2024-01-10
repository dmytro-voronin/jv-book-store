package mate.academy.bookstore.repository.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mate.academy.bookstore.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find users shopping cart by email")
    @Sql(scripts = {
            "classpath:databases/users/add-user-to-users-table.sql",
            "classpath:databases/shoppingcarts/add-users-shopping-cart-to-shopping-cart-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/users/delete-user-from-users-table.sql",
            "classpath:databases/shoppingcarts/"
                    + "remove-all-shopcarts-and-cartitems-from-sc-and-ci-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserEmail_ShouldReturnEmptyShoppingCart() {
        String email = "user@example.com";

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email);

        assertNotNull(shoppingCart);
        assertEquals(email, shoppingCart.getUser().getEmail());
        assertTrue(shoppingCart.getCartItems().isEmpty());
    }
}
