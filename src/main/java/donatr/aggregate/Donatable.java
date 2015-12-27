package donatr.aggregate;

import donatr.event.*;
import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Donatable extends Aggregate {
	private String id;
	private String name;
	private String imageUrl;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal balance = BigDecimal.ZERO;
	private int timesDonated = 0;

	public void on(final DonatableCreatedEvent event) {
		id = event.getId();
		name = event.getName();
		imageUrl = event.getImageUrl();
		amount = event.getAmount();
	}

	public void on(final AccountCreditedEvent event) {
		balance = balance.add(event.getAmount());
		timesDonated = timesDonated + 1;
	}

	public void on(final AccountDebitedEvent event) {
		balance = balance.subtract(event.getAmount());
	}

	public void on(final DonatableAmountUpdatedEvent event) {
		amount = event.getAmount();
	}

	public void on(final DonatableImageUrlUpdatedEvent event) {
		imageUrl = event.getImageUrl();
	}

	public void on(final DonatableNameUpdatedEvent event) {
		name = event.getName();
	}
}
