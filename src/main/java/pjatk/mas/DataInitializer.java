package pjatk.mas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjatk.mas.models.*;
import pjatk.mas.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final WaiterRepository waiterRepository;
    private final ChefRepository chefRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final ManagerRepository managerRepository;
    private final VehicleRepository vehicleRepository;
    private final DiningTableRepository diningTableRepository;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(
            ClientRepository clientRepository,
            WaiterRepository waiterRepository,
            ChefRepository chefRepository,
            DeliveryDriverRepository deliveryDriverRepository,
            ManagerRepository managerRepository,
            VehicleRepository vehicleRepository,
            DiningTableRepository diningTableRepository,
            MenuItemRepository menuItemRepository,
            IngredientRepository ingredientRepository,
            OrderRepository orderRepository,
            ReservationRepository reservationRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.waiterRepository = waiterRepository;
        this.chefRepository = chefRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.managerRepository = managerRepository;
        this.vehicleRepository = vehicleRepository;
        this.diningTableRepository = diningTableRepository;
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
        this.orderRepository = orderRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        Ingredient feijaoPreto = new Ingredient("Feijao Preto", new BigDecimal("0.028"));
        Ingredient feijaoCarioca = new Ingredient("Feijao Carioca", new BigDecimal("0.030"));
        Ingredient farinhaMandioca = new Ingredient("Farinha de Mandioca", new BigDecimal("0.015"));
        Ingredient linguica = new Ingredient("Linguica Calabresa", new BigDecimal("0.070"));
        Ingredient bacon = new Ingredient("Bacon", new BigDecimal("0.060"));
        Ingredient ovos = new Ingredient("Ovos", new BigDecimal("0.600"));
        Ingredient couve = new Ingredient("Couve Mineira", new BigDecimal("0.025"));
        Ingredient queijoCanastra = new Ingredient("Queijo Canastra", new BigDecimal("0.100"));
        Ingredient polvilhoAzedo = new Ingredient("Polvilho Azedo", new BigDecimal("0.035"));
        Ingredient polvilhoDoce = new Ingredient("Polvilho Doce", new BigDecimal("0.030"));
        Ingredient frangoCaipira = new Ingredient("Frango Caipira", new BigDecimal("0.070"));
        Ingredient costela = new Ingredient("Costelinha de Porco", new BigDecimal("0.090"));
        Ingredient lombo = new Ingredient("Lombo Suino", new BigDecimal("0.110"));
        Ingredient milhoVerde = new Ingredient("Milho Verde", new BigDecimal("0.047"));
        Ingredient canjiquinha = new Ingredient("Canjiquinha", new BigDecimal("0.040"));
        Ingredient alho = new Ingredient("Alho", new BigDecimal("0.040"));
        Ingredient cebola = new Ingredient("Cebola", new BigDecimal("0.015"));
        Ingredient sangueGalinha = new Ingredient("Sangue de Galinha", new BigDecimal("0.020"));
        Ingredient doceLeite = new Ingredient("Doce de Leite", new BigDecimal("0.080"));
        Ingredient manteigaGarrafa = new Ingredient("Manteiga de Garrafa", new BigDecimal("0.120"));

        ingredientRepository.save(feijaoPreto);
        ingredientRepository.save(feijaoCarioca);
        ingredientRepository.save(farinhaMandioca);
        ingredientRepository.save(linguica);
        ingredientRepository.save(bacon);
        ingredientRepository.save(ovos);
        ingredientRepository.save(couve);
        ingredientRepository.save(queijoCanastra);
        ingredientRepository.save(polvilhoAzedo);
        ingredientRepository.save(polvilhoDoce);
        ingredientRepository.save(frangoCaipira);
        ingredientRepository.save(costela);
        ingredientRepository.save(lombo);
        ingredientRepository.save(milhoVerde);
        ingredientRepository.save(canjiquinha);
        ingredientRepository.save(alho);
        ingredientRepository.save(cebola);
        ingredientRepository.save(sangueGalinha);
        ingredientRepository.save(doceLeite);
        ingredientRepository.save(manteigaGarrafa);

        MenuItem feijaoTropeiro = new MenuItem("Feijao Tropeiro", 30);
        feijaoTropeiro.addIngredient(feijaoCarioca, new BigDecimal("300"));
        feijaoTropeiro.addIngredient(bacon, new BigDecimal("100"));
        feijaoTropeiro.addIngredient(linguica, new BigDecimal("100"));
        feijaoTropeiro.addIngredient(farinhaMandioca, new BigDecimal("80"));
        feijaoTropeiro.addIngredient(ovos, new BigDecimal("2"));
        feijaoTropeiro.addIngredient(couve, new BigDecimal("50"));
        feijaoTropeiro.addIngredient(alho, new BigDecimal("10"));

        MenuItem frangoMolhoPardo = new MenuItem("Frango ao Molho Pardo", 45);
        frangoMolhoPardo.addIngredient(frangoCaipira, new BigDecimal("400"));
        frangoMolhoPardo.addIngredient(sangueGalinha, new BigDecimal("100"));
        frangoMolhoPardo.addIngredient(cebola, new BigDecimal("80"));
        frangoMolhoPardo.addIngredient(alho, new BigDecimal("15"));
        frangoMolhoPardo.addIngredient(manteigaGarrafa, new BigDecimal("30"));

        MenuItem tutuFeijao = new MenuItem("Tutu de Feijao", 20);
        tutuFeijao.addIngredient(feijaoPreto, new BigDecimal("250"));
        tutuFeijao.addIngredient(farinhaMandioca, new BigDecimal("60"));
        tutuFeijao.addIngredient(bacon, new BigDecimal("80"));
        tutuFeijao.addIngredient(alho, new BigDecimal("10"));
        tutuFeijao.addIngredient(cebola, new BigDecimal("60"));

        MenuItem canjiquinhaCostelinha = new MenuItem("Canjiquinha com Costelinha", 60);
        canjiquinhaCostelinha.addIngredient(canjiquinha, new BigDecimal("200"));
        canjiquinhaCostelinha.addIngredient(costela, new BigDecimal("350"));
        canjiquinhaCostelinha.addIngredient(alho, new BigDecimal("15"));
        canjiquinhaCostelinha.addIngredient(cebola, new BigDecimal("80"));

        MenuItem lomboSuino = new MenuItem("Lombo Suino Assado", 90);
        lomboSuino.addIngredient(lombo, new BigDecimal("400"));
        lomboSuino.addIngredient(alho, new BigDecimal("20"));
        lomboSuino.addIngredient(manteigaGarrafa, new BigDecimal("40"));

        MenuItem paoDeQueijo = new MenuItem("Pao de Queijo (6 unidades)", 15);
        paoDeQueijo.addIngredient(polvilhoAzedo, new BigDecimal("150"));
        paoDeQueijo.addIngredient(polvilhoDoce, new BigDecimal("100"));
        paoDeQueijo.addIngredient(queijoCanastra, new BigDecimal("100"));
        paoDeQueijo.addIngredient(ovos, new BigDecimal("2"));

        MenuItem curauMilho = new MenuItem("Curau de Milho", 25);
        curauMilho.addIngredient(milhoVerde, new BigDecimal("300"));

        MenuItem romeuJulieta = new MenuItem("Romeu e Julieta", 5);
        romeuJulieta.addIngredient(queijoCanastra, new BigDecimal("80"));
        romeuJulieta.addIngredient(doceLeite, new BigDecimal("60"));

        menuItemRepository.save(feijaoTropeiro);
        menuItemRepository.save(frangoMolhoPardo);
        menuItemRepository.save(tutuFeijao);
        menuItemRepository.save(canjiquinhaCostelinha);
        menuItemRepository.save(lomboSuino);
        menuItemRepository.save(paoDeQueijo);
        menuItemRepository.save(curauMilho);
        menuItemRepository.save(romeuJulieta);

        DiningTable mesa1 = new DiningTable(2);
        DiningTable mesa2 = new DiningTable(4);
        DiningTable mesa3 = new DiningTable(4);
        DiningTable mesa4 = new DiningTable(6);
        DiningTable mesa5 = new DiningTable(8);
        DiningTable mesa6 = new DiningTable(2);

        diningTableRepository.save(mesa1);
        diningTableRepository.save(mesa2);
        diningTableRepository.save(mesa3);
        diningTableRepository.save(mesa4);
        diningTableRepository.save(mesa5);
        diningTableRepository.save(mesa6);

        Chef chefWagner = new Chef("Wagner", "Carvalho", new BigDecimal("55.00"));
        chefWagner.addCookingCourse("Cozinha Mineira Tradicional");
        chefWagner.addCookingCourse("Tecnicas de Defumacao");
        chefWagner.addCookingCourse("Confeitaria Brasileira");
        chefWagner.addMenuItem(feijaoTropeiro);
        chefWagner.addMenuItem(frangoMolhoPardo);
        chefWagner.addMenuItem(tutuFeijao);
        chefWagner.addMenuItem(paoDeQueijo);
        chefWagner.setUsername("chef.wagner");
        chefWagner.setPassword(passwordEncoder.encode("password123"));

        Chef chefMaria = new Chef("Maria", "das Gracas", new BigDecimal("50.00"));
        chefMaria.addCookingCourse("Cozinha Regional Brasileira");
        chefMaria.addCookingCourse("Doces e Sobremesas Mineiras");
        chefMaria.addMenuItem(canjiquinhaCostelinha);
        chefMaria.addMenuItem(lomboSuino);
        chefMaria.addMenuItem(curauMilho);
        chefMaria.addMenuItem(romeuJulieta);
        chefMaria.setUsername("chef.maria");
        chefMaria.setPassword(passwordEncoder.encode("password123"));

        chefRepository.save(chefWagner);
        chefRepository.save(chefMaria);

        Waiter garcomCarlos = new Waiter("Carlos", "Oliveira", new BigDecimal("28.00"));
        garcomCarlos.addDiningTable(mesa1);
        garcomCarlos.addDiningTable(mesa2);
        garcomCarlos.addDiningTable(mesa3);
        garcomCarlos.setUsername("carlos");
        garcomCarlos.setPassword(passwordEncoder.encode("password123"));

        Waiter garcomAna = new Waiter("Ana", "Batista", new BigDecimal("28.00"));
        garcomAna.addDiningTable(mesa4);
        garcomAna.addDiningTable(mesa5);
        garcomAna.addDiningTable(mesa6);
        garcomAna.setUsername("ana");
        garcomAna.setPassword(passwordEncoder.encode("password123"));

        waiterRepository.save(garcomCarlos);
        waiterRepository.save(garcomAna);

        DeliveryDriver entregadorRodrigo = new DeliveryDriver("Rodrigo", "Santos", new BigDecimal("20.00"), "MG/456789/2019");
        Vehicle moto = new Vehicle("MGA1234", VehicleType.MOTORBIKE, entregadorRodrigo);
        entregadorRodrigo.addVehicle(moto);
        entregadorRodrigo.setUsername("rodrigo");
        entregadorRodrigo.setPassword(passwordEncoder.encode("password123"));

        DeliveryDriver entregadorFelipe = new DeliveryDriver("Felipe", "Barbosa", new BigDecimal("20.00"), "MG/987654/2021");
        Vehicle carro = new Vehicle("MGB5678", VehicleType.CAR,     entregadorFelipe);
        Vehicle bicicleta = new Vehicle("MGC9012", VehicleType.BICYCLE, entregadorFelipe);
        entregadorFelipe.addVehicle(carro);
        entregadorFelipe.addVehicle(bicicleta);
        entregadorFelipe.setUsername("felipe");
        entregadorFelipe.setPassword(passwordEncoder.encode("password123"));

        deliveryDriverRepository.save(entregadorRodrigo);
        deliveryDriverRepository.save(entregadorFelipe);

        Manager manager = new Manager("Henrique", "Teixeira", new BigDecimal("80.00"));
        manager.setUsername("manager");
        manager.setPassword(passwordEncoder.encode("password123"));
        managerRepository.save(manager);

        Client clienteMariana = new Client("Mariana", "Souza",  "31987654321", "mariana.souza@gmail.com");
        clienteMariana.setUsername("mariana");
        clienteMariana.setPassword(passwordEncoder.encode("password123"));

        Client clienteWillian = new Client("Willian", "Alves",  "31912345678", "pedro.alves@gmail.com");
        clienteWillian.setUsername("willian");
        clienteWillian.setPassword(passwordEncoder.encode("password123"));

        Client clienteJulia = new Client("Julia", "Costa",  "31955556666", "julia.costa@gmail.com");
        clienteJulia.setUsername("julia");
        clienteJulia.setPassword(passwordEncoder.encode("password123"));

        Client clienteRafael = new Client("Rafael", "Mendes", "31944443333", "rafael.mendes@gmail.com");
        clienteRafael.setUsername("rafael");
        clienteRafael.setPassword(passwordEncoder.encode("password123"));

        clientRepository.save(clienteMariana);
        clientRepository.save(clienteWillian);
        clientRepository.save(clienteJulia);
        clientRepository.save(clienteRafael);

        Reservation reserva1 = new Reservation(LocalDateTime.now().plusDays(1).withHour(19).withMinute(0), 2, clienteMariana, mesa1);
        Reservation reserva2 = new Reservation(LocalDateTime.now().plusDays(1).withHour(20).withMinute(30), 4, clienteWillian, mesa2);
        Reservation reserva3 = new Reservation(LocalDateTime.now().withHour(12).withMinute(0), 6, clienteJulia, mesa4);
        reserva3.setReservationStatus(ReservationStatus.ONGOING);
        Reservation reserva4 = new Reservation(LocalDateTime.now().minusDays(3).withHour(19).withMinute(0), 2, clienteRafael, mesa6);
        reserva4.setReservationStatus(ReservationStatus.COMPLETED);

        clienteMariana.addReservation(reserva1);
        clienteWillian.addReservation(reserva2);
        clienteJulia.addReservation(reserva3);
        clienteRafael.addReservation(reserva4);

        reservationRepository.save(reserva1);
        reservationRepository.save(reserva2);
        reservationRepository.save(reserva3);
        reservationRepository.save(reserva4);

        PickupOrder pedidoRetirada1 = new PickupOrder(clienteMariana, LocalTime.of(12, 30));
        pedidoRetirada1.setWaiter(garcomCarlos);
        pedidoRetirada1.addMenuItemQuantity(paoDeQueijo,2);
        pedidoRetirada1.addMenuItemQuantity(feijaoTropeiro,1);
        pedidoRetirada1.addMenuItemQuantity(romeuJulieta,1);
        clienteMariana.addOrder(pedidoRetirada1);
        orderRepository.save(pedidoRetirada1);

        PickupOrder pedidoRetirada2 = new PickupOrder(clienteWillian, LocalTime.of(13, 0));
        pedidoRetirada2.setWaiter(garcomCarlos);
        pedidoRetirada2.addMenuItemQuantity(canjiquinhaCostelinha,1);
        pedidoRetirada2.addMenuItemQuantity(paoDeQueijo,1);
        pedidoRetirada2.setOrderStatus(OrderStatus.IN_PREPARATION);
        clienteWillian.addOrder(pedidoRetirada2);
        orderRepository.save(pedidoRetirada2);

        PickupOrder pedidoRetirada3 = new PickupOrder(clienteRafael, LocalTime.of(19, 30));
        pedidoRetirada3.setWaiter(garcomAna);
        pedidoRetirada3.addMenuItemQuantity(lomboSuino,1);
        pedidoRetirada3.addMenuItemQuantity(tutuFeijao,1);
        pedidoRetirada3.addMenuItemQuantity(curauMilho,2);
        pedidoRetirada3.setOrderStatus(OrderStatus.COMPLETED);
        clienteRafael.addOrder(pedidoRetirada3);
        orderRepository.save(pedidoRetirada3);

        DeliveryOrder pedidoEntrega1 = new DeliveryOrder(clienteJulia, "Rua dos Inconfidentes", "320", 12);
        pedidoEntrega1.setWaiter(garcomAna);
        pedidoEntrega1.setDeliveryDriver(entregadorRodrigo);
        pedidoEntrega1.addMenuItemQuantity(frangoMolhoPardo,2);
        pedidoEntrega1.addMenuItemQuantity(feijaoTropeiro,1);
        pedidoEntrega1.addMenuItemQuantity(paoDeQueijo,2);
        pedidoEntrega1.setOrderStatus(OrderStatus.IN_PREPARATION);
        clienteJulia.addOrder(pedidoEntrega1);
        orderRepository.save(pedidoEntrega1);

        DeliveryOrder pedidoEntrega2 = new DeliveryOrder(clienteRafael, "Avenida Afonso Pena", "1500", null);
        pedidoEntrega2.setWaiter(garcomCarlos);
        pedidoEntrega2.setDeliveryDriver(entregadorFelipe);
        pedidoEntrega2.addMenuItemQuantity(lomboSuino,1);
        pedidoEntrega2.addMenuItemQuantity(romeuJulieta,2);
        pedidoEntrega2.setOrderStatus(OrderStatus.COMPLETED);
        clienteRafael.addOrder(pedidoEntrega2);
        orderRepository.save(pedidoEntrega2);

        DeliveryOrder pedidoEntrega3 = new DeliveryOrder(clienteMariana, "Rua da Bahia", "45", 3);
        pedidoEntrega3.setWaiter(garcomAna);
        pedidoEntrega3.setDeliveryDriver(entregadorRodrigo);
        pedidoEntrega3.addMenuItemQuantity(canjiquinhaCostelinha,1);
        pedidoEntrega3.addMenuItemQuantity(curauMilho,1);
        clienteMariana.addOrder(pedidoEntrega3);
        orderRepository.save(pedidoEntrega3);

        System.out.println("Clients: " + clientRepository.count());
        System.out.println("Menu items: " + menuItemRepository.count());
        System.out.println("Ingredients: " + ingredientRepository.count());
        System.out.println("Orders: " + orderRepository.count());
        System.out.println("Reservations: " + reservationRepository.count());
        System.out.println("Dining tables: " + diningTableRepository.count());
        System.out.println("Chefs: " + chefRepository.count());
        System.out.println("Waiters: " + waiterRepository.count());
        System.out.println("Delivery drivers: " + deliveryDriverRepository.count());
        System.out.println("Manager: " + managerRepository.count());
        System.out.println("---");
        System.out.println("Demo accounts (all passwords: password123)");
        System.out.println("Clients: mariana / willian / julia / rafael");
        System.out.println("Chefs: chef.wagner / chef.maria");
        System.out.println("Waiters: carlos / ana");
        System.out.println("Drivers: rodrigo / felipe");
    }

    private BigDecimal computePrice(MenuItem item) {
        return item.getIngredientQuantities().stream()
                .map(iq -> iq.getIngredient().getBasePrice().multiply(iq.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}