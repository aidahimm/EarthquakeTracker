package it.unipi.dstm;


import javax.ejb.Remote;
import java.util.List;
@Remote
public interface EarthquakeInterface {
    public List<EarthquakeDTO> listEarthquakes(java.util.Date startDate,java.util.Date endDate);
}
