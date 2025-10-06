package Entities;

import Entities.HelperClasses.Category;
import Entities.HelperClasses.Colors;
import Entities.HelperClasses.ShippingMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record Items(
        UUID id,
        String name,
        Category category,
        Colors color,
        int[] size,
        int count,
        BigDecimal price,
        ShippingMethod shippingMethod
) {
}
