package com.aplana.integration.twitter;

import twitter4j.Status;
import twitter4j.StatusAdapter;

import javax.jms.*;

/**
 * Listens to the Twitter and sends messages by JMS.
 *
 * @author Vitaliy Samolovskikh
 */
public class TwitterListener extends StatusAdapter {
	private Connection connection;
	private Destination destination;

	@Override
	public void onStatus(Status status) {
		String text = status.getText();
		System.out.println("Status updated: " + text);

		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(destination);
			producer.send(session.createTextMessage(text));
			producer.close();
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
