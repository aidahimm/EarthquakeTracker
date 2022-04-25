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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "earthInfo",value="/earthInfo")
public class earthInfo extends HttpServlet {

    @EJB
    private EarthquakeInterface earthquakeInterface;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<EarthquakeDTO> earthquakeDTOList =null;


        String region= request.getParameter("region");
        java.util.Date startDate=null;
        java.util.Date endDate=null;
        try {
            if(request.getParameter("startDate")!=null)
            {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startDate"));
            }
            if(request.getParameter("endDate")!=null) {
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endDate"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



            // retrieve the data from all servers
            if ("all".equals(region)) {

                String id = "java:global/ejb_data_querying/DataServersRemoteEJB!" + DataServersInterface.class.getName();

                String host = "localhost";// if you run your client and server sample on same machine
                String port = "3700";//default
// to obtain port use asadmin get "configs.config.server-config.iiop-service.iiop-listener.orb-listener-1.*"
                Properties prop = new Properties();
                prop.put("org.omg.CORBA.ORBInitialHost", host);
                prop.put("org.omg.CORBA.ORBInitialPort", port);
                InitialContext context = null;
                DataServersInterface dataServersInterface = null;

                try {
                    context = new InitialContext(prop);
                    dataServersInterface = (DataServersInterface) context.lookup(id);
                } catch (NamingException e) {
                    e.printStackTrace();
                }
                try {
                    earthquakeDTOList = dataServersInterface.collectServersData(startDate
                    ,endDate);
                    request.setAttribute("earthquakesList", earthquakeDTOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else // retrieve the data from the current server.
            {
                try {
                    earthquakeDTOList = earthquakeInterface.listEarthquakes(startDate,endDate);
                    request.setAttribute("earthquakesList", earthquakeDTOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        //notification initialization
        try {
            initializeConnection();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
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

