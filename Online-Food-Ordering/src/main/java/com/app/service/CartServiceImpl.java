package com.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.dao.CartDao;
import com.app.dao.CartItemDao;
import com.app.entities.Cart;
import com.app.entities.CartItem;
import com.app.entities.Food;
import com.app.entities.User;
import com.app.request.AddCartItemRequest;

public class CartServiceImpl implements CartService {

	@Autowired
	private CartDao cartDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CartItemDao cartItemDao;
	@Autowired
	private FoodService foodService;
	
	@Override
	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Food food=foodService.findFoodById(req.getFoodId());
		Cart cart=cartDao.findByCustomerId(user.getId());
		
		for(CartItem cartItem:cart.getItem()) {
			if(cartItem.getFood().equals(food)) {
				int newQuantity=cartItem.getQuantity()+req.getQuantity();
				return updateCartItemQuantity(cartItem.getId(), newQuantity);
			}
		}
		
		CartItem newCartItem=new CartItem();
		newCartItem.setFood(food);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(req.getQuantity());
		newCartItem.setIngredients(req.getIngredients());
		newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());
		
		CartItem savedCartItem=cartItemDao.save(newCartItem);
		cart.getItem().add(savedCartItem);
		
		return savedCartItem;
	}

	@Override
	public CartItem updateCartItemQuantity(Long cartItem, int quantity) throws Exception {
		Optional<CartItem> cartItemOptional=cartItemDao.findById(cartItem);
		if(cartItemOptional.isEmpty()) {
			throw new Exception("Cart Item Not Found!");
		}
		CartItem item=cartItemOptional.get();
		item.setQuantity(quantity);
		item.setTotalPrice(item.getFood().getPrice()*quantity);
		
		return cartItemDao.save(item);
	}

	@Override
	public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
		User user=userService.findUserByJwtToken(jwt);
		Cart cart=cartDao.findByCustomerId(user.getId());
		
		Optional<CartItem> cartItemOptional=cartItemDao.findById(cartItemId);
		if(cartItemOptional.isEmpty()) {
			throw new Exception("Cart Item Not Found!");
		}
		
		CartItem item=cartItemOptional.get();
		cart.getItem().remove(item);
		
		return cartDao.save(cart);
	}

	@Override
	public Long calculateCartTotals(Cart cart) throws Exception {
		Long total=0L;
		for(CartItem cartItem:cart.getItem()) {
			total += cartItem.getFood().getPrice() * cartItem.getQuantity();
		}

		return total;
	}

	@Override
	public Cart findCartById(Long id) throws Exception {
		Optional<Cart> optionalCart=cartDao.findById(id);
		if(optionalCart.isEmpty()) {
			throw new Exception("Cart Not found with Id "+id);
		}
		
		return optionalCart.get();
	}

	@Override
	public Cart findCartByUserId(Long userId) throws Exception {
//		User user=userService.findUserByJwtToken(jwt);
		Cart cart=cartDao.findByCustomerId(userId);
		cart.setTotal(calculateCartTotals(cart));
		
		return cart;
	}

	@Override
	public Cart clearCart(Long userId) throws Exception {
//		User user=userService.findUserByJwtToken(jwt);
		Cart cart=findCartByUserId(userId);
		cart.getItem().clear();
		
		return cartDao.save(cart);
	}

}
