package com.gazorpazorp.common.cart.command;

import java.util.UUID;

import com.gazorpazorp.common.AuditEntry;
import com.gazorpazorp.common.AuditableAbstractCommand;

import lombok.Value;

@Value
public class CreateCartCommand extends AuditableAbstractCommand {

	public CreateCartCommand(AuditEntry auditEntry) {
		super(UUID.randomUUID().toString(), auditEntry);
	}
}
