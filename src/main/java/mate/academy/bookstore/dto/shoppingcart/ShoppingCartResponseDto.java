package mate.academy.bookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.bookstore.dto.cartitem.CartItemResponseDto;

@Data
@Accessors(chain = true)
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
