package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.ProcessDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Process;
import eu.mrndesign.matned.metalserwisproductionrest.repository.ProcessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith({SpringExtension.class})
@SpringBootTest
class ProcessServiceTest {

    @Autowired
    private ProcessService processService;

    @MockBean
    private ProcessRepository processRepository;

    private List<Process> processes;
    private String[] sortBy;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        processes = new LinkedList<>();
        sortBy = new String[1];
        sortBy[0] = "something";
        pageable = processService.getPageable(1, 10, sortBy);
        for (int i = 1; i <= 3; i++) {
            processes.add(new Process("Process"+i, "Process"+i+" description"));
        }
    }

    @Test
    void saveProcess() {
        doReturn(processes.get(0)).when(processRepository).save(any());
        assertEquals("Process1", processService.saveProcess(new ProcessDTO("a","a")).getName());
    }

    @Test
    void saveProcessFailsWithRuntimeExWhenEmptyDataGiven() {
        doReturn(processes.get(0)).when(processRepository).save(any());
        assertThrows(RuntimeException.class, ()-> processService.saveProcess(null));
    }

    @Test
    void findAllProcesses() {
        doReturn(new PageImpl<>(processes.subList(0, 3), pageable, 3)).when(processRepository).findAll(any(Pageable.class));
        assertEquals(3, processService.findAllProcesses(1,1,sortBy).size());
    }

    @Test
    void findProcessById() {
        doReturn(Optional.of(processes.get(0))).when(processRepository).findById(any());
        assertEquals(ProcessDTO.apply(processes.get(0)), processService.findProcessById(1L));
    }

    @Test
    void findProcessByIdThrowsRuntimeExWhenNoProcessFound() {
        doReturn(Optional.empty()).when(processRepository).findById(any());
        assertThrows(RuntimeException.class, ()-> processService.findProcessById(1L));
    }

    @Test
    void editProcess() {
        doReturn(Optional.of(processes.get(0))).when(processRepository).findById(any());
        doReturn(processes.get(0)).when(processRepository).save(any());

        ProcessDTO updateData = new ProcessDTO("testing-change", null);
        ProcessDTO dto = processService.editProcess(0L, updateData);

        assertEquals(ProcessDTO.apply(processes.get(0)).getName(), "testing-change");
        assertEquals(ProcessDTO.apply(processes.get(0)).getDescription(), "Process1 description");

        updateData = new ProcessDTO("", "");
        dto = processService.editProcess(0L, updateData);

        assertEquals(ProcessDTO.apply(processes.get(0)).getName(), "testing-change");
        assertEquals(ProcessDTO.apply(processes.get(0)).getDescription(), "Process1 description");


    }

    @Test
    void editThrowsExceptionWhenProcessNotFound(){
        doReturn(Optional.empty()).when(processRepository).findById(any());
        assertThrows(RuntimeException.class, ()-> processService.editProcess(1L,ProcessDTO.apply(processes.get(0))));
    }
}
