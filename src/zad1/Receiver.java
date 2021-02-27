package zad1;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver {

	public static void main(String[] args) {
		Context context = null;
		ConnectionFactory factory = null;
		Connection connection = null;
		String factoryName = "ConnectionFactory";
		String admDestName = null;
		Destination dest = null;
		int c = 1;
		Session session = null;
		MessageConsumer receiver = null;
		
		if(args.length < 1 || args.length > 2) {
			System.out.println("usage: Receiver <destination> [count]");
			System.exit(1);
		}
		
		admDestName = args[0];
		if(args.length == 2) {
			c = Integer.parseInt(args[1]);
		}
		
		try {
			//create the jndi InitialContext
			context = new InitialContext();
			
			factory = (ConnectionFactory) context.lookup(factoryName);
			dest = (Destination) context.lookup(admDestName);
			connection = factory.createConnection();
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			receiver = session.createConsumer(dest);
			
			connection.start();
			
			for(int i = 0; i <c;i++) {
				Message message = receiver.receive();
				if(message instanceof TextMessage) {
					TextMessage text = (TextMessage) message;
					System.out.println("Received: " + text.getText());
				}
			}	
		}catch (JMSException | NamingException e) {
			System.err.println(e); 
		} finally {
			if(context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					System.err.println(e);
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
