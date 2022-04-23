package it.unipi.dstm;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface DataServersInterface {
    public List<EarthquakeDTO> collectServersData ();
}
