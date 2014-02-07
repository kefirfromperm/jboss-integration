package com.aplana.integration.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Listen JMS messages and sends to Twitter.
 *
 * @author Vitaliy Samolovskikh
 */
public class JmsListener implements MessageListener {
	private Twitter twitter;

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				String text = ((TextMessage) message).getText();
				System.out.println("Received message: " + text);
				twitter.updateStatus(text);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}
}
