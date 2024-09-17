package ro.axon.dot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ro.axon.dot.domain.component.RolesComponent;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RolesMapper {
    RolesMapper INSTANCE = Mappers.getMapper(RolesMapper.class);

    RolesComponent mapRolesYAMLToRoles(RolesComponent yamlRolesComponent);
}