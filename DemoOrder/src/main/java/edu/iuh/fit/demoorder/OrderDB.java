package edu.iuh.fit.demoorder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDB {
    private List<Order> orders =  new ArrayList<Order>();

    public void addOrder(Order order){
        order.setOrderId(String.valueOf(orders.size() + 1));
        order.setOrderDate(LocalDateTime.now());
        orders.add(order);
    }

    public Order getOrder(String orderId){
        for(Order order : orders){
            if(order.getOrderId().equals(orderId)){
                return order;
            }
        }
        return null;
    }

    public List<Order> getOrders() throws InterruptedException {
        return orders;
    }
}
