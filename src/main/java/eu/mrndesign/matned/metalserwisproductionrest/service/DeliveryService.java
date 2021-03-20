package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.DeliveryDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Delivery;
import eu.mrndesign.matned.metalserwisproductionrest.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.NO_SUCH_DELIVERY;

@Service
public class DeliveryService extends BaseService{

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryDTO saveDelivery(DeliveryDTO dto) {
        return DeliveryDTO.apply(
                deliveryRepository
                        .save(
                                new Delivery(
                                        dto.getDeliveryCode(),
                                        Delivery.DeliveryType.valueOf(dto.getDeliveryType()),
                                        dto.getPickUpTime(),
                                        dto.getDeliveryTime(),
                                        dto.getDescription())
                        ));
    }

    public DeliveryDTO setShippedStatus(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(()->new RuntimeException(NO_SUCH_DELIVERY));
        delivery.setShipped();
        return DeliveryDTO.apply(deliveryRepository.save(delivery));
    }

    public List<DeliveryDTO> findAllDeliveries(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return deliveryRepository.findAll(getPageable(startPage, itemsPerPage, sortBy)).stream()
                .map(DeliveryDTO::apply)
                .collect(Collectors.toList());
    }

    public DeliveryDTO findDeliveryById(Long id) {
        return DeliveryDTO.apply(deliveryRepository.findById(id).orElseThrow(()->new RuntimeException(NO_SUCH_DELIVERY)));
    }

    public DeliveryDTO editDelivery(Long id, DeliveryDTO editedData) {
        Delivery toEdit = deliveryRepository.findById(id).orElseThrow(()->new RuntimeException(NO_SUCH_DELIVERY));
        toEdit.applyNew(editedData);
        return DeliveryDTO.apply(deliveryRepository.save(toEdit));
    }

}
