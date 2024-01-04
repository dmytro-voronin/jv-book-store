package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.cartitem.CartItemRequestDto;
import mate.academy.bookstore.dto.cartitem.QuantityRequestDto;
import mate.academy.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart and items in it")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add an book to the shopping cart",
            description = "Add an book to the shopping cart of the logged-in user")
    public void addCartItem(Authentication authentication,
                            @RequestBody CartItemRequestDto requestDto) {
        shoppingCartService.addCartItem(authentication, requestDto);
    }

    @GetMapping
    @Operation(summary = "Get a shopping cart of the logged-in user",
            description = "Get a shopping cart with all books of the logged-in user")
    public ShoppingCartResponseDto getUsersShoppingCart(Authentication authentication) {
        return shoppingCartService.getUsersShoppingCart(authentication);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Update quantity of a book in the shopping cart by id")
    public void updateQuantity(@PathVariable Long cartItemId,
                               @RequestBody QuantityRequestDto quantityRequestDto) {
        shoppingCartService.updateQuantityOfBookById(cartItemId, quantityRequestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Remove a book from the shopping cart by id")
    public void removeFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.removeItemFromCartById(cartItemId);
    }
}
