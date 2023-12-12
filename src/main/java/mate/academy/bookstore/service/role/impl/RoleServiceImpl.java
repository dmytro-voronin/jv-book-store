package mate.academy.bookstore.service.role.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.repository.role.RoleRepository;
import mate.academy.bookstore.service.role.RoleService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByRoleName(Role.RoleName roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(() ->
                new EntityNotFoundException("can't find role by roleName: " + roleName));
    }
}
