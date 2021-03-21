package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ClientDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.ClientEntity;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ClientRepository;
import eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.CLIENT_DOESN_T_EXIST;
import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.CLIENT_EXISTS;

@Service
public class ClientService extends BaseService{

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDTO saveClient(ClientDTO dto) {
        if (clientRepository.existsByClientName(dto.getName()))
            throw new RuntimeException(CLIENT_EXISTS);
        else return ClientDTO.apply(clientRepository.save(new ClientEntity(dto.getName(), dto.getDescription())));
    }

    public List<ClientDTO> findAllClients(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return clientRepository.findAll(getPageable(startPage, itemsPerPage, sortBy)).stream()
                .map(ClientDTO::apply)
                .collect(Collectors.toList());
    }

    public List<ClientDTO> findAllClientsWithActiveOrders(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        return clientRepository.findAllWithActiveOrders(getPageable(startPage, itemsPerPage, sortBy)).stream()
                .map(ClientDTO::apply)
                .collect(Collectors.toList());

    }

    public ClientDTO findClientById(Long id) {
        return ClientDTO.apply(findClientByIdService(id));
    }

    public ClientDTO editClient(Long id, ClientDTO editedData) {
        if(editedData != null) {
            ClientEntity entity = findClientByIdService(id);
            if (editedData.getName() != null) if (!editedData.getName().isEmpty())
                entity.setClientName(editedData.getName());
            if (editedData.getDescription() != null) if (!editedData.getDescription().isEmpty())
                entity.setClientDescription(editedData.getDescription());
            return ClientDTO.apply(clientRepository.save(entity));
        }
        throw new RuntimeException(Exceptions.WRONG_DATA_GIVEN);
    }


    private ClientEntity findClientByIdService(Long id){
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException(CLIENT_DOESN_T_EXIST));
    }

}
