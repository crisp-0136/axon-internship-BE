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

    @Mapping(source = "id", target = "id", qualifiedByName = "uidToString")
    @Mapping(source = "team.name", target = "teamName") // Map the team's name to teamName
    EmployeeListItem mapEmployeeEtyToEmployeeDto(EmployeeEty employeeEty);

    @Named("uidToString")
    default String map(UID uid) {
        return uid != null ? uid.toString() : null;
    }
}
