package zad1;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Sender {

	public static void main(String[] args) {
		Context context = null;
		ConnectionFactory factory = null;
		Connection connection = null;
		String factoryName = "ConnectionFactory";
		String admDestName = null;
		Destination dest = null;
		int c =1;
		Session session = null;
		MessageProducer sender = null;
		String text = "Message";
		
		if(args.length < 1 || args.length > 2) {
			System.out.println("usage: Sender <destination> [count]");
			System.exit(1);
		}
		
		admDestName = args[0];
		if(args.length == 2) {
			c = Integer.parseInt(args[1]);
		}
		
		try {
			//creating a jndi InitialContext
			context = new InitialContext();
			
			//looking up a connectionfactory
			factory = (ConnectionFactory) context.lookup(factoryName);
			
			//creating a connection
			connection = factory.createConnection();
			
			//creating a session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//creating destination
			dest = (Destination) context.lookup(admDestName);
			
			//create the sender
			sender = session.createProducer(dest);
			
			connection.start();
			
			for(int i =0; i< c; i++) {
				TextMessage message = session.createTextMessage();
				message.setText(text + (i+1));
				sender.send(message);
				System.out.println("Sender sent message: " + message.getText());
			}
			
			//
		}catch(JMSException | NamingException e) {
			System.out.println(e);
		} finally {
			//close context
			if(context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					System.err.println(e);
				}
			}
		}
		
		
		

	}

}
