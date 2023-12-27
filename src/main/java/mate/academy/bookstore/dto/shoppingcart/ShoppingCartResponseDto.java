package mate.academy.bookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.bookstore.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
