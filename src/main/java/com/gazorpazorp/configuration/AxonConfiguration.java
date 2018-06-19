package com.gazorpazorp.configuration;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gazorpazorp.common.cart.CartAggregate;
import com.gazorpazorp.common.cart.event.CartClearedEvent;

@Configuration
public class AxonConfiguration {

	@Bean
	CommandBus commandBus (TransactionManager transactionManager) {
		SimpleCommandBus commandBus = new SimpleCommandBus(transactionManager, NoOpMessageMonitor.INSTANCE);
		commandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
		return commandBus;
	}
	

//	//Snapshot config
	@Bean
	public AggregateFactory<CartAggregate> cartAggregateAggregateFactory() {
		return new GenericAggregateFactory<CartAggregate>(CartAggregate.class);
	}
	@Bean(name = "cartAggregateRepository")
    public Repository<CartAggregate> cartAggregateRepository(AggregateFactory<CartAggregate> cartAggregateAggregateFactory,
                                                          EventStore eventStore,
                                                          SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return new EventSourcingRepository<>(cartAggregateAggregateFactory,
                                                    eventStore,
                                                    snapshotTriggerDefinition);
    }	
	@Bean
	public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter){
		return new EventTypeSnapshotTriggerDefinition(snapshotter, CartClearedEvent.class);
	}	
	@Bean
	public SpringAggregateSnapshotterFactoryBean snapshotterFactoryBean() {
		return new SpringAggregateSnapshotterFactoryBean();
	}
//	
//	@Bean//("CartAggregateRepository")
//	public EventSourcingRepository<CartAggregate> cartRepository(EventStore eventStore, Snapshotter snapshotter) {
//		return new EventSourcingRepository<>(
//				CartAggregate.class,
//				eventStore,
//				new EventCountSnapshotTriggerDefinition(snapshotter, 10)
//		);
//	}
	
}
