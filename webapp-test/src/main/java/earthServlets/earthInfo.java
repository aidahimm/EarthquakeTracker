package earthServlets;

import it.unipi.dstm.DataServersInterface;
import it.unipi.dstm.EarthquakeDTO;
import it.unipi.dstm.EarthquakeInterface;


import javax.ejb.EJB;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "earthInfo",value="/earthInfo")
public class earthInfo extends HttpServlet {

    @EJB
    private EarthquakeInterface earthquakeInterface;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            initializeConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        List<EarthquakeDTO> earthquakeDTOList =null;
        List<EarthquakeDTO> earthquakeAll =null;
        String id="java:global/ejb_data_querying/DataServersRemoteEJB!"+ DataServersInterface.class.getName();

        try {
            earthquakeDTOList= earthquakeInterface.listEarthquakes();
            request.setAttribute("earthquakesList",earthquakeDTOList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String host="localhost";// if you run your client and server sample on same machine
        String port ="3700";//default
// to obtain port use asadmin get "configs.config.server-config.iiop-service.iiop-listener.orb-listener-1.*"
        Properties prop = new Properties();
        prop.put("org.omg.CORBA.ORBInitialHost",host);
        prop.put("org.omg.CORBA.ORBInitialPort",port);
        InitialContext context = null;
        DataServersInterface dataServersInterface  = null;

        try {
            context = new InitialContext(prop);
            dataServersInterface = (DataServersInterface) context.lookup(id);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        try {
            earthquakeDTOList= earthquakeInterface.listEarthquakes();
            request.setAttribute("earthquakesList",earthquakeDTOList);
            System.out.println(earthquakeDTOList.get(4).getDate());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (dataServersInterface!=null) {
            System.out.println("Data server Interface NOT NULL");
        }else{
            System.out.println("Data server Interface IS NULL");
        }



        try {
            earthquakeAll = dataServersInterface.collectServersData();
            request.setAttribute("earthquakesAll",earthquakeAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String resourceURL="/pages/showdata.jsp";
        RequestDispatcher requestDispatcher=request.getRequestDispatcher(resourceURL);


        requestDispatcher.forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }


    public void initializeConnection() throws NamingException, JMSException {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:8080");
        env.put("queue.queueSampleQueue","myQueue2");

        try{
            Context ic = new InitialContext();
            ConnectionFactory qcf = (ConnectionFactory)ic.lookup("jms/__defaultConnectionFactory");
            Queue queue = (Queue)ic.lookup("jmsmyQueue2");
            javax.jms.Connection qc = qcf.createConnection();
            Session qs = qc.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer qprod = qs.createConsumer(queue);
            earthquakeConsumerBean asyncReceiver = new earthquakeConsumerBean();
            qprod.setMessageListener(asyncReceiver);
            qc.start();
//            Message msg = qprod.receive();
//            ObjectMessage obj = ((ObjectMessage) msg);
//            String message = obj.toString();
        }
        catch(NamingException | JMSException e){
            System.err.println("OUTCH! PUBLISHING PROBLEMS!");
            System.err.println(e.getMessage());

        }

    }
}

