package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ProcessDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ProcessRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.NO_SUCH_PROCESS;

@Service
public class ProcessService extends BaseService{

    private final ProcessRepository processRepository;

    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }


    public ProcessDTO saveProcess(ProcessDTO dto){
        return ProcessDTO.apply(processRepository.save(new Process(dto.getName(), dto.getDescription())));
    }

    public List<ProcessDTO> findAllProcesses(Integer startPage, Integer itemsPerPage, String[] sortBy){
        return processRepository.findAll(getPageable(startPage, itemsPerPage, sortBy)).stream()
                .map(ProcessDTO::apply)
                .collect(Collectors.toList());
    }

    public ProcessDTO findProcessById(Long id){
        return ProcessDTO.apply(processRepository.findById(id).orElseThrow(()->new RuntimeException(NO_SUCH_PROCESS)));
    }

    public ProcessDTO editProcess(Long id, ProcessDTO editedData){
        Process toEdit = processRepository.findById(id).orElseThrow(()->new RuntimeException(NO_SUCH_PROCESS));
        toEdit.applyNew(editedData);
        return ProcessDTO.apply(processRepository.save(toEdit));
    }


}
