package eu.mrndesign.matned.metalserwisproductionrest.controller;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
import eu.mrndesign.matned.metalserwisproductionrest.service.ClientService;
import eu.mrndesign.matned.metalserwisproductionrest.service.DeliveryService;
import eu.mrndesign.matned.metalserwisproductionrest.service.OrderService;
import eu.mrndesign.matned.metalserwisproductionrest.service.ProcessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final DeliveryService deliveryService;
    private final ProcessService processService;


    public OrderController(OrderService orderService,
                           ClientService clientService,
                           DeliveryService deliveryService,
                           ProcessService processService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.deliveryService = deliveryService;
        this.processService = processService;
    }

    @GetMapping("/orders")
    public List<OrderDTO> showAllOrders(@RequestParam(defaultValue = "${default.sort.by}", name = "sort") String[] sort,
                                        @RequestParam(defaultValue = "${default.page.start}", name = "page") Integer page,
                                        @RequestParam(defaultValue = "${default.page.size}", name = "amount") Integer amount,
                                        @RequestParam(defaultValue = "all", name = "search") String search,
                                        @RequestParam(defaultValue = "", name = "element") String element){
        switch (search){
//            TODO
            case "DEADLINE_DATE" : orderService.findByDeadlineDate(element, page, amount, sort);
            case "NOT_DONE": orderService.findByNotDone(page, amount, sort);
            case "DONE": orderService.findByDone(page, amount, sort);
            case "DELIVERY": orderService.findByDeliveryCode(element, page, amount, sort);
            case "CLIENT": orderService.findByClientName(element, page, amount, sort);
            default: return orderService.findAll(page, amount, sort);
        }

    }

}
