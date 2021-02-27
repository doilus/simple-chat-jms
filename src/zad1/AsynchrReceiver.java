package zad1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AsynchrReceiver extends JFrame implements MessageListener{
	private Connection con;
	private JTextArea ta = new JTextArea(10,20);
	private AsynchrReceiver(String destName) {
		try {
			Hashtable properties = new Hashtable();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
			properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
		
			
			Context context = new InitialContext(properties);
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			Destination dest = (Destination) context.lookup(destName);
			con = factory.createConnection();
			Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer receiver = session.createConsumer(dest);
			receiver.setMessageListener(this);
			con.start();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		add(new JScrollPane(ta));
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {con.close();} catch(Exception exc) {}
				dispose();
				System.exit(0);
			}
		});
		setTitle("Czekam");
		pack();
		setLocationRelativeTo(null);
		show();
	}
	
	int i=0;
	public void onMessage(Message msg) {
		setTitle("Received msg " + i++);
		try {
			 ta.append(((TextMessage) msg).getText() + "\n");
		}catch(JMSException exc) {System.err.println(exc);}
	}
	
	
	public static void main(String[] args) {
		new AsynchrReceiver("queue1");
	}
	
	
	
}
