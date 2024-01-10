package mate.academy.bookstore.repository.cartitem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Delete all items from users shopping cart")
    @Sql(scripts = {
            "classpath:databases/users/add-user-to-users-table.sql",
            "classpath:databases/shoppingcarts/add-users-shopping-cart-to-shopping-cart-table.sql",
            "classpath:databases/books/add-few-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:databases/users/delete-user-from-users-table.sql",
            "classpath:databases/shoppingcarts/"
                    + "remove-all-shopcarts-and-cartitems-from-sc-and-ci-table.sql",
            "classpath:databases/books/remove-all-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCartItemsByShoppingCart() {
        User user = new User();
        user.setId(5L);
        user.setEmail("user@example.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(5L);
        shoppingCart.setUser(user);

        Book triumph = new Book();
        triumph.setId(10L);
        triumph.setTitle("Triumph");

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(triumph);
        cartItem.setQuantity(5);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        cartItemRepository.save(cartItem);
        assertEquals(1, shoppingCart.getCartItems().size());

        cartItemRepository.deleteCartItemsByShoppingCart(shoppingCart);
        shoppingCart = shoppingCartRepository.findByUserEmail(user.getEmail());
        assertEquals(0, shoppingCart.getCartItems().size());
    }
}
