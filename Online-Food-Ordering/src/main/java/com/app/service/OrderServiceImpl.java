package com.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.AddressDao;
import com.app.dao.OrderDao;
import com.app.dao.OrderItemDao;
import com.app.dao.RestaurantDao;
import com.app.dao.UserDao;
import com.app.entities.Address;
import com.app.entities.Cart;
import com.app.entities.CartItem;
import com.app.entities.Order;
import com.app.entities.OrderItem;
import com.app.entities.Restaurant;
import com.app.entities.User;
import com.app.request.OrderRequest;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private CartService cartService;
	
	@Override
	public Order createOrder(OrderRequest order, User user) throws Exception {
		Address shipAddress=order.getDeliveryAddress();
		Address savedAddress=addressDao.save(shipAddress);
		
		if(!user.getAddresses().contains(savedAddress)) {
			user.getAddresses().add(savedAddress);
			userDao.save(user);
		}
		
		Restaurant restaurant=restaurantService.findRestaurantById(order.getRestaurantId());
		
		Order createdOrder=new Order();
		createdOrder.setCustomer(user);
		createdOrder.setCreatedAt(new Date());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.setDeliveryAddress(savedAddress);
		createdOrder.setRestaurant(restaurant);
		
		Cart cart=cartService.findCartByUserId(null);
		
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem cartItem:cart.getItem()) {
			OrderItem orderItem=new OrderItem();
			orderItem.setFood(cartItem.getFood());
			orderItem.setIngredients(cartItem.getIngredients());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());
			
			OrderItem savedOrderItem=orderItemDao.save(orderItem);
			orderItems.add(savedOrderItem);
		}
		
		Long totalPrice=cartService.calculateCartTotals(cart);
		
		createdOrder.setItems(orderItems);
		createdOrder.setTotalPrice(totalPrice);
		
		Order savedOrder=orderDao.save(createdOrder);
		restaurant.getOrders().add(savedOrder);
		
		return createdOrder;
	}

	@Override
	public Order updateOrder(Long orderId, String orderStatus) throws Exception {
		Order order=findOrderById(orderId);
		if(orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED") || orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING")) {
			order.setOrderStatus(orderStatus);
			return orderDao.save(order);
		}
		
		throw new Exception("Please select a valid order status!!");
	}

	@Override
	public void cancelOrder(Long orderId) throws Exception {
		Order order=findOrderById(orderId);
		orderDao.deleteById(orderId);

	}

	@Override
	public List<Order> getUsersOrder(Long userId) throws Exception {
		return orderDao.findByCustomerId(userId);
	}

	@Override
	public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception {
		List<Order> orders=orderDao.findByRestaurantId(restaurantId);
		if(orderStatus != null) {
			orders = orders.stream()
					.filter(order->order.getOrderStatus().equals(orderStatus))
					.collect(Collectors.toList());
		}
		
		return orders;
	}

	@Override
	public Order findOrderById(Long orderId) throws Exception {
		Optional<Order> optionalOrder=orderDao.findById(orderId);
		if(optionalOrder.isEmpty()) {
			throw new Exception("Order Not Found!!");
		}

		return optionalOrder.get();
	}

}
