package eu.mrndesign.matned.metalserwisproductionrest.model.audit;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class SearchHistory extends BaseEntity implements AuditInterface {

    @Column(updatable = false)
    private String query;

    public SearchHistory() {
    }

    public SearchHistory(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "query='" + query + '\'' +
                '}';
    }
}
