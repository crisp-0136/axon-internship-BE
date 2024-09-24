package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.model.LeaveReqDetailsListItems;
import ro.axon.dot.model.LeaveReqDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface LeaveReqMapper {

    LeaveReqMapper INSTANCE = Mappers.getMapper(LeaveReqMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeEty", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "crtUsr", ignore = true)
    @Mapping(target = "crtTms", ignore = true)
    @Mapping(target = "mdfUsr", ignore = true)
    @Mapping(target = "mdfTms", ignore = true)
    @Mapping(target = "noDays", ignore = true)
    @Mapping(target = "rejectReason", ignore = true)
    LeaveReqEty toEntity(LeaveReqDto leaveReqDto);

    @Mapping(source = "employeeEty.firstName", target = "employeeDetails.firstName")
    @Mapping(source = "employeeEty.lastName", target = "employeeDetails.lastName")
    @Mapping(source = "employeeEty.employeeId", target = "employeeDetails.employeeId")
    @Mapping(source = "leaveReqEty.noDays", target = "noOfDays")
    LeaveReqDetailsListItems toDto(LeaveReqEty leaveReqEty);

    @Mapping(target = "employeeEty", ignore = true) // Ignoring employeeEty since it's not in DTO
    @Mapping(target = "noDays", ignore = true) // Ignoring employeeEty since it's not in DTO
    @Mapping(target = "rejectReason", ignore = true) // Ignoring employeeEty since it's not in DTO
    LeaveReqEty toEntity(LeaveReqDetailsListItems leaveReqDto);


}
