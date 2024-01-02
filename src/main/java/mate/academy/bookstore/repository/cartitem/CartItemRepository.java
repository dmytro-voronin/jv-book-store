package mate.academy.bookstore.repository.cartitem;

import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Transactional
    void deleteCartItemsByShoppingCart(ShoppingCart shoppingCart);
}
