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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
       processes.forEach(x->{
           processList.add(processRepository.findByProcessName(x).orElse(processRepository.save(new Process())));
       });
        ClientEntity client = clientRepository.findByClientName(clientName).orElse(clientRepository.save(new ClientEntity()));
        Delivery deliveryEntity = deliveryRepository.findByDeliveryCode(deliveryCode).orElse(deliveryRepository.save(new Delivery()));
        return OrderDTO.apply(orderRepository.save(Order.apply(dto, processList, client, deliveryEntity)));
    }

    public List<OrderDTO> findAll(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        Pageable pageable = getPageable(startPage, itemsPerPage, sortBy);
        Page<Order> orders = orderRepository.findAll(pageable);
        List<Order> _orders = orders.getContent();
        return convertEntityToDTOList(_orders);
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


//    Private


    private List<OrderDTO> convertEntityToDTOList(List<Order> orders) {
        return orders.stream()
                .map(OrderDTO::apply)
                .collect(Collectors.toList());
    }
}
