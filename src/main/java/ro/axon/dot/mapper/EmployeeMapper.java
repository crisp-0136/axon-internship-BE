package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.model.EmployeeListItem;

import java.rmi.server.UID;
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "employeeId", target = "employeeId")
    @Mapping(source = "team.name", target = "teamName")
    EmployeeListItem mapEmployeeEtyToEmployeeDto(EmployeeEty employeeEty);
}
