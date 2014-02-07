package com.aplana.integration.twitter;

import org.apache.activemq.ActiveMQConnectionFactory;
import twitter4j.*;
import twitter4j.conf.PropertyConfiguration;

import javax.jms.*;
import java.io.IOException;
import java.util.Properties;

/**
 * Start class
 * @author Vitaliy Samolovskikh
 */
public class Start {
	public static void main(String[] args) throws IOException, JMSException {
		// Create ActiveMQ connection
		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("activemq.properties"));
		ActiveMQConnectionFactory connectionFactory =
				new ActiveMQConnectionFactory(
						properties.getProperty("username"),
						properties.getProperty("password"),
						properties.getProperty("url")
				);
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination readStatus = session.createQueue("READ_STATUS");
		Destination writeStatus = session.createQueue("WRITE_STATUS");

		// Twitter listener
		TwitterListener twitterListener = new TwitterListener();
		twitterListener.setConnection(connection);
		twitterListener.setDestination(readStatus);

		TwitterStreamFactory streamFactory = new TwitterStreamFactory();
		TwitterStream twitterStream = streamFactory.getInstance();
		twitterStream.addListener(twitterListener);
		twitterStream.filter(new FilterQuery().track(new String[]{"JMS"}));

		// JMS listener
		Twitter twitter = TwitterFactory.getSingleton();
		JmsListener jmsListener = new JmsListener();
		jmsListener.setTwitter(twitter);
		MessageConsumer consumer = session.createConsumer(writeStatus);
		consumer.setMessageListener(jmsListener);

		// Start connection
		connection.start();

		// Pause
		System.in.read();

		twitterStream.shutdown();
		session.close();
		connection.stop();
		connection.close();
	}
}
