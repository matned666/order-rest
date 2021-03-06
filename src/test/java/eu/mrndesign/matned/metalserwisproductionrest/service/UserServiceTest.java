package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static eu.mrndesign.matned.metalserwisproductionrest.service.UserService.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRoleRepository userRoleRepository;

    private List<User> allUsers;

    @BeforeEach
    void setup(){
        allUsers = new LinkedList<>();
        User user;
        for (int i = 1; i <= 3; i++) {
            user = new User("test@test"+i+".tst", passwordEncoder.encode("test"+i));
            user.addRole(new UserRole(UserRole.Role.MANAGER));
            user.addRole(new UserRole(UserRole.Role.USER));
            allUsers.add(user);
            System.out.println(user);
        }
    }

    @Test
    void isCollectingUsersFromRepositoryWorking(){
        doReturn(allUsers).when(userRepository).findAll();

        assertEquals(userService.findAll().size(), 3);
    }

    @Test
    void isThrowingRuntimeExceptionWhenUserNotFound(){
        doReturn(Optional.empty()).when(userRepository).findById(any());

        assertThrows(RuntimeException.class, ()->{userService.findUserById(1L);}, USER_NOT_FOUND);
    }

    @Test
    void returnedUserDTOFieldsAreTheSameAsReceivedUserFromRepository(){
        doReturn(Optional.of(allUsers.get(0))).when(userRepository).findById(any());

        UserDTO userDTO = userService.findUserById(1L);

        assertEquals(allUsers.get(0).getLogin(), userDTO.getLogin());
    }

    @Test
    void passwordSentToDatabaseIsStillEncryptedInTheDTO(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("test3@test.tst", "test3");
        doReturn(User.applyRegistrationDTO(userRegistrationDTO)).when(userRepository).save(any());

        userService.add(userRegistrationDTO);
        System.out.println("Encrypted password: " + userRegistrationDTO.getPassword());
        assertNotEquals("test3", userRegistrationDTO.getPassword());
    }

    @Test
    void addingNewUserReturnsCorrectData(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("test3@test.tst", "test3");
        doReturn(User.applyRegistrationDTO(userRegistrationDTO)).when(userRepository).save(any());

        assertEquals(userService.add(userRegistrationDTO).getLogin(), "test3@test.tst");
    }

    @Test
    void registratedPasswordIsUpdatedAndSentToDatabaseEncryptedAndDataIsEqualToTheGiven(){
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("test3@test.tst", "test3");
        doReturn(Optional.of(allUsers.get(0))).when(userRepository).findById(any());

        userService.updatePassword(0L, userRegistrationDTO);

        assertEquals(allUsers.get(0).getPassword(), userRegistrationDTO.getPassword());
    }


    @Test
    void sentLoginThatWasChangedIsEqualToSentData(){
        UserDTO userDTO = new UserDTO("test3@test.tst", new AuditDTO.AuditBuilder().build());
        doReturn(Optional.of(allUsers.get(0))).when(userRepository).findById(any());
        doReturn(allUsers.get(0)).when(userRepository).save(any());

        userService.updateLogin(0L, userDTO);

        assertEquals(allUsers.get(0).getLogin(), "test3@test.tst");
    }

    @Test
    void roleCanBeAssignedToUserAndTakenAwayFromHimOrHerInDatabaseAndTryToRemoveTheSameRoleSecondTimeThrowExceptionWithMessage(){
        UserRole adminRole = new UserRole(UserRole.Role.ADMIN);

        doReturn(Optional.of(allUsers.get(0))).when(userRepository).findById(any());
        doReturn(allUsers.get(0)).when(userRepository).save(any());
        doReturn(adminRole).when(userRoleRepository).findByRoleName(any());
//        ASSIGNING ROLE
        userService.assignNewRole(0L, UserRole.Role.ADMIN.name());

        assertTrue(allUsers.get(0).getRoles().contains(adminRole));
//        TAKING THE ROLE AWAY
        userService.takeAwayRole(0L, UserRole.Role.ADMIN.name());

        assertFalse(allUsers.get(0).getRoles().contains(adminRole));
//        SECOND TIME TAKING THE SAME ROLE (NOT EXISTING) DOESNT DO ANYTHING
        userService.takeAwayRole(0L, UserRole.Role.ADMIN.name());

        assertEquals(allUsers.get(0).getRoles().size(), 2);
    }






}
