package eu.mrndesign.matned.metalserwisproductionrest.dto.order;

import eu.mrndesign.matned.metalserwisproductionrest.dto.DTOEntityDescriptionImplementation;
import eu.mrndesign.matned.metalserwisproductionrest.dto.audit.AuditDTO;
import eu.mrndesign.matned.metalserwisproductionrest.model.audit.AuditInterface;
import eu.mrndesign.matned.metalserwisproductionrest.model.order.Order;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OrderDTO implements DTOEntityDescriptionImplementation {

    public static OrderDTO apply(Order entity) {
        OrderDTO dto = new OrderDTO.OrderDTOBuilder(entity.getProduct(), entity.getDesiredQuantity())
                .quantityDone(entity.getQuantityDone())
                .description(entity.getDescription())
                .orderDate(entity.getOrderDate() != null ? entity.getOrderDate() : new Date(System.currentTimeMillis()))
                .isDone(entity.isDone())
                .isActive(entity.isActive())
                .clientName(entity.getClient().getClientName())
                .delivery(entity.getDelivery().getDescription())
                .build();
        if (dto.orderDeadline != null)
            dto.orderDeadline = entity.getOrderDeadline();
        entity.getProcesses().forEach(x->dto.addProcess(x.getProcessName()));
        return dto;
    }

    public static OrderDTO applyWithAudit(Order entity) {
        OrderDTO dto = apply(entity);
        dto.auditDTO = AuditInterface.apply(entity);
        return dto;
    }

    @NotNull
    @NotEmpty
    private String product;

    @NotNull
    @NotEmpty
    private int desiredQuantity;

    private int quantityDone;
    private String description;
    private Date orderDate;
    private Date orderDeadline;
    private boolean isDone;
    private boolean isActive;
    private String clientName;
    private String delivery;
    private List<String> processes;
    private AuditDTO auditDTO;

    private OrderDTO(OrderDTOBuilder builder) {
        this.product = builder.product;
        this.description = builder.description;
        this.desiredQuantity = builder.desiredQuantity;
        this.quantityDone = builder.quantityDone;
        this.description = builder.description;
        this.orderDate = builder.orderDate != null ? builder.orderDate : new Date(System.currentTimeMillis());
        this.orderDeadline = builder.orderDeadline;
        this.isDone = builder.isDone;
        this.isActive = builder.isActive;
        this.clientName = builder.clientName;
        this.delivery = builder.delivery;

        this.processes = new LinkedList<>();
        this.processes.addAll(builder.processes);
    }



    public String getProduct() {
        return product;
    }

    public int getDesiredQuantity() {
        return desiredQuantity;
    }

    public int getQuantityDone() {
        return quantityDone;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getOrderDeadline() {
        return orderDeadline;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDelivery() {
        return delivery;
    }

    public List<String> getProcesses() {
        return processes;
    }

    public void addProcess(String process) {
        this.processes.add(process);
    }

    public void removeProcess(String process) {
        processes.remove(process);
    }

    public AuditDTO getAuditDTO() {
        return auditDTO;
    }

    @Override
    public String getName() {
        return product;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static class OrderDTOBuilder {

        private String product;
        private int desiredQuantity;

        private int quantityDone;
        private String description;
        private Date orderDate;
        private Date orderDeadline;
        private boolean isDone;
        private boolean isActive;
        private String clientName;
        private String delivery;
        private List<String> processes;

        public OrderDTOBuilder(String product, int desiredQuantity) {
            init(product, desiredQuantity);
        }


        private void init(String product, int desiredQuantity) {
            this.product = product;
            this.desiredQuantity = desiredQuantity;
            this.processes = new LinkedList<>();
            this.quantityDone = 0;
            this.isDone = false;
        }

        public OrderDTOBuilder quantityDone(int quantityDone) {
            this.quantityDone = quantityDone;
            return this;
        }

        public OrderDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public OrderDTOBuilder orderDate(Date orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public OrderDTOBuilder orderDeadline(Date orderDeadline) {
            this.orderDeadline = orderDeadline;
            return this;
        }

        public OrderDTOBuilder isDone(boolean isDone) {
            this.isDone = isDone;
            return this;
        }

        public OrderDTOBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public OrderDTOBuilder clientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public OrderDTOBuilder delivery(String delivery) {
            this.delivery = delivery;
            return this;
        }

        public OrderDTOBuilder addProcess(String processName) {
            this.processes.add(processName);
            return this;
        }

        public OrderDTO build() {
            return new OrderDTO(this);
        }

    }


}
