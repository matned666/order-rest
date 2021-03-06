package eu.mrndesign.matned.metalserwisproductionrest.controller;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRoleRepository;
import eu.mrndesign.matned.metalserwisproductionrest.service.UserService;
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

import java.util.List;

import static eu.mrndesign.matned.metalserwisproductionrest.JsonOps.asJsonString;
import static eu.mrndesign.matned.metalserwisproductionrest.service.UserService.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserService userService;


    private UserDTO defaultUser;

    String login = "test@test1.tst";
    String password = "test1";


    @BeforeEach
    void setup() throws Exception {
        defaultUser = new UserDTO("test@test1.tst");
        System.out.println(defaultUser);

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(login, password);
        userRegistrationDTO.setPasswordConfirm(password);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .content(asJsonString(userRegistrationDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @AfterEach
    @WithMockUser(roles = "ADMIN")
    void reset() throws Exception {
        User user = userRepository.findByLogin(login);

        if (user != null) {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/users/" + user.getId())
                            .content(asJsonString(defaultUser))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept("application/json"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andReturn();

            assertThrows(RuntimeException.class, () -> userService.findUserByLogin(login), USER_NOT_FOUND);
        }
    }

    @Test
    @DisplayName("GET /users test - users found 200")
    @WithMockUser(roles = "MANAGER")
    void getAllUsersList() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(userRepository.findAll());


    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addingUser_CheckingIfExists_deleteIt() throws Exception {

        User user = userRepository.findByLogin(login);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + user.getId())
                        .content(asJsonString(defaultUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{'login':'" + login + "'}"))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithMockUser(roles = "CEO")
    void addingNewUser_addingANewRole_takeTheRoleAway_DeleteUser_TEST() throws Exception {

        Long userId = userRepository.findByLogin(login).getId();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/"+userId+"/add-role/"+UserRole.Role.BANNED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'login':'test@test1.tst'}"))
                .andReturn();

        List<UserRole> roles = userRoleRepository.findByUserLogin(login);
        assertTrue(roles.stream()
                .anyMatch(x -> x.getRoleName().equals(UserRole.Role.BANNED.roleName())));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/"+userId+"/take-away-role/"+UserRole.Role.BANNED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        roles = userRoleRepository.findByUserLogin(login);
        assertFalse(roles.stream()
                .anyMatch(x -> x.getRoleName().equals(UserRole.Role.BANNED.roleName())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void userRoleRepositoryQueryFindRolesByUserLoginThrowsNoException() {
        assertDoesNotThrow(()->{userRoleRepository.findByUserLogin(login);});
    }



}
