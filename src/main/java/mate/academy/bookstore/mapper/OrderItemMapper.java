package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.orderitem.OrderItemDto;
import mate.academy.bookstore.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    @AfterMapping
    default void setBookId(
            @MappingTarget OrderItemDto orderItemDto,
            OrderItem orderItem
    ) {
        orderItemDto.setBookId(orderItem.getBook().getId());
    }
}
