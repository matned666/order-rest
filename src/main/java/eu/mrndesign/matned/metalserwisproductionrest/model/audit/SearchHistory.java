package eu.mrndesign.matned.metalserwisproductionrest.model.audit;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.SearchHistoryDTO;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class SearchHistory extends BaseEntity<SearchHistoryDTO> implements AuditInterface {

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

    @Override
    public void applyNew(SearchHistoryDTO editedData) {

    }
}
