package ro.axon.dot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LegallyDaysOffEty;
import ro.axon.dot.domain.repositories.LegallyDaysOffRepository;
import ro.axon.dot.model.LeaveReqDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LeaveReqServiceTest {

    @Mock
    private LegallyDaysOffRepository legallyDaysOffRepository;

    @InjectMocks
    private LeaveReqService leaveReqService;

    private LeaveReqDto leaveReqDto;
    private EmployeeEty employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        leaveReqDto = new LeaveReqDto();
        leaveReqDto.setStartDate(LocalDate.of(2024, 9, 27)); // Friday
        leaveReqDto.setEndDate(LocalDate.of(2024, 9, 30));   // Monday


        employee = new EmployeeEty();
        employee.setEmployeeId("EMP123");


        LegallyDaysOffEty legallyOffDay = new LegallyDaysOffEty();
        legallyOffDay.setDate(LocalDate.of(2024, 9, 29)); // Sunday


        when(legallyDaysOffRepository.findByPeriodIn(List.of("2024-09")))
                .thenReturn(List.of(legallyOffDay));
    }

    @Test
    public void testCalculateEffectiveDaysRequestedWithWeekendOverlap() {
        int effectiveLeaveDays = leaveReqService.calculateEffectiveDaysRequested(leaveReqDto, employee);

        assertEquals(2, effectiveLeaveDays);
    }
}
