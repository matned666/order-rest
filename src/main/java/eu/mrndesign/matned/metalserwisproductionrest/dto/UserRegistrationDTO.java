package eu.mrndesign.matned.metalserwisproductionrest.dto;

import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.dto.validation.PasswordMatches;
import eu.mrndesign.matned.metalserwisproductionrest.dto.validation.PasswordValidationObjectInterface;
import eu.mrndesign.matned.metalserwisproductionrest.dto.validation.UniqueEmail;
import eu.mrndesign.matned.metalserwisproductionrest.dto.validation.ValidPassword;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserRegistrationDTO implements PasswordValidationObjectInterface {

    public static UserRegistrationDTO applyWithAudit(User entity){
        return new UserRegistrationDTO(entity.getLogin(), entity.getPassword(), AuditInterface.apply(entity));
    }

    public static UserRegistrationDTO apply(User entity){
        return new UserRegistrationDTO(entity.getLogin(), entity.getPassword());
    }

    @UniqueEmail(message = "It should be a unique email")
    @NotEmpty(message = "The value cannot be empty")
    @Size(min = 5, message = "The login must be at least {min} signs long")
    @Pattern(
            regexp = ".{1,}@.{1,}[.].{2,3}",
            message = "It should be a valid email address"
    )
    private String login;

    @ValidPassword
    @NotEmpty(message = "The value cannot be empty")
    private String password;

    private String passwordConfirm;

    private AuditDTO auditDTO;

    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserRegistrationDTO(String login, String password, AuditDTO auditDTO) {
        this.login = login;
        this.password = password;
        this.auditDTO = auditDTO;
    }





    public String getLogin() {
        return login;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public AuditDTO getAuditDTO() {
        return auditDTO;
    }

    public void setAuditDTO(AuditDTO auditDTO) {
        this.auditDTO = auditDTO;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", auditDTO=" + auditDTO +
                '}';
    }
}
