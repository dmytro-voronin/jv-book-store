package mate.academy.bookstore.service.order;

import java.util.List;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.ShippingAddressRequestDto;
import mate.academy.bookstore.dto.order.StatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import org.springframework.security.core.Authentication;

public interface OrderService {
    void createOrder(
            Authentication authentication,
            ShippingAddressRequestDto requestDto
    );

    List<OrderResponseDto> getAll(Authentication authentication);

    void updateStatus(Long id, StatusRequestDto requestDto);

    List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId);

    OrderItemDto getByIdAndOrderId(Long itemId, Long orderId);
}
