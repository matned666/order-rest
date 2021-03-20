package eu.mrndesign.matned.metalserwisproductionrest.model.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.order.OrderDTO;
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
public class Order extends BaseEntity<OrderDTO> implements AuditInterface {

    public static Order apply(OrderDTO dto, List<Process> processes, ClientEntity client, Delivery delivery){
        return new OrderBuilder(dto.getProduct(), dto.getDesiredQuantity())
                .client(client)
                .delivery(delivery)
                .description(dto.getDescription())
                .orderDate(dto.getOrderDate())
                .orderDeadline(dto.getOrderDeadline())
                .addProcesses(processes)
                .build();
    }

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
        this.processes.addAll(builder.processes);
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

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public void applyNew(OrderDTO dto) {
        this.product = dto.getProduct();
        this.desiredQuantity = dto.getDesiredQuantity();
        this.quantityDone = dto.getQuantityDone();
        this.description = dto.getDescription();
        if(dto.getOrderDate() != null) this.orderDate = dto.getOrderDate();
        if(dto.getOrderDate() != null) this.orderDeadline = dto.getOrderDeadline();
    }

    public void applyNewProcessList(List<Process> allById) {
        this.processes.clear();
        this.processes.addAll(allById);
    }

    public static class OrderBuilder{

        private String product;
        private int desiredQuantity;
        private String description;
        private Date orderDate;
        private Date orderDeadline;
        private Delivery delivery;
        private ClientEntity client;
        private List<Process> processes;


        public OrderBuilder(String product, int desiredQuantity) {
            this.product = product;
            this.desiredQuantity = desiredQuantity;
            processes = new LinkedList<>();
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

        public OrderBuilder addProcesses(List<Process> processes){
            this.processes.addAll(processes);
            return this;
        }




        public Order build(){
            return new Order(this);
        }
        
        
    }

    
}
