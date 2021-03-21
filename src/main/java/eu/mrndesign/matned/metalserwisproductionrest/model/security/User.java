package eu.mrndesign.matned.metalserwisproductionrest.model.security;

import eu.mrndesign.matned.metalserwisproductionrest.dto.UserDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.UserRegistrationDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USER_ENTITY")
public class User extends BaseEntity<UserDTO> implements AuditInterface {

    public static final String ROLE_NOT_FOUND = "Role not found";

    public static User applyRegistration(UserRegistrationDTO dto, UserRole role) {
        User register = new User(dto.getLogin(), dto.getPassword());
        register.addRole(role);
        return register;
    }

    public static User applyRegistrationDTO(UserRegistrationDTO dto) {
        return new User(dto.getLogin(), dto.getPassword());
    }



    private String login;

    private String password;

    @ManyToMany
    private List<UserRole> roles;

    public User() {
    }

    public User(String login) {
        this.login = login;
        roles = new LinkedList<>();
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        roles = new LinkedList<>();
    }


    public void addRole(UserRole userRole){
        if(isRoleCorrect(userRole)) return;
        if (roles == null) roles = new ArrayList<>();
        roles.add(userRole);
    }

    public void removeRole(UserRole userRole) {
        if(roles != null){
            roles.remove(roles.stream()
            .filter(x->x.getRoleName().equals(userRole.getRoleName()))
            .findFirst().orElse(null));
        }
    }

    private boolean isRoleCorrect(UserRole userRole) {
        if (roles == null) return false;
        return roles.stream().anyMatch(r -> r.getRoleName().equals(userRole.getRoleName()));
    }

    public String getLogin() {
        return login;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    @Override
    public void applyNew(UserDTO editedData) {

    }
}

