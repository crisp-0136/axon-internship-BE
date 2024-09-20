package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.model.AddEmployeeDto;
import ro.axon.dot.model.EmployeeListItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);


    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "crtUsr", ignore = true)
    @Mapping(target = "crtTms", ignore = true)
    @Mapping(target = "mdfUsr", ignore = true)
    @Mapping(target = "mdfTms", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "contractEndDate", ignore = true)
    @Mapping(target = "leaveRequestEties", ignore = true)
    @Mapping(target = "empYearlyDaysOffEties", ignore = true)
    @Mapping(target = "team", ignore = true)
    EmployeeEty dtoToEntity(AddEmployeeDto employeeDto);
}
