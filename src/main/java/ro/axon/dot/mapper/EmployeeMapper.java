package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.model.EmployeeListItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "teamId", target = "team.id") // Map teamId to team.id
    @Mapping(target = "password", ignore = true) // Ignore password
    @Mapping(target = "role", constant = "USER") // Set default value for role
    @Mapping(target = "version", ignore = true) // Ignore version
    EmployeeEty mapEmployeeDtoToEmployeeEty(EmployeeListItem employeeListItem);

    @Mapping(source = "team.id", target = "teamId") // Map team.id to teamId
    EmployeeListItem mapEmployeeEtyToEmployeeDto(EmployeeEty employeeEty);
}
