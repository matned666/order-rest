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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static eu.mrndesign.matned.metalserwisproductionrest.Patterns.getDateFromString;
import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.*;

@Service
public class OrderService extends BaseService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProcessRepository processRepository;
    private final ClientRepository clientRepository;

    public OrderService(OrderRepository orderRepository,
                        DeliveryRepository deliveryRepository,
                        ProcessRepository processRepository,
                        ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.processRepository = processRepository;
        this.clientRepository = clientRepository;
    }

    public OrderDTO saveOrder(OrderDTO dto, List<Long> processes, Long clientId, Long deliveryId) {
        List<Process> processList = new LinkedList<>();
        processes.forEach(x -> processList.add(processRepository.findById(x).orElseThrow(() -> new RuntimeException(NO_SUCH_PROCESS))));
        ClientEntity client = clientRepository.findById(clientId).orElse(null);
        Delivery deliveryEntity = deliveryRepository.findById(deliveryId).orElse(null);
        return OrderDTO.apply(orderRepository.save(Order.apply(dto, processList, client, deliveryEntity)));
    }

    public OrderDTO changeOrderActiveStatus(Long orderId) {
        Order toEdit = getClientById(orderId);
        toEdit.setActive();
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO findById(Long id) {
        if (id != null) {
            return OrderDTO.apply(getClientById(id));
        } else {
            throw new RuntimeException(NO_SUCH_ORDER);
        }
    }

    public OrderDTO edit(Long id, OrderDTO editedData) {
        Order toEdit = getClientById(id);
        toEdit.applyNew(editedData);
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeClient(Long orderId, Long clientId) {
        Order toEdit = getClientById(orderId);
        toEdit.setClient(clientRepository.findById(clientId).orElse(toEdit.getClient()));
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeDelivery(Long orderId, Long deliveryId) {
        Order toEdit = getClientById(orderId);
        toEdit.setDelivery(deliveryRepository.findById(deliveryId).orElse(toEdit.getDelivery()));
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeProcesses(Long orderId, List<Long> processesIds) {
        Order toEdit = getClientById(orderId);
        toEdit.applyNewProcessList(processRepository.findAllById(processesIds));
        return OrderDTO.apply(orderRepository.save(toEdit));
    }


    public List<OrderDTO> findAll(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findAll(getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByClientName(String clientName, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByClientName(clientName, getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByDeliveryCode(String deliveryCode, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByDeliveryCode(deliveryCode, getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByDone(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByDone(getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByNotDone(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByNotDone(getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByDeadlineDate(String deadlineD, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        Date deadlineDate = getDateFromString(deadlineD);
        if (deadlineDate != null)
            return findList(orderRepository.findOrdersByDeadlineDate(deadlineDate, getPageable(startPage, itemsPerPage, sortBy)));
        else return findAll(startPage, itemsPerPage, sortBy);
    }

    public List<OrderDTO> findByDeadlineDateBetweenDates(String deadlineStartD, String deadlineEndD, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        Date deadlineStartDate = getDateFromString(deadlineStartD);
        Date deadlineEndDate = getDateFromString(deadlineEndD);
        if (deadlineStartDate != null && deadlineEndDate != null)
            return findList(orderRepository.findByDeadlineDateBetweenDates(deadlineStartDate, deadlineEndDate, getPageable(startPage, itemsPerPage, sortBy)));
        else if (deadlineStartDate != null)
            return findList(orderRepository.findByDeadlineDateBetweenDates(deadlineStartDate, new Date(Long.MAX_VALUE), getPageable(startPage, itemsPerPage, sortBy)));
        else if (deadlineEndDate != null)
            return findList(orderRepository.findByDeadlineDateBetweenDates(new Date(0L), deadlineEndDate, getPageable(startPage, itemsPerPage, sortBy)));
        else return findAll(startPage, itemsPerPage, sortBy);
    }

    public List<OrderDTO> findByOrderDateBetweenDates(String orderStartD, String orderEndD, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        Date orderStartDate = getDateFromString(orderStartD);
        Date orderEndDate = getDateFromString(orderEndD);
        if (orderStartDate != null && orderEndDate != null)
            return findList(orderRepository.findByOrderDateBetweenDates(orderStartDate, orderEndDate, getPageable(startPage, itemsPerPage, sortBy)));
        else if (orderStartDate != null)
            return findList(orderRepository.findByOrderDateBetweenDates(orderStartDate, new Date(Long.MAX_VALUE), getPageable(startPage, itemsPerPage, sortBy)));
        else if (orderEndDate != null)
            return findList(orderRepository.findByOrderDateBetweenDates(new Date(0L), orderEndDate, getPageable(startPage, itemsPerPage, sortBy)));
        else return findAll(startPage, itemsPerPage, sortBy);
    }

    public List<OrderDTO> findOrdersByOverDeadlineDate(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByOverDeadlineDate(getPageable(startPage, itemsPerPage, sortBy)));
    }


    //    Private


    private Order getClientById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException(NO_SUCH_ORDER));
    }

    private List<OrderDTO> findList(Page<Order> orders) {
        List<Order> _orders = orders.getContent();
        return convertEntityToDTOList(_orders);
    }

    private List<OrderDTO> convertEntityToDTOList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::apply)
                .collect(Collectors.toList());
    }
}
