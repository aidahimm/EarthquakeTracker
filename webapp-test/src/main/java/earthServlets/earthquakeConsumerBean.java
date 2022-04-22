package earthServlets;


import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Console;
import java.util.Properties;


public class earthquakeConsumerBean implements MessageListener {


    @Override
    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            try {
                String text = ((TextMessage) msg).getText();
                String outMsg = "Received: " + text;
                String bar = "++++++++++++++++++++++++++++++++++++++++++++";
                outMsg = "\n" +bar +"\n" + outMsg + "\n" + bar +"\n";

                Console con = System.console();
                con.printf(outMsg);

            } catch (final JMSException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void onException(JMSException exception)
    {
        System.err.println("an error occurred: " + exception);
    }

}