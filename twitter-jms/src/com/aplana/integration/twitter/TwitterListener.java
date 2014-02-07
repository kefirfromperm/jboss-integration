package com.aplana.integration.twitter;

import twitter4j.Status;
import twitter4j.TwitterAdapter;

import javax.jms.*;

/**
 * Listens to the Twitter and sends messages by JMS.
 * @author Vitaliy Samolovskikh
 */
public class TwitterListener extends TwitterAdapter {
	private Connection connection;
	private Destination destination;

	@Override
	public void updatedStatus(Status status) {
		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(destination);

			producer.send(session.createTextMessage(status.getText()));

			session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
