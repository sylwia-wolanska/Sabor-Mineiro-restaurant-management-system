package pjatk.mas.mappers;

import org.springframework.stereotype.Component;
import pjatk.mas.dto.response.*;
import pjatk.mas.models.*;

import java.util.List;

@Component
public class OrderMapper {

    public OrderSummaryDTO toSummaryDTO(Order order) {
        return new OrderSummaryDTO(
                order.getId(),
                order.getClient().getName() + " " + order.getClient().getSurname(),
                order.isPickup() ? "PICKUP" : "DELIVERY",
                order.getOrderStatus().name(),
                order.getTotalPrice(),
                order.countEstimatedPrepTime()
        );
    }

    public OrderDetailDTO toDetailDTO(Order order) {
        List<MenuItemQuantityDTO> items = order.getMenuItemQuantities().stream()
                .map(miq -> new MenuItemQuantityDTO(
                        miq.getMenuItem().getId(),
                        miq.getMenuItem().getName(),
                        miq.getMenuItem().getPreparationTime(),
                        miq.getMenuItem().getPrice(),
                        miq.getQuantity(),
                        miq.getSubtotal()
                ))
                .toList();

        String fullAddress = null;
        String deliveryDriverName = null;
        java.time.LocalTime hourOfPickup = null;

        if (order instanceof DeliveryOrder delivery) {
            fullAddress = delivery.getFullAddress();
            if (delivery.getDeliveryDriver() != null) {
                deliveryDriverName = delivery.getDeliveryDriver().getName() + " " + delivery.getDeliveryDriver().getSurname();
            }
        } else if (order instanceof PickupOrder pickup) {
            hourOfPickup = pickup.getHourOfPickup();
        }

        String waiterName = null;
        Long waiterId = null;
        if (order.getWaiter() != null) {
            waiterName = order.getWaiter().getName() + " " + order.getWaiter().getSurname();
            waiterId = order.getWaiter().getId();
        }

        return new OrderDetailDTO(
                order.getId(),
                order.getClient().getName() + " " + order.getClient().getSurname(),
                order.isPickup() ? "PICKUP" : "DELIVERY",
                order.getOrderStatus().name(),
                order.getTotalPrice(),
                order.countEstimatedPrepTime(),
                items,
                hourOfPickup,
                fullAddress,
                deliveryDriverName,
                waiterName,
                waiterId
        );
    }
}