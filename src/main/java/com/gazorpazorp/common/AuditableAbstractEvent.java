package com.gazorpazorp.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AuditableAbstractEvent implements Serializable {
	
	private String aggregateIdentifier;
	private AuditEntry auditEntry;
	
}
