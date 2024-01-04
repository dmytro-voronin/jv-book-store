package mate.academy.bookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.ShippingAddressRequestDto;
import mate.academy.bookstore.dto.order.StatusRequestDto;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.orderitem.OrderItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemMapper cartItemMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public void createOrder(Authentication authentication, ShippingAddressRequestDto requestDto) {
        Order order = new Order();
        order.setUser(userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Can`t find user by name!")));
        order.setStatus(Order.Status.NEW);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderItems(cartRepository.findByUserEmail(authentication.getName())
                .getCartItems().stream()
                .map(cartItemMapper::toOrderItem)
                .collect(Collectors.toSet()));
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order savedOrder = orderRepository.save(order);
        savedOrder.getOrderItems().forEach(orderItem
                -> orderItem.setOrder(savedOrder));
        orderItemRepository.saveAll(savedOrder.getOrderItems());
        shoppingCartService.clearShoppingCart(authentication);
    }

    @Override
    public List<OrderResponseDto> getAll(Authentication authentication) {
        return orderRepository.findAllByUserEmail(authentication.getName()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long id, StatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + id)
        );
        order.setStatus(Order.Status.valueOf(requestDto.getStatus()));
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemDto> getAllOrderItemsByOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find items of order with id: " + orderId)
        ).getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto getByIdAndOrderId(Long itemId, Long orderId) {
        return orderItemMapper.toDto(orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId)
        ).getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find item by id: " + itemId + "of order with id: " + orderId
                        )
                ));
    }
}
