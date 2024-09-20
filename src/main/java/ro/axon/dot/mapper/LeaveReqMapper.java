package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.LeaveReqEty;
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


}
