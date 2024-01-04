package mate.academy.bookstore.mapper;

import java.util.stream.Collectors;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.mapper.impl.OrderItemMapperImpl;
import mate.academy.bookstore.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @AfterMapping
    default void setUserIdAndOrderItems(
            @MappingTarget OrderResponseDto orderResponseDto,
            Order order
    ) {
        OrderItemMapper orderItemMapper = new OrderItemMapperImpl();
        orderResponseDto.setUserId(order.getUser().getId());
        orderResponseDto.setOrderItemDtos(order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
    }
}
