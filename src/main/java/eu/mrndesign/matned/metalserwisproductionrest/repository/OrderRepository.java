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

    @Query("select o from Order o where o.orderDeadline > :deadline_date")
    Page<Order> findOrdersByOverDeadlineDate(@Param("deadline_date") Date deadlineDate, Pageable pageable);

    @Query("select o from Order o where o.isDone = true")
    Page<Order> findOrdersByDone(Pageable pageable);

    @Query("select o from Order o where o.isDone = false")
    Page<Order> findOrdersByNotDone(Pageable pageable);



}
