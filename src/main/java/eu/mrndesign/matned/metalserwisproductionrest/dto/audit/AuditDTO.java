package eu.mrndesign.matned.metalserwisproductionrest.dto.audit;

import java.io.Serializable;
import java.util.Objects;

public class AuditDTO implements Serializable {

    private Long id;
    private Long version;
    private Long createdById;
    private String createdDate;
    private Long lastModifiedById;
    private String lastModifiedDate;

    private AuditDTO(AuditBuilder builder){
        this.id = builder.id;
        this.version = builder.version;
        this.createdById = builder.createdById;
        this.createdDate = builder.createdDate;
        this.lastModifiedById = builder.lastModifiedById;
        this.lastModifiedDate = builder.lastModifiedDate;
    }

    public AuditDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Long lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditDTO auditDTO = (AuditDTO) o;
        return id.equals(auditDTO.id) &&
                Objects.equals(version, auditDTO.version) &&
                Objects.equals(createdById, auditDTO.createdById) &&
                Objects.equals(createdDate, auditDTO.createdDate) &&
                Objects.equals(lastModifiedById, auditDTO.lastModifiedById) &&
                Objects.equals(lastModifiedDate, auditDTO.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, createdById, createdDate, lastModifiedById, lastModifiedDate);
    }

    @Override
    public String toString() {
        return "AuditDTO{" +
                "id=" + id +
                ", version=" + version +
                ", createdById=" + createdById +
                ", createdDate='" + createdDate + '\'' +
                ", lastModifiedById=" + lastModifiedById +
                ", lastModifiedDate='" + lastModifiedDate + '\'' +
                '}';
    }

    public static class AuditBuilder{

        private Long id;
        private Long version;
        private Long createdById;
        private String createdDate;
        private Long lastModifiedById;
        private String lastModifiedDate;

        public AuditBuilder() {
        }

        public AuditBuilder version(Long version){
            this.version=version;
            return this;
        }

        public AuditBuilder createdById(Long createdById){
            this.createdById=createdById;
            return this;
        }

        public AuditBuilder createdDate(String createdDate){
            this.createdDate=createdDate;
            return this;
        }

        public AuditBuilder lastModifiedById(Long lastModifiedById){
            this.lastModifiedById=lastModifiedById;
            return this;
        }

        public AuditBuilder lastModifiedDate(String lastModifiedDate){
            this.lastModifiedDate=lastModifiedDate;
            return this;
        }

        public AuditBuilder id(Long id){
            this.id=id;
            return this;
        }

        public AuditDTO build(){
            return new AuditDTO(this);
        }
    }
}
