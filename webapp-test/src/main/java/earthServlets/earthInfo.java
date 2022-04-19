package earthServlets;

import it.unipi.dstm.EarthquakeDTO;
import it.unipi.dstm.EarthquakeInterface;


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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id="java:global/earthquak_ejb/EarthquakeRemoteEJB!"+ EarthquakeInterface.class.getName();

        Properties props=new Properties();

        InitialContext ic= null;

        try {
            ic = new InitialContext(props);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        EarthquakeInterface earthquakeInterface= null;
        try {
            earthquakeInterface = (EarthquakeInterface) ic.lookup(id);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        List<EarthquakeDTO> earthquakeDTOList =null;
        try {
            earthquakeDTOList= earthquakeInterface.listEarthquakes();
            request.setAttribute("earthquakesList",earthquakeDTOList);
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
}
