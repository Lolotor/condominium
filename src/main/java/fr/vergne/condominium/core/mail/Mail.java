package fr.vergne.condominium.core.mail;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Mail {

	Headers headers();

	String subject();

	Address sender();

	Stream<Address> receivers();

	ZonedDateTime receivedDate();

	// Here for debug only, to be removed
	List<String> lines();// TODO Remove

	public interface Address {
		Optional<String> name();

		String email();
		
		public static Address createWithCanonEmail(Optional<String> name, String email) {
			String canonEmail = email.toLowerCase();
			return new Mail.Address() {

				@Override
				public Optional<String> name() {
					return name;
				}

				@Override
				public String email() {
					return canonEmail;
				}
			};
		}
	}

	static class Base implements Mail {
		private final String id;
		private final List<String> lines;
		private final Headers headers;
		private final String body;
		private final Supplier<ZonedDateTime> receivedDateSupplier;
		private final Supplier<Address> senderSupplier;
		private final Supplier<Stream<Address>> receiversSupplier;

		public Base(String id, List<String> lines, Headers headers, String body,
				Supplier<ZonedDateTime> receivedDateSupplier, Supplier<Address> sender2Supplier,
				Supplier<Stream<Address>> receiversSupplier) {
			this.id = id;
			this.receivedDateSupplier = receivedDateSupplier;
			this.headers = headers;
			this.body = body;
			this.senderSupplier = sender2Supplier;
			this.receiversSupplier = receiversSupplier;
			this.lines = Collections.unmodifiableList(lines);
		}

		@Override
		public List<String> lines() {
			return lines;
		}

		@Override
		public Headers headers() {
			return headers;
		}

		@Override
		public String subject() {
			return headers.get("subject").body();
		}

		@Override
		public Address sender() {
			return senderSupplier.get();
		}

		@Override
		public Stream<Address> receivers() {
			return receiversSupplier.get();
		}

		@Override
		public ZonedDateTime receivedDate() {
			return receivedDateSupplier.get();
		}

		@Override
		public String toString() {
			return id + " at " + receivedDateSupplier;
		}
	}
}