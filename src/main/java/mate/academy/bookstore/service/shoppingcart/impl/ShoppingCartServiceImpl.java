package mate.academy.bookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.QuantityRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.cartitem.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void addCartItem(Authentication authentication, CartItemRequestDto requestDto) {
        CartItem item = new CartItem();
        item.setShoppingCart(shoppingCartRepository.findByUserEmail(authentication.getName()));
        item.setBook(bookRepository.findById(requestDto.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + requestDto.bookId())
        ));
        item.setQuantity(requestDto.quantity());
        cartItemRepository.save(item);
    }

    @Override
    public ShoppingCartResponseDto getUsersShoppingCart(Authentication authentication) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserEmail(authentication.getName());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void updateQuantityOfBookById(Long cartItemId, QuantityRequestDto quantityRequestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()
                -> new EntityNotFoundException(
                "Can't update quantity, item with id " + cartItemId
                        + " doesn't exist."
        ));
        cartItem.setQuantity(quantityRequestDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCartById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
