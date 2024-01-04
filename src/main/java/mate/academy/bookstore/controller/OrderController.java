package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.ShippingAddressRequestDto;
import mate.academy.bookstore.dto.order.StatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.service.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
@Tag(name = "Order management",
        description = "Endpoints for managing orders and items in it")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order",
            description = "Creation a new order based on the user's shopping cart and address")
    public void createOrder(
            Authentication authentication,
            @RequestBody @Valid ShippingAddressRequestDto requestDto
    ) {
        orderService.createOrder(authentication, requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders of the logged-in user",
            description = "Getting a list of orders of the logged-in user")
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        return orderService.getAll(authentication);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status by order id",
            description = "Updating order status by order id, only for admins")
    public void updateOrderStatusById(
            @PathVariable Long id,
            @RequestBody @Valid StatusRequestDto requestDto
    ) {
        orderService.updateStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items by order id",
            description = "Getting a list of order items by order id")
    public List<OrderItemDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getAllOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get item by id and order id",
            description = "Getting an item of order by id and order's id")
    public OrderItemDto getOrderItemByIdAndOrderId(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getByIdAndOrderId(itemId, orderId);
    }
}
