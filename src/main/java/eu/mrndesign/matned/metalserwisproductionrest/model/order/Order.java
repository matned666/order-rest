package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "CLIENT_ORDER")
public class Order extends BaseEntity implements AuditInterface {

    private String product;
    private int desiredQuantity;
    private int quantityDone;
    private String description;
    private Date orderDate;
    private Date orderDeadline;

    private boolean isDone;

    @ManyToOne
    private ClientEntity client;
    @ManyToOne
    private Delivery delivery;

    @ManyToMany
    private List<Process> processes;

    public Order() {
    }

    public Order(OrderBuilder builder) {
        this.product = builder.product;
        this.desiredQuantity = builder.desiredQuantity;
        this.description = builder.description;
        this.orderDate = builder.orderDate;
        this.orderDeadline = builder.orderDeadline;
        this.delivery = builder.delivery;
        this.client = builder.client;
        this.processes = new LinkedList<>();
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getDesiredQuantity() {
        return desiredQuantity;
    }

    public void setDesiredQuantity(int desiredQuantity) {
        this.desiredQuantity = desiredQuantity;
    }

    public int getQuantityDone() {
        return quantityDone;
    }

    public void setQuantityDone(int quantityDone) {
        this.quantityDone = quantityDone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(Date orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public ClientEntity getClient() {
        return client;
    }

    public static class OrderBuilder{

        private String product;
        private int desiredQuantity;
        private String description;
        private Date orderDate;
        private Date orderDeadline;
        private Delivery delivery;
        private ClientEntity client;

        public OrderBuilder(String product, int desiredQuantity) {
            this.product = product;
            this.desiredQuantity = desiredQuantity;
        }

        public OrderBuilder description(String description){
            this.description = description;
            return this;
        }

        public OrderBuilder orderDate(Date orderDate){
            this.orderDate = orderDate;
            return this;
        }

        public OrderBuilder orderDeadline(Date orderDeadline){
            this.orderDeadline = orderDeadline;
            return this;
        }

        public OrderBuilder delivery(Delivery delivery){
            this.delivery = delivery;
            return this;
        }

        public OrderBuilder client(ClientEntity client){
            this.client = client;
            return this;
        }

        public Order build(){
            return new Order(this);
        }
        
        
    }

    
}
