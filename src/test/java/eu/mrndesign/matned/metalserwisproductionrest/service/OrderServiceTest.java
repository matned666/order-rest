package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Order;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ClientRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.DeliveryRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.OrderRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private ProcessRepository processRepository;
    @MockBean
    private ClientRepository clientRepository;

    private List<ClientEntity> clients;
    private List<Delivery> deliveries;
    private List<Process> processes;
    private List<Order> orders;
    private String[] sortBy;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        clients = new LinkedList<>();
        deliveries = new LinkedList<>();
        processes = new LinkedList<>();
        orders = new LinkedList<>();
        sortBy = new String[1];
        sortBy[0] = "something";
        pageable = orderService.getPageable(1, 10, sortBy);
        for (int i = 1; i <= 3; i++) {
            clients.add(new ClientEntity("Client"+i, "Client"+i+" description"));
            deliveries.add(new Delivery("Delivery"+i, "Delivery"+i+" description"));
            processes.add(new Process("Process"+i, "Process"+i+" description"));
            orders.add(new Order.OrderBuilder("Order"+i, i*1000).build());
        }
    }

    @Test
    void saveOrder() {
        doReturn(orders.get(0)).when(orderRepository).save(any());
        doReturn(Optional.of(clients.get(0))).when(clientRepository).findById(any());
        doReturn(Optional.of(deliveries.get(0))).when(deliveryRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        assertEquals(OrderDTO.apply(orders.get(0)).getProduct(), orderService.saveOrder(OrderDTO.apply(orders.get(0)),new LinkedList<>(),1L, 1L ).getProduct());
    }

    @Test
    void changeOrderActiveStatus() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());
        assertFalse(orders.get(0).isActive());
        OrderDTO notImportant = orderService.changeOrderActiveStatus(1L);
        assertTrue(orders.get(0).isActive());
    }

    @Test
    void findById() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());

        assertEquals(OrderDTO.apply(orders.get(0)).getProduct(), orderService.findById(1L ).getProduct());
    }

    @Test
    void findByIdThrowsRuntimeXWhenNoOrderFoundById() {
        doReturn(Optional.empty()).when(orderRepository).findById(any());

        assertThrows(RuntimeException.class, ()-> orderService.findById(1L));
    }

    @Test
    void edit() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        OrderDTO updateData = new OrderDTO.OrderDTOBuilder("testing-change", 0).build();
        OrderDTO dto = orderService.edit(0L, updateData);

        assertEquals(OrderDTO.apply(orders.get(0)).getName(), "testing-change");
        assertEquals(OrderDTO.apply(orders.get(0)).getDesiredQuantity(), 0);

        updateData = new OrderDTO.OrderDTOBuilder("", 1).build();
        dto = orderService.edit(0L, updateData);

        assertEquals(OrderDTO.apply(orders.get(0)).getName(), "testing-change");
        assertEquals(OrderDTO.apply(orders.get(0)).getDesiredQuantity(), 1);

    }

    @Test
    void changeClient() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(Optional.of(clients.get(0))).when(clientRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        assertNull(orders.get(0).getClient());

        OrderDTO dto = orderService.changeClient(0L, 0L);

        assertEquals(orders.get(0).getClient(), clients.get(0));
    }

     @Test
    void changeClientLeavesPreviousWhenNoClientFoundInDB() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(Optional.empty()).when(clientRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        OrderDTO dto = orderService.changeClient(0L, 0L);

         assertNull(orders.get(0).getClient());

    }

    @Test
    void changeDelivery() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(Optional.of(deliveries.get(0))).when(deliveryRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        assertNull(orders.get(0).getDelivery());

        OrderDTO dto = orderService.changeDelivery(0L, 0L);

        assertEquals(orders.get(0).getDelivery(), deliveries.get(0));
    }

    @Test
    void changeDeliveryDontChange() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(Optional.empty()).when(deliveryRepository).findById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        OrderDTO dto = orderService.changeDelivery(0L, 0L);
        assertNull(orders.get(0).getDelivery());
    }

    @Test
    void changeProcesses() {
        doReturn(Optional.of(orders.get(0))).when(orderRepository).findById(any());
        doReturn(processes).when(processRepository).findAllById(any());
        doReturn(orders.get(0)).when(orderRepository).save(any());

        OrderDTO dto = orderService.changeProcesses(0L, new LinkedList<>());
        assertEquals(orders.get(0).getProcesses(), processes);
    }

    @Test
    void findAll() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findAll(any(Pageable.class));
        assertEquals(3 , orderService.findAll(1,1,sortBy).size());
    }

    @Test
    void findByClientName() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByClientName(any(),any(Pageable.class));
        assertEquals(3 , orderService.findByClientName("anything",1,1,sortBy).size());
    }

    @Test
    void findByDeliveryCode() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByDeliveryCode(any(),any(Pageable.class));
        assertEquals(3 , orderService.findByDeliveryCode("anything",1,1,sortBy).size());
    }

    @Test
    void findByDone() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByDone(any(Pageable.class));
        assertEquals(3 , orderService.findByDone(1,1,sortBy).size());
    }

    @Test
    void findByNotDone() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByNotDone(any(Pageable.class));
        assertEquals(3 , orderService.findByNotDone(1,1,sortBy).size());
    }

    @Test
    void findByDeadlineDate() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByDeadlineDate(any(),any(Pageable.class));
        assertEquals(3 , orderService.findByDeadlineDate("2000-01-01",1,1,sortBy).size());
    }

    @Test
    void findByDeadlineDateWithAnotherDateFormat() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByDeadlineDate(any(),any(Pageable.class));
        assertEquals(3 , orderService.findByDeadlineDate("01-01-2000",1,1,sortBy).size());
    }

    @Test
    void findByDeadlineDateWithAWrongDateFormat() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByDeadlineDate(any(),any(Pageable.class));
        doReturn(new PageImpl<>(orders.subList(0,2),pageable, 2)).when(orderRepository).findAll(any(Pageable.class));
        assertEquals(2 , orderService.findByDeadlineDate("010100dfgdfgsfg",1,1,sortBy).size());
    }

    @Test
    void findByDeadlineDateBetweenDates() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findByDeadlineDateBetweenDates(any(), any(),any(Pageable.class));
        assertEquals(3 , orderService.findByDeadlineDateBetweenDates("2000-01-01","2000-01-01",1,1,sortBy).size());
    }

    @Test
    void findByOrderDateBetweenDates() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findByOrderDateBetweenDates(any(), any(),any(Pageable.class));
        assertEquals(3 , orderService.findByOrderDateBetweenDates("2000-01-01","2000-01-01",1,1,sortBy).size());

    }

    @Test
    void findOrdersByOverDeadlineDate() {
        doReturn(new PageImpl<>(orders.subList(0,3),pageable, 3)).when(orderRepository).findOrdersByOverDeadlineDate(any(Pageable.class));
        assertEquals(3 , orderService.findOrdersByOverDeadlineDate(1,1,sortBy).size());
    }

}
