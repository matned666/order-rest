package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ClientDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.order.DeliveryDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ProcessDTO;
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

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.NO_SUCH_ORDER;

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

    public OrderDTO saveOrder(OrderDTO dto, List<String> processes, String clientName, String deliveryCode){
       List<Process> processList = new LinkedList<>();
       processes.forEach(x-> processList.add(processRepository.findByProcessName(x).orElse(processRepository.save(new Process()))));
        ClientEntity client = clientRepository.findByClientName(clientName).orElse(clientRepository.save(new ClientEntity()));
        Delivery deliveryEntity = deliveryRepository.findByDeliveryCode(deliveryCode).orElse(deliveryRepository.save(new Delivery()));
        return OrderDTO.apply(orderRepository.save(Order.apply(dto, processList, client, deliveryEntity)));
    }

    public OrderDTO changeOrderActiveStatus(Long orderId){
        Order toEdit = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException(NO_SUCH_ORDER));
        toEdit.setActive();
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

 public OrderDTO findById(Long id){
        if (id != null) {
            return OrderDTO.apply(orderRepository.findById(id).orElseThrow(() -> new RuntimeException(NO_SUCH_ORDER)));
        } else {
            throw new RuntimeException(NO_SUCH_ORDER);
        }
    }

    public OrderDTO edit(Long id, OrderDTO editedData){
        Order toEdit = orderRepository.findById(id).orElseThrow(()-> new RuntimeException(NO_SUCH_ORDER));
        toEdit.applyNew(editedData);
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeClient(Long orderId, Long clientId){
        Order toEdit = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException(NO_SUCH_ORDER));
        toEdit.setClient(clientRepository.findById(clientId).orElse(toEdit.getClient()));
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeDelivery(Long orderId, Long deliveryId){
        Order toEdit = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException(NO_SUCH_ORDER));
        toEdit.setDelivery(deliveryRepository.findById(deliveryId).orElse(toEdit.getDelivery()));
        return OrderDTO.apply(orderRepository.save(toEdit));
    }

    public OrderDTO changeProcesses(Long orderId, List<Long> processesIds){
        Order toEdit = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException(NO_SUCH_ORDER));
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

    public List<OrderDTO> findByDeadlineDate(Date deadlineDate, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByDeadlineDate(deadlineDate, getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByDeadlineDateBetweenDates(Date deadlineStartDate, Date deadlineEndDate, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findByDeadlineDateBetweenDates(deadlineStartDate, deadlineEndDate, getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findByOrderDateBetweenDates(Date deadlineStartDate, Date deadlineEndDate, Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findByOrderDateBetweenDates(deadlineStartDate, deadlineEndDate, getPageable(startPage, itemsPerPage, sortBy)));
    }

    public List<OrderDTO> findOrdersByOverDeadlineDate(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return findList(orderRepository.findOrdersByOverDeadlineDate(getPageable(startPage, itemsPerPage, sortBy)));
    }

//  PROCESS  TODO

//    public ProcessDTO saveProcess(ProcessDTO dto){
//
//    }
//
//    public List<ProcessDTO> findAllProcesses(){
//
//
//    }
//
//    public ProcessDTO findProcessById(Long id){
//
//    }
//
//    public ProcessDTO editProcess(Long id, ProcessDTO editedData){
//
//    }
//
////    Delivery TODO
//
//    public DeliveryDTO saveDelivery(ProcessDTO dto){
//
//    }
//
//    public List<DeliveryDTO> findAllDeliveries(){
//
//    }
//
//    public DeliveryDTO findDeliveryById(Long id){
//
//    }
//
//    public DeliveryDTO editDelivery(Long id, DeliveryDTO editedData){
//
//    }
//
//
//
////    Client TODO
//
//    public ClientDTO saveClient(ClientDTO dto){
//
//    }
//
//    public List<ClientDTO> findAllClients(){
//
//
//    }
//
//    public ClientDTO findClientById(Long id){
//
//    }
//
//    public ClientDTO editClient(Long id, ClientDTO editedData){
//
//    }



//    Private

    public List<OrderDTO> findList(Page<Order> orders) {
        List<Order> _orders = orders.getContent();
        return convertEntityToDTOList(_orders);
    }

    private List<OrderDTO> convertEntityToDTOList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::apply)
                .collect(Collectors.toList());
    }
}
