package mate.academy.bookstore.service.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.QuantityRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.service.shoppingcart.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Add cart item to the shopping cart")
    void addCartItem_Success() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(shoppingCartRepository.findByUserEmail("user@example.com"))
                .thenReturn(new ShoppingCart());
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(cartItemRepository.save(any())).thenReturn(new CartItem());

        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 2);
        shoppingCartService.addCartItem(authentication, requestDto);

        verify(cartItemRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Getting users shopping cart")
    void getUsersShoppingCart_ShouldReturnShoppingCartResponseDto() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(shoppingCartRepository.findByUserEmail("user@example.com"))
                .thenReturn(new ShoppingCart());
        when(shoppingCartMapper.toDto(any())).thenReturn(new ShoppingCartResponseDto());

        ShoppingCartResponseDto result = shoppingCartService.getUsersShoppingCart(authentication);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Update quantity of book by id")
    void updateQuantityOfBookById_Success() {
        Long cartItemId = 1L;
        QuantityRequestDto quantityRequestDto = new QuantityRequestDto()
                .setQuantity(3);
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(new CartItem()));
        when(cartItemRepository.save(any())).thenReturn(new CartItem());

        shoppingCartService.updateQuantityOfBookById(cartItemId, quantityRequestDto);

        verify(cartItemRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Delete item by id")
    void removeItemFromCartById_Success() {
        Long cartItemId = 1L;

        shoppingCartService.removeItemFromCartById(cartItemId);

        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }

    @Test
    @DisplayName("Clear users shopping cart")
    void clearShoppingCart_Success() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(shoppingCartRepository.findByUserEmail("user@example.com"))
                .thenReturn(new ShoppingCart());

        shoppingCartService.clearShoppingCart(authentication);

        verify(cartItemRepository, times(1))
                .deleteCartItemsByShoppingCart(any());
    }
}
