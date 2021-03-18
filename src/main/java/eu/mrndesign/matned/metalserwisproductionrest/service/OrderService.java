package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.repository.DeliveryRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.OrderRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ProcessRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends BaseService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProcessRepository processRepository;

    public OrderService(OrderRepository orderRepository,
                        DeliveryRepository deliveryRepository,
                        ProcessRepository processRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.processRepository = processRepository;
    }

}
