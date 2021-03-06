package eu.mrndesign.matned.metalserwisproductionrest.config;

import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRoleRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeed implements InitializingBean {

    @Value("${default.admin.username}")
    private String defaultAdminLogin;

    @Value("${default.admin.password}")
    private String defaultUserPassword;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public DataSeed(PasswordEncoder passwordEncoder,
                    UserRepository userRepository,
                    UserRoleRepository userRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (UserRole.Role role : UserRole.Role.values()) {
            createRole(role);
        }
        createDefaultUser();
    }



    private void createRole(UserRole.Role role) {
        String roleCheck = role.roleName();
        if (!userRoleRepository.roleExists(roleCheck)) {
            userRoleRepository.save(UserRole.apply(role));
        }
    }

    private void createDefaultUser() {

        if (!userRepository.existsByLogin(defaultAdminLogin)) {
            User defaultUser = new User();
            defaultUser.setLogin(defaultAdminLogin);
            defaultUser.setPassword(passwordEncoder.encode(defaultUserPassword));
            UserRole role = userRoleRepository.findByRoleName(UserRole.Role.ADMIN.roleName());
            defaultUser.addRole(role);
            userRepository.save(defaultUser);
        }
    }

}
