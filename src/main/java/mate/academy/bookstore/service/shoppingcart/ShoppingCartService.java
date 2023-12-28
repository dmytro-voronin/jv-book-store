package mate.academy.bookstore.service.shoppingcart;

import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.QuantityRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    void registerNewShoppingCart(User user);

    void addCartItem(Authentication authentication, CartItemRequestDto requestDto);

    ShoppingCartResponseDto getUsersShoppingCart(Authentication authentication);

    void updateQuantityOfBookById(
            Long cartItemId,
            QuantityRequestDto quantityRequestDto);

    void removeItemFromCartById(Long cartItemId);
}
