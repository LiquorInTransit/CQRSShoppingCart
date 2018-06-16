package com.gazorpazorp.common.cart;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.HashMap;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.gazorpazorp.common.cart.command.AddItemToCartCommand;
import com.gazorpazorp.common.cart.command.ClearCartCommand;
import com.gazorpazorp.common.cart.command.CreateCartCommand;
import com.gazorpazorp.common.cart.command.RemoveItemFromCartCommand;
import com.gazorpazorp.common.cart.event.CartClearedEvent;
import com.gazorpazorp.common.cart.event.ItemAddedToCartEvent;
import com.gazorpazorp.common.cart.event.ItemRemovedFromCartEvent;
import com.gazorpazorp.common.cart.event.ShoppingCartInitiatedEvent;
import com.gazorpazorp.common.exception.InsufficientInventoryException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Aggregate
@Getter
@NoArgsConstructor
public class CartAggregate {

	@AggregateIdentifier
	private String id;
	
	private Map<String, Integer> items;	
	
	/*-------------CQRS------------*/
	//Initialization
	@CommandHandler
	public CartAggregate (CreateCartCommand command) {
		apply(new ShoppingCartInitiatedEvent(command.getTargetId(), command.getAuditEntry()));
	}	
	@EventSourcingHandler
	public void on (ShoppingCartInitiatedEvent event) {
		this.id = event.getAggregateIdentifier();
		items = new HashMap<>();
	}
	
	//Add, Remove, Clear
	/**
	 * Add
	 * @param command
	 * @throws Exception
	 */
	@CommandHandler
	public void addItem (AddItemToCartCommand command) {
		apply (new ItemAddedToCartEvent(command.getTargetId(), command.getProductId(), command.getQty(), command.getAuditEntry()));
	}
	@EventSourcingHandler
	public void on (ItemAddedToCartEvent event) throws InsufficientInventoryException {
		Integer itemQty = items.get(event.getProductId());
		if (itemQty == null) {
			items.put(event.getProductId(), event.getQty());
		} else {
			itemQty += event.getQty();
			items.replace(event.getProductId(), itemQty);
		}
	}
	
	/**
	 * Remove
	 * @param command
	 * @throws Exception
	 */
	@CommandHandler
	public void removeItem (RemoveItemFromCartCommand command) {
		apply (new ItemRemovedFromCartEvent(command.getTargetId(), command.getProductId(), command.getQty(), command.getAuditEntry()));
	}
	@EventSourcingHandler
	public void on (ItemRemovedFromCartEvent event) {
		Integer itemQty = items.get(event.getProductId());
		if (itemQty != null) {
			itemQty -= event.getQty();
			if (itemQty < 1)
				items.remove(event.getProductId());
			else
				items.replace(event.getProductId(), itemQty);
		}
	}
	
	/**
	 * Clear Cart
	 * @param command
	 * @throws Exception
	 */
	@CommandHandler
	public void addItem (ClearCartCommand command) {
		apply (new CartClearedEvent(command.getTargetId(), command.getAuditEntry()));
	}
	@EventSourcingHandler
	public void on (CartClearedEvent event) {
		items.clear();
	}
}
