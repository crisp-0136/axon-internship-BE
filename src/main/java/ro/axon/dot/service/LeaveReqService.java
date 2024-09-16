package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.Enums.Status;
import ro.axon.dot.domain.Enums.Type;
import ro.axon.dot.domain.LeaveReqRepository;
import ro.axon.dot.mapper.LeaveReqMapper;
import ro.axon.dot.model.LeaveReqItem;
import ro.axon.dot.model.LeaveReqList;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveReqService {
    private final LeaveReqRepository leaveReqRepository;

    public LeaveReqItem initializeLeaveReqItem() {
        LeaveReqItem leaveReqItem = new LeaveReqItem();
        leaveReqItem.setId(1L);
        leaveReqItem.setEmployeeId("E123");
        leaveReqItem.setCrtUsr("admin");
        leaveReqItem.setCrtTms(Instant.now());
        leaveReqItem.setMdfUsr("admin");
        leaveReqItem.setMdfTms(Instant.now());
        leaveReqItem.setStartDate(new Date());  // Set the current date
        leaveReqItem.setEndDate(new Date());    // Set the current date for now
        leaveReqItem.setNoDays(5);              // Example value
        leaveReqItem.setType(Type.VACATION_LEAVE); // Assuming Type enum is defined
        leaveReqItem.setStatus(Status.PENDING);   // Assuming Status enum is defined
        leaveReqItem.setDescription("Annual vacation leave");
        leaveReqItem.setRejectReason("None");

        return leaveReqItem;
    }

    public LeaveReqList getLeaveList(){
        var leaveReqList = new LeaveReqList();
        leaveReqList.setItems(leaveReqRepository.findAll().stream().map(LeaveReqMapper.INSTANCE::mapLeaveReqEtyToLeaveReqDto)
                .collect(Collectors.toList()));
        return leaveReqList;
    }

}
