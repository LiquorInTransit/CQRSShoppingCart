package com.gazorpazorp.web.controller;

import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractCommand;
import com.gazorpazorp.common.cart.CartAggregate;
import com.gazorpazorp.common.cart.command.AddItemToCartCommand;
import com.gazorpazorp.common.cart.command.ClearCartCommand;
import com.gazorpazorp.common.cart.command.CreateCartCommand;
import com.gazorpazorp.common.cart.command.RemoveItemFromCartCommand;
import com.gazorpazorp.web.request.ShoppingCartRequest;

@RestController
@RequestMapping(value = "/api/command")
public class ShoppingCartController {

	private CommandGateway commandGateway;
	
	private String getCurrentUser() {
//		if (SecurityContextHolder.getContext().getAuthentication() != null) {
//			return SecurityContextHolder.getContext().getAuthentication().getName();
//		}
//		return null;
		return "user";
	}
	
	private AuditEntry getAuditEntry() {
		return new AuditEntry(getCurrentUser());
	}
	
	@Autowired
	public ShoppingCartController (CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}
	
	@PostMapping("/cart")
	@ResponseStatus(value = HttpStatus.OK)
	public String createCart () {
		CreateCartCommand command = new CreateCartCommand(getAuditEntry());
		return commandGateway.sendAndWait(command);
		//return command.getTargetId();
	}
	
	@PostMapping("/cart/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public void updateCart (@RequestBody ShoppingCartRequest request, @PathVariable String id) {
		AuditableAbstractCommand command;
		switch (request.getType()) {
		case ADD:
			command = new AddItemToCartCommand(id, request.getProductId(), request.getQty(), getAuditEntry());
			break;
		case REMOVE:
			command = new RemoveItemFromCartCommand(id, request.getProductId(), request.getQty(), getAuditEntry());
			break;
		case CLEAR:
			command = new ClearCartCommand(id, getAuditEntry());
			break;
		default:
			throw new IllegalArgumentException("You must set a request type");
				
		}
		commandGateway.sendAndWait(command);
	}
}
