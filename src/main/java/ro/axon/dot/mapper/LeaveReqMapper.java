package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.model.LeaveReqItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LeaveReqMapper {

    LeaveReqMapper INSTANCE = Mappers.getMapper(LeaveReqMapper.class);
    @Mapping(source = "employee.employeeId", target = "employeeId")
    LeaveReqItem mapLeaveReqEtyToLeaveReqDto(LeaveReqEty leaveReqEty);
}
