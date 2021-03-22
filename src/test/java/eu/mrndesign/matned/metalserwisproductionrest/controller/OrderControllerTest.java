package eu.mrndesign.matned.metalserwisproductionrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
import eu.mrndesign.matned.metalserwisproductionrest.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private ClientService clientService;
    @MockBean
    private ProcessService processService;
    @MockBean
    private DeliveryService deliveryService;

    private List<OrderDTO> orders;
    private String ordersJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        orders = new LinkedList<>();
        for (int i = 1; i <= 3; i++) {
            orders.add(new OrderDTO.OrderDTOBuilder("test"+i, i*100)
                    .build());
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ordersJson = ow.writeValueAsString(orders);
    }

    @Test
    @DisplayName("GET /orders test - all orders found with status 200")
    @WithMockUser(roles = "USER")
    void getAllOrdersList() throws Exception {
        Mockito.doReturn(orders).when(orderService).findAll(any(), any(), any());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(ordersJson))
                .andReturn();
    }


    @Test
    @DisplayName("GET /orders test - all orders by client found with status 200")
    @WithMockUser(roles = "USER")
    void getAllOrdersListAccordingToClient() throws Exception {
        Mockito.doReturn(orders).when(orderService).findByClientName(anyString(), any(), any(), any());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders?search=client")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(ordersJson))
                .andReturn();
    }


    @Test
    @DisplayName("GET /orders test - all orders NOT found with status 403")
    @WithMockUser(roles = {"BANNED"})
    void getAllOrdersListForbiddenForBannedUser() throws Exception {
        Mockito.doReturn(orders).when(orderService).findAll(any(), any(), any());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("GET /orders test - all orders NOT found with status 401")
    void getAllOrdersListForbiddenForUnauthorized() throws Exception {
        Mockito.doReturn(orders).when(orderService).findAll(any(), any(), any());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }



}
