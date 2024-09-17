package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeeMapper {

}
