package eu.mrndesign.matned.metalserwisproductionrest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.LinkedList;
import java.util.List;

import static eu.mrndesign.matned.metalserwisproductionrest.JsonOps.asJsonString;
import static org.mockito.ArgumentMatchers.*;
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

    private List<OrderDTO> orders;
    private String ordersJson;
    ObjectWriter ow;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        orders = new LinkedList<>();
        for (int i = 1; i <= 3; i++) {
            orders.add(new OrderDTO.OrderDTOBuilder("test"+i, i*100)
                    .build());
        }
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
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


    @Test
    @DisplayName("GET /orders/1 test - order found with status 200")
    @WithMockUser(roles = "USER")
    void getSingleOrder() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).findById(any());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/1")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(ow.writeValueAsString(orders.get(0))))
                .andReturn();
    }

    @Test
    @DisplayName("GET /orders/1 test - order NOT found with status 403")
    @WithMockUser(roles = "BANNED")
    void getSingleOrderForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("GET /orders/1 test - order NOT found with status 401")
    void getSingleOrderUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders test - order saved with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void saveNewOrder() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).saveOrder(any(),any(),any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders test - order NOT saved with status 403")
    @WithMockUser(roles = "USER")
    void saveNewOrderForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders test - order NOT saved with status 401")
    void saveNewOrderUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1 test - order edited with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void editOrder() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).edit(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();    }

    @Test
    @DisplayName("POST /orders/1/activate test - order activation with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void setActivationOrder() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).setActive(any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1/activate"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1 test - order NOT edited with status 403")
    @WithMockUser(roles = "USER")
    void editOrderForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1 test - order NOT edited with status 401")
    void editOrderUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1/client/1 test - order client edited with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void editOrderClient() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).changeClient(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1/client/1")
                        .content(asJsonString(orders.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1/processes test - order client edited with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void editOrderProcesses() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).changeProcesses(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1/processes")
                        .content(asJsonString(new LinkedList<>()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST /orders/1/delivery/1 test - order delivery edited with status 200")
    @WithMockUser(roles = "PUBLISHER")
    void editOrderDelivery() throws Exception {
        Mockito.doReturn(orders.get(0)).when(orderService).changeDelivery(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/orders/1/delivery/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(asJsonString(orders.get(0))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("DELETE /orders/1 test - order deleted with status 200")
    @WithMockUser(roles = "MANAGER")
    void deleteOrder() throws Exception {
        Mockito.doReturn(orders).when(orderService).deleteOrder(any(), any(), any(), any());
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/1")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(ordersJson))
                .andReturn();
    }

    @Test
    @DisplayName("DELETE /orders/1 test - order NOT deleted with status 403")
    @WithMockUser(roles = {"PUBLISHER", "USER"})
    void deleteOrderStatusForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("DELETE /orders/1 test - order NOT deleted with status 401")
    void deleteOrderStatusUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/orders/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }


}
