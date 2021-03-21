package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ClientDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.CLIENT_EXISTS;
import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    private List<ClientEntity> clients;
    private String[] sortBy;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        clients = new LinkedList<>();
        sortBy = new String[1];
        sortBy[0] = "something";
        pageable = clientService.getPageable(1, 10, sortBy);
        for (int i = 1; i <= 3; i++) {
            clients.add(new ClientEntity("client"+i, "description"+i));
        }
    }

    @Test
    void findingAllClients(){
        doReturn(new PageImpl<>(clients.subList(0, 3), pageable, 3)).when(clientRepository).findAll(any(Pageable.class));
        assertEquals(3, clientService.findAllClients(1,1,sortBy).size());
    }

    @Test
    void findOne(){
        doReturn(Optional.of(clients.get(0))).when(clientRepository).findById(any());
        assertEquals(ClientDTO.apply(clients.get(0)), clientService.findClientById(1L));
    }

    @Test
    void saveReturnsRightDTO(){
        doReturn(false).when(clientRepository).existsByClientName(any());
        doReturn(clients.get(0)).when(clientRepository).save(any());
        assertEquals(ClientDTO.apply(clients.get(0)), clientService.saveClient(ClientDTO.apply(clients.get(0))));
    }

    @Test
    void testThrowsRuntimeExceptionWhenTryingToSaveExistingClient(){
        doReturn(true).when(clientRepository).existsByClientName(any());
        assertThrows(RuntimeException.class, ()-> clientService.saveClient(ClientDTO.apply(clients.get(0))));
    }

    @Test
    void findAllClientsWithActiveOrders(){
        doReturn(new PageImpl<>(clients.subList(0, 3), pageable, 3)).when(clientRepository).findAllWithActiveOrders(any(Pageable.class));
        assertEquals(3, clientService.findAllClientsWithActiveOrders(1,1,sortBy).size());
    }

    @Test
    void editClient(){
        doReturn(Optional.of(clients.get(0))).when(clientRepository).findById(any());
        doReturn(clients.get(0)).when(clientRepository).save(any());
        assertEquals(clientService.saveClient(ClientDTO.apply(clients.get(0))).getName(), "client1");
        assertEquals(clientService.saveClient(ClientDTO.apply(clients.get(0))).getDescription(), "description1");
    }

    @Test
    void editThrowsExceptionWhenClientNotFound(){
        doReturn(Optional.empty()).when(clientRepository).findById(any());
        assertThrows(RuntimeException.class, ()-> clientService.editClient(1L,ClientDTO.apply(clients.get(0))));
    }
}
