package it.unipi.dstm;



import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NamingException {


        String id="java:global/earthquak_ejb/EarthquakeRemoteEJB!"+EarthquakeInterface.class.getName();
        //id="java:global/earthquak_ejb/EarthquakeRemoteEJB!it.unipi.dstm.EarthquakeInterface";
        Properties props=new Properties();
        InitialContext ic=new InitialContext(props);
        EarthquakeInterface earthquakeInterface= (EarthquakeInterface) ic.lookup(id);
        List<EarthquakeDTO> list=earthquakeInterface.listEarthquakes();

        for(EarthquakeDTO dto:list)
        {
            System.out.println(dto.getMagnitude());
        }

    }
}
