package eu.mrndesign.matned.metalserwisproductionrest.model.audit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableName;
    private String beforeStatement;
    private String afterStatement;
    private String operationType;

    public AuditHistory() {
    }

    public AuditHistory(String table, String before, String after, OperationType operationType) {
        this.tableName = table;
        this.beforeStatement = before;
        this.afterStatement = after;
        this.operationType = operationType.name();

    }

    public Long getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }

    public String getBeforeStatement() {
        return beforeStatement;
    }

    public String getAfterStatement() {
        return afterStatement;
    }

    public String getOperationType() {
        return operationType;
    }

    public enum OperationType{
        ADD,
        UPDATE,
        DELETE
    }
}
