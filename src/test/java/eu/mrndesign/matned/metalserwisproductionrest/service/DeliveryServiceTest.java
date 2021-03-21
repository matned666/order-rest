package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.DeliveryDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import eu.mrndesign.matned.metalserwisproductionrest.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith({SpringExtension.class})
@SpringBootTest
class DeliveryServiceTest {

    @Autowired
    private DeliveryService deliveryService;

    @MockBean
    private DeliveryRepository deliveryRepository;

    private List<Delivery> deliveries;
    private String[] sortBy;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        deliveries = new LinkedList<>();
        sortBy = new String[1];
        sortBy[0] = "something";
        pageable = deliveryService.getPageable(1, 10, sortBy);
        for (int i = 1; i <= 3; i++) {
            deliveries.add(new Delivery("D"+i, Delivery.DeliveryType.SHIPPING));
        }
    }

    @Test
    void saveDelivery() {
        doReturn(deliveries.get(0)).when(deliveryRepository).save(any());
        assertEquals("D1", deliveryService.saveDelivery(DeliveryDTO.apply(deliveries.get(0))).getDeliveryCode());
    }

    @Test
    void setShippedStatus() {
    }

    @Test
    void findAllDeliveries() {
    }

    @Test
    void findDeliveryById() {
    }

    @Test
    void editDelivery() {
    }
}
