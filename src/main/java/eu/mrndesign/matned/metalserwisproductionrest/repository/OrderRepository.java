package eu.mrndesign.matned.metalserwisproductionrest.repository;

import eu.mrndesign.matned.metalserwisproductionrest.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join o.client c where lower(c.clientName) = lower(:client_name)")
    Page<Order> findOrdersByClientName(@Param("client_name") String clientName, Pageable pageable);

    @Query("select o from Order o where o.orderDate = :order_date")
    Page<Order> findOrdersByOrderDate(@Param("order_date") Date orderDate, Pageable pageable);

    @Query("select o from Order o where o.orderDeadline = :deadline_date")
    Page<Order> findOrdersByDeadlineDate(@Param("deadline_date") Date deadlineDate, Pageable pageable);

    @Query("select o from Order o where o.orderDeadline < CURRENT_DATE")
    Page<Order> findOrdersByOverDeadlineDate(Pageable pageable);

    @Query("select o from Order o where o.isDone = true")
    Page<Order> findOrdersByDone(Pageable pageable);

    @Query("select o from Order o where o.isDone = false")
    Page<Order> findOrdersByNotDone(Pageable pageable);

    @Query("select o from Order o join o.delivery d where d.deliveryCode = :delivery_code")
    Page<Order> findOrdersByDeliveryCode(@Param("delivery_code") String deliveryCode, Pageable pageable);

    @Query("select o from Order o where o.orderDeadline >= :deadline_date_start and o.orderDeadline <= :deadline_date_end")
    Page<Order> findByDeadlineDateBetweenDates(@Param("deadline_date_start") Date deadlineStartDate, @Param("deadline_date_end")Date deadlineEndDate, Pageable pageable);

    @Query("select o from Order o where o.orderDate >= :order_date_start and o.orderDate <= :order_date_end")
    Page<Order> findByOrderDateBetweenDates(@Param("order_date_start") Date orderDateStart, @Param("order_date_start") Date orderDateEnd, Pageable pageable);
}
