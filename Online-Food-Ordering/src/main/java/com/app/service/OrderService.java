package com.app.service;

import java.util.List;

import com.app.entities.Order;
import com.app.entities.User;
import com.app.request.OrderRequest;

public interface OrderService {

	public Order createOrder(OrderRequest order,User user) throws Exception;
	public Order updateOrder(Long orderId,String orderStatus) throws Exception;
	public void cancelOrder(Long orderId) throws Exception;
	public List<Order> getUsersOrder(Long userId) throws Exception;
	public List<Order> getRestaurantsOrder(Long restaurantId,String orderStatus) throws Exception;
	public Order findOrderById(Long orderId) throws Exception;
	
}
