package eu.mrndesign.matned.metalserwisproductionrest.dto;

import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.security.UserRole;

import java.util.Objects;

public class UserRoleDTO {

    public static UserRoleDTO applyWithAudit(UserRole entity){
        UserRoleDTO dto = apply(entity);
        dto.auditDTO = AuditInterface.apply(entity);
        return dto;
    }

    public static UserRoleDTO apply(UserRole entity){
        return new UserRoleDTO(entity.getRoleName());
    }

    private String roleName;
    private AuditDTO auditDTO;

    public UserRoleDTO() {
    }

    public UserRoleDTO(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public AuditDTO getAuditDTO() {
        return auditDTO;
    }

    public void setAuditDTO(AuditDTO auditDTO) {
        this.auditDTO = auditDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDTO that = (UserRoleDTO) o;
        return roleName.equals(that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }

    @Override
    public String toString() {
        return "UserRoleDTO{" +
                "roleName='" + roleName + '\'' +
                ", auditDTO=" + auditDTO +
                '}';
    }
}
