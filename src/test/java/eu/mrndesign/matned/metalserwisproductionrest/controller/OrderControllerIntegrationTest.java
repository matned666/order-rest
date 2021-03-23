package eu.mrndesign.matned.metalserwisproductionrest.controller;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Order;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.LinkedList;
import java.util.List;

import static eu.mrndesign.matned.metalserwisproductionrest.JsonOps.asJsonString;
import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private OrderDTO orderDTO;
    private List<Order> ordersTested;
    private String orderName;
    private int quantity;

    @BeforeEach
    @WithMockUser(username = "test@test.tst", roles = "ADMIN")
    void setup() throws Exception {
        orderName = "testProduct12367123542139486jhgsdfjhgkajghkj1237agfyvludv";
        quantity = 21111;

        orderDTO = new OrderDTO.OrderDTOBuilder(orderName, quantity).build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .content(asJsonString(orderDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        ordersTested = orderRepository.findByNameAndQuantity(orderName, quantity);

    }

    @AfterEach
    @WithMockUser(username = "test@test.tst", roles = "ADMIN")
    void reset() {

        ordersTested.forEach(x -> {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.delete("/orders/" + x.getId()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @DisplayName("GET /orders test - orders found 200")
    @WithMockUser(username = "test@test.tst", roles = "ADMIN")
    void getAllOrdersList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        List<Order> orders = orderRepository.findAll();
        System.out.println(" >-----> ALL ORDERS: ");
        orders.forEach(x -> System.out.println(" >-----> ORDER: " + x.toString()));
    }

    @Test
    @DisplayName("Set order to active")
    @WithMockUser(roles = "ADMIN")
    void setOrderActive() {
        ordersTested.forEach(x -> {
            try {
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/orders/" +x.getId()+"/activate")
                                .accept("application/json"))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(content().string(org.hamcrest.Matchers.containsString("\"active\":true")))
                        .andExpect(status().isOk())
                        .andReturn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
