package com.gazorpazorp.query.handler;

import java.util.Map;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.SequenceNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gazorpazorp.common.cart.event.CartClearedEvent;
import com.gazorpazorp.common.cart.event.ItemAddedToCartEvent;
import com.gazorpazorp.common.cart.event.ItemRemovedFromCartEvent;
import com.gazorpazorp.common.cart.event.ShoppingCartInitiatedEvent;
import com.gazorpazorp.query.model.ShoppingCartEntity;
import com.gazorpazorp.query.repository.ShoppingCartRepository;


@ProcessingGroup("default")
@Component
public class ShoppingCartEventHandler {

	private ShoppingCartRepository cartRepo;
	
	@Autowired
	public ShoppingCartEventHandler (ShoppingCartRepository cartRepo) {
		this.cartRepo = cartRepo;
	}
	
	@EventHandler
	public void handle (ShoppingCartInitiatedEvent event, @SequenceNumber Long aggregateVersion) {
		cartRepo.save(new ShoppingCartEntity(event.getAggregateIdentifier(), aggregateVersion));
	}
	
	@EventHandler
	public void handle (ItemAddedToCartEvent event, @SequenceNumber Long aggregateVersion) {
		ShoppingCartEntity cart = cartRepo.getOne(event.getAggregateIdentifier());
		cart.setAggregateVersion(aggregateVersion);
		Map<String, Integer> items = cart.getItems();
		Integer itemQty = items.get(event.getProductId());
		if (itemQty == null) {
			items.put(event.getProductId(), event.getQty());
		} else {
			itemQty += event.getQty();
			items.replace(event.getProductId(), itemQty);
		}
		cart.setItems(items);
		cartRepo.save(cart);
	}
	
	@EventHandler
	public void handle (ItemRemovedFromCartEvent event, @SequenceNumber Long aggregateVersion) {
		ShoppingCartEntity cart = cartRepo.getOne(event.getAggregateIdentifier());
		cart.setAggregateVersion(aggregateVersion);
		Map<String, Integer> items = cart.getItems();
		Integer itemQty = items.get(event.getProductId());
		if (itemQty != null) {
			itemQty -= event.getQty();
			if (itemQty < 1)
				items.remove(event.getProductId());
			else
				items.replace(event.getProductId(), itemQty);
		}
		cart.setItems(items);
		cartRepo.save(cart);
	}
	
	@EventHandler
	public void handle (CartClearedEvent event, @SequenceNumber Long aggregateVersion) {
		ShoppingCartEntity cart = cartRepo.getOne(event.getAggregateIdentifier());
		cart.setAggregateVersion(aggregateVersion);
		Map<String, Integer> items = cart.getItems();
		items.clear();
		cart.setItems(items);
		cartRepo.save(cart);
	}
	
//	@EventHandler
//	public void handle (ShoppingCartUpdatedEvent event, @SequenceNumber Long aggregateVersion) {
//		ShoppingCartEntity cart = cartRepo.findById(event.getAggregateIdentifier()).get();
//		cart.setAggregateVersion(aggregateVersion);
//		cart.setNum(event.getNum());
//		cartRepo.save(cart);
//	}
}
