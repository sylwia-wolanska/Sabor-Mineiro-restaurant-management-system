package pjatk.mas.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.dto.request.CreateEmployeeRequestDTO;
import pjatk.mas.dto.response.EmployeeSummaryDTO;
import pjatk.mas.mappers.PersonMapper;
import pjatk.mas.models.*;
import pjatk.mas.repositories.*;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final WaiterRepository waiterRepository;
    private final ChefRepository chefRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final PersonRepository personRepository;
    private final OrderRepository orderRepository;
    private final PersonMapper personMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeService(WaiterRepository waiterRepository, ChefRepository chefRepository, DeliveryDriverRepository deliveryDriverRepository,
                           PersonRepository personRepository, OrderRepository orderRepository, PersonMapper personMapper,
                           BCryptPasswordEncoder passwordEncoder) {
        this.waiterRepository = waiterRepository;
        this.chefRepository = chefRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.personRepository = personRepository;
        this.orderRepository = orderRepository;
        this.personMapper = personMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryDTO> getAllEmployees() {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Employee)
                .map(p -> personMapper.toEmployeeSummaryDTO((Employee) p))
                .toList();
    }

    public Employee createEmployee(CreateEmployeeRequestDTO dto) {
        if (personRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' is already taken");
        }

        Employee employee = switch (dto.getRole().toUpperCase()) {
            case "WAITER" -> new Waiter(dto.getName(), dto.getSurname(), dto.getSalaryPerHour());
            case "CHEF" -> new Chef(dto.getName(), dto.getSurname(), dto.getSalaryPerHour());
            case "DELIVERY_DRIVER" -> {
                if (dto.getDrivingLicenseNumber() == null || dto.getDrivingLicenseNumber().isBlank()) {
                    throw new IllegalArgumentException("Driving license number is required for delivery drivers");
                }
                yield new DeliveryDriver(dto.getName(), dto.getSurname(), dto.getSalaryPerHour(), dto.getDrivingLicenseNumber());
            }
            default -> throw new IllegalArgumentException("Unknown role: " + dto.getRole());
        };

        employee.setUsername(dto.getUsername());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        return personRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        if (!(person instanceof Employee)) {
            throw new IllegalArgumentException("Person is not an employee");
        }
        if (person instanceof Manager) {
            throw new IllegalArgumentException("Cannot delete the Manager");
        }

        if (person instanceof Waiter waiter) {
            waiter.getProcessedOrders().forEach(order -> order.setWaiter(null));
            orderRepository.saveAll(waiter.getProcessedOrders());
        }

        if (person instanceof DeliveryDriver driver) {
            driver.getDeliveryOrders().forEach(order -> {
                order.setDeliveryDriver(null);
                orderRepository.save(order);
            });
        }

        personRepository.delete(person);
    }
}