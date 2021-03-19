package eu.mrndesign.matned.metalserwisproductionrest.service;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRepository;
import eu.mrndesign.matned.metalserwisproductionrest.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static eu.mrndesign.matned.metalserwisproductionrest.utils.Exceptions.USER_NOT_FOUND;


@Service
public class UserService extends BaseService{

    @Value("${default.admin.username}")
    private String defaultAdminLogin;

    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(PasswordEncoder passwordEncoder,
                       HttpServletRequest request,
                       UserRepository userRepository,
                       UserRoleRepository userRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.request = request;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserDTO> findAll(){
        return convertEntityToUserDTOList(userRepository.findAll());
    }

    public List<UserDTO> findAll(Integer startPage, Integer itemsPerPage, String[] sortBy) {
        Pageable pageable = getPageable(startPage, itemsPerPage, sortBy);
        Page<User> users = userRepository.findAll(pageable);
        List<User> _users = users.getContent();
        return convertEntityToUserDTOList(_users);
    }

    public UserDTO findUserById(Long id) {return UserDTO.applyWithAudit(userRepository.findById(id).orElseThrow(()->new RuntimeException(USER_NOT_FOUND)));}

    public UserDTO add(UserRegistrationDTO dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserRole role = userRoleRepository.findByRoleName(UserRole.Role.USER.roleName());
        return UserDTO.applyWithAudit(userRepository.save(User.applyRegistration(dto, role)));
    }

    public UserDTO updatePassword(Long id, UserRegistrationDTO dto){
        User entity = userRepository.findById(id).orElseThrow(()-> new RuntimeException(USER_NOT_FOUND));
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));  //  encoding password inside DTO for a better security
        entity.setPassword(dto.getPassword());
        userRepository.save(entity);
        return UserDTO.applyWithAudit(entity);
    }

    public UserDTO updateLogin(Long id, UserDTO dto){
        User entity = userRepository.findById(id).orElseThrow(()->new RuntimeException(USER_NOT_FOUND));
        entity.setLogin(dto.getLogin());
        return UserDTO.applyWithAudit(userRepository.save(entity));
    }

    public UserDTO assignNewRole(Long userId, String role){
        UserRole.Role parsedRole = UserRole.Role.valueOf(role);
        User entity = userRepository.findById(userId).orElseThrow(()->new RuntimeException(USER_NOT_FOUND));
        UserRole userRole = userRoleRepository.findByRoleName(parsedRole.roleName());
        entity.addRole(userRole);
        return UserDTO.applyWithAudit(userRepository.save(entity));
    }

    public UserDTO takeAwayRole(Long userId, String role){
        UserRole.Role parsedRole = UserRole.Role.valueOf(role);
        User entity = userRepository.findById(userId).orElseThrow(()->new RuntimeException(USER_NOT_FOUND));
        UserRole userRole = userRoleRepository.findByRoleName(parsedRole.roleName());
        entity.removeRole(userRole);
        return UserDTO.applyWithAudit(userRepository.save(entity));
    }



    public List<UserDTO> deleteUser(Long id) {
        userRepository.deleteById(id);
        return findAll(defaultStartPage, defaultPageSize, new String[]{defaultSortBy});
    }


    public UserDTO findUserByLogin(String login){
        return UserDTO.applyWithAudit(userRepository.findByLogin(login));
    }

    public Optional<User> getAuditor() {
        return userRepository.findAuditorByLogin(request.getUserPrincipal() != null ?
                request.getUserPrincipal().getName() :
                defaultAdminLogin);
    }


//            Private

    List<UserDTO> convertEntityToUserDTOList(List<User> all) {
        return all.stream()
                .map(UserDTO::applyWithAudit)
                .collect(Collectors.toList());
    }
}
