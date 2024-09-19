package ro.axon.dot.service;

import org.springframework.stereotype.Service;
import ro.axon.dot.domain.component.RolesComponent;
import ro.axon.dot.model.RolesList;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RolesService {

    private final RolesComponent rolesComponent;

    public RolesService(RolesComponent rolesComponent) {
        this.rolesComponent = rolesComponent;
    }

    @Transactional(readOnly=true)
    public RolesList getMyRoles() {
        return new RolesList(rolesComponent.getRoles());
    }
}