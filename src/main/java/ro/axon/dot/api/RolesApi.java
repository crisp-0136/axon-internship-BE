package ro.axon.dot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.axon.dot.model.RolesList;
import ro.axon.dot.service.RolesService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RolesApi {

    private final RolesService rolesService;

    @GetMapping("/misc/roles")
    public ResponseEntity<RolesList> getRolesService() {
        RolesList rolesList = rolesService.getMyRoles();
        return ResponseEntity.ok(rolesList);
    }
}