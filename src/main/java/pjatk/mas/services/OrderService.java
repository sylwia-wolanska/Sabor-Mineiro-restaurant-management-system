package pjatk.mas.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.dto.response.OrderDetailDTO;
import pjatk.mas.dto.response.OrderSummaryDTO;
import pjatk.mas.mappers.OrderMapper;
import pjatk.mas.models.*;
import pjatk.mas.repositories.*;
import pjatk.mas.session.OrderWizardSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final WaiterRepository waiterRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository, WaiterRepository waiterRepository,
                        ClientRepository clientRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.waiterRepository = waiterRepository;
        this.clientRepository = clientRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryDTO> getOrdersForPerson(Person person) {
        if (person instanceof Client client) {
            Client managed = clientRepository.findById(client.getId()).orElseThrow(() -> new RuntimeException("Client not found"));
            return orderRepository.findByClient(managed).stream().map(orderMapper::toSummaryDTO).toList();
        }
        return orderRepository.findAll().stream().map(orderMapper::toSummaryDTO).toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailDTO getOrderDetailById(Long id, Person person) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));

        if (person instanceof Client client && !order.getClient().getId().equals(client.getId())) {
            throw new IllegalStateException("Access denied");
        }

        return orderMapper.toDetailDTO(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public Order createOrder(OrderWizardSession session, Client sessionClient) {
        Client client = clientRepository.findById(sessionClient.getId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (!session.hasItems()) {
            throw new IllegalStateException("Cannot create an order without items");
        }

        Order order;
        if (session.isPickupAtRestaurant()) {
            if (session.getHourOfPickup() == null) {
                throw new IllegalArgumentException("Pickup time is required");
            }
            order = new PickupOrder(client, session.getHourOfPickup());
        } else {
            if (session.getStreet() == null || session.getBuildingNumber() == null) {
                throw new IllegalArgumentException("Full address is required for delivery");
            }
            order = new DeliveryOrder(client, session.getStreet(), session.getBuildingNumber(), session.getApartmentNumber());
        }

        for (Map.Entry<Long, Integer> entry : session.getSelectedItems().entrySet()) {
            MenuItem menuItem = menuItemRepository.findById(entry.getKey()).orElseThrow(() -> new RuntimeException("MenuItem not found"));
            order.addMenuItemQuantity(menuItem, entry.getValue());
        }

        waiterRepository.findAll().stream().findFirst().ifPresent(order::setWaiter);

        Order savedOrder = orderRepository.save(order);
        client.addOrder(savedOrder);
        return savedOrder;
    }

    public OrderDetailDTO updateOrderStatus(Long orderId, OrderStatus newStatus, Person person) {
        Order order = getOrderById(orderId);
        OrderStatus current = order.getOrderStatus();

        boolean valid = switch (current) {
            case INITIALIZED -> newStatus == OrderStatus.IN_PREPARATION || newStatus == OrderStatus.CANCELLED;
            case IN_PREPARATION -> newStatus == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new IllegalStateException("Invalid status transition: " + current + " -> " + newStatus);
        }

        order.setOrderStatus(newStatus);

        if (newStatus == OrderStatus.CANCELLED) {
            order.setCancelledAt(LocalDateTime.now());
        }

        Order saved = orderRepository.save(order);
        return orderMapper.toDetailDTO(saved);
    }
}