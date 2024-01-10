package mate.academy.bookstore.dto.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QuantityRequestDto {
    private int quantity;
}
