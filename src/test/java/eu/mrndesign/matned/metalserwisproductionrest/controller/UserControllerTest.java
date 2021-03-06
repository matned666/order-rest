package eu.mrndesign.matned.metalserwisproductionrest.controller;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import eu.mrndesign.matned.metalserwisproductionrest.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<UserDTO> allUsers;

    @BeforeEach
    void setup(){
        allUsers = new LinkedList<>();
        UserDTO user;
        for (int i = 1; i <= 3; i++) {
            user = new UserDTO("test@test"+i+".tst");
            allUsers.add(user);
            System.out.println(user);
        }
    }

    @Test
    @DisplayName("GET /users test - users found 200")
    @WithMockUser(roles = "MANAGER")
    void getAllUsersList() throws Exception {

        Mockito.doReturn(allUsers).when(userService).findAll();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{'login':'test@test1.tst'},{'login':'test@test2.tst'},{'login':'test@test3.tst'}]"))
                .andReturn();

    }

    @Test
    @DisplayName("GET /users/0 test - user with id found 200")
    @WithMockUser(roles = {"PUBLISHER", "USER", "MANAGER"})
    void getSingleUser() throws Exception {

        Mockito.doReturn(allUsers.get(0)).when(userService).findUserById(any());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andReturn();

    }


    @Test
    @DisplayName("DELETE /users/0 test - user with id deleted status 200")
    @WithMockUser(roles = {"MANAGER", "PUBLISHER", "USER"})
    void deleteSingleUser() throws Exception {
        Mockito.doReturn(allUsers).when(userService).deleteUser(any());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("[{'login':'test@test1.tst'},{'login':'test@test2.tst'},{'login':'test@test3.tst'}]"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("DELETE /users/0 test - user with id deleted status 403")
    @WithMockUser(roles = {"PUBLISHER", "USER"})
    void deleteSingleUserStatusForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users test - user status 200")
    @WithMockUser(roles = {"PUBLISHER", "USER"})
    void addUserStatusOk() throws Exception {
        Mockito.doReturn(allUsers.get(0)).when(userService).add(any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .content(asJsonString(allUsers.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users test - user status 403")
    @WithMockUser(roles = "USER")
    void addUserStatusForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .content(asJsonString(allUsers.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0 test - user status 200")
    @WithMockUser(roles = {"PUBLISHER", "USER"})
    void updateUserLoginStatusOk() throws Exception {
        Mockito.doReturn(allUsers.get(0)).when(userService).updateLogin(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/0")
                        .content(asJsonString(allUsers.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    @DisplayName("POST /users/0 test - user status 403")
    @WithMockUser(roles = "USER")
    void updateUserLoginStatusForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/0")
                        .content(asJsonString(allUsers.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0/password test - user status 200")
    @WithMockUser(roles = {"MANAGER","PUBLISHER", "USER"})
    void updateUserPasswordStatusOk() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("test@test1.tst","test123");
        registrationDTO.setPasswordConfirm("test123");
        Mockito.doReturn(allUsers.get(0)).when(userService).updatePassword(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/0/password")
                        .content(asJsonString(registrationDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andReturn();
    }


    @Test
    @DisplayName("POST /users/0/password test - user status 403")
    @WithMockUser(roles = {"PUBLISHER", "USER"})
    void updateUserPasswordStatusForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/0/password")
                        .content(asJsonString(allUsers.get(0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0/add-role test - user status 403")
    @WithMockUser(roles = {"PUBLISHER", "MANAGER", "USER"})
    void addUserRoleForbiddenForManager() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0/add-role/"+UserRole.Role.ADMIN.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0/add-role test - user status 200")
    @WithMockUser(roles = {"PUBLISHER", "CEO", "MANAGER", "USER"})
    void addUserRoleStatusOk() throws Exception {
        Mockito.doReturn(allUsers.get(0)).when(userService).assignNewRole(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0/add-role/"+UserRole.Role.ADMIN.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0/take-away-role test - user status 403")
    @WithMockUser(roles = {"PUBLISHER", "MANAGER", "USER"})
    void takeAwayUserRoleForbiddenForManager() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0/take-away-role/"+UserRole.Role.BANNED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("POST /users/0/take-away-role test - user status 200")
    @WithMockUser(roles = {"PUBLISHER", "CEO", "MANAGER", "USER"})
    void takeAwayUserRoleStatusOk() throws Exception {
        Mockito.doReturn(allUsers.get(0)).when(userService).takeAwayRole(any(), any());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0/take-away-role/"+UserRole.Role.BANNED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andReturn();
    }



}
