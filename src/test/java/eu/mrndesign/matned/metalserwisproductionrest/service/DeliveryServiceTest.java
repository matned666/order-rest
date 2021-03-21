package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.DeliveryDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import eu.mrndesign.matned.metalserwisproductionrest.repository.DeliveryRepository;
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
            deliveries.add(new Delivery("D"+i, "D"+i));
        }
    }

    @Test
    void saveDelivery() {
        doReturn(deliveries.get(0)).when(deliveryRepository).save(any());
        assertEquals("D1", deliveryService.saveDelivery(DeliveryDTO.apply(deliveries.get(0))).getDeliveryCode());
    }

    @Test
    void setShippedStatus() {
        doReturn(Optional.of(deliveries.get(0))).when(deliveryRepository).findById(any());
        doReturn(deliveries.get(0)).when(deliveryRepository).save(any());

        assertFalse(deliveries.get(0).isShipped());

        DeliveryDTO dto = deliveryService.setShippedStatus(1L);

        assertTrue(deliveries.get(0).isShipped());

    }

    @Test
    void findAllDeliveries() {
        doReturn(new PageImpl<>(deliveries.subList(0,3),pageable, 3)).when(deliveryRepository).findAll(any(Pageable.class));
        assertEquals(3 , deliveryService.findAllDeliveries(1,1,sortBy).size());

    }

    @Test
    void findDeliveryById() {
        doReturn(Optional.of(deliveries.get(0))).when(deliveryRepository).findById(any());
        assertEquals(deliveryService.findDeliveryById(1L), DeliveryDTO.apply(deliveries.get(0)));
    }

    @Test
    void editDelivery() {
        doReturn(Optional.of(deliveries.get(0))).when(deliveryRepository).findById(any());
        doReturn(deliveries.get(0)).when(deliveryRepository).save(any());

        DeliveryDTO deliveryDTO = new DeliveryDTO("sss", null);
        DeliveryDTO notImportant = deliveryService.editDelivery(1L, deliveryDTO);

        assertEquals("sss", deliveries.get(0).getDeliveryCode());
        assertEquals("D1", deliveries.get(0).getDescription());

    }

    @Test
    void throwsRuntimeExWhenEmptyDTOGivenToEdit() {
        doReturn(Optional.empty()).when(deliveryRepository).findById(any());
        assertThrows(RuntimeException.class, ()-> deliveryService.editDelivery(1L, DeliveryDTO.apply(deliveries.get(0))));
    }
}
