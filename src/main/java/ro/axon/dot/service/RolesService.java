package ro.axon.dot.service;

import org.springframework.stereotype.Service;
import ro.axon.dot.domain.component.RolesComponent;
import ro.axon.dot.model.RolesList;

import javax.transaction.Transactional;

@Service
public class RolesService {

    private final RolesComponent rolesComponent;

    public RolesService(RolesComponent rolesComponent) {
        this.rolesComponent = rolesComponent;
    }

    @Transactional
    public RolesList getMyRoles() {
        return new RolesList(rolesComponent.getRoles());
    }
}