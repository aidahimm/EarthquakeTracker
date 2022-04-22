package it.unipi.dstm.ejb;

import it.unipi.dstm.EarthquakeDTO;
import it.unipi.dstm.EarthquakeInterface;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Stateless
public class EarthquakeRemoteEJB  implements EarthquakeInterface {


    @Resource(lookup="jdbc/Regional1Pool")
    private DataSource dataSource;

    @Override
    public List<EarthquakeDTO> listEarthquakes() {

        Connection connection=null;
        ResultSet rs=null;
        PreparedStatement pstm=null;
        List<EarthquakeDTO> earthquakeDTOS=new ArrayList<>();
        try {
            earthquakeDTOS.add(new EarthquakeDTO());
            connection=dataSource.getConnection();
            StringBuilder sqlStringBuilder=new StringBuilder();
            sqlStringBuilder.append("select " );
            sqlStringBuilder.append("  e.magnitude,  ");
            sqlStringBuilder.append("  e.latitude,  ");
            sqlStringBuilder.append("  e.longitude,  ");
            sqlStringBuilder.append("  e.depth,  ");
            sqlStringBuilder.append("  e.date  ");
            sqlStringBuilder.append(" from regional1.earthquakedata e ");
            pstm=connection.prepareStatement(sqlStringBuilder.toString());
            rs=pstm.executeQuery();
            while (rs.next())
            {
                EarthquakeDTO earthquakeDTO=new EarthquakeDTO();
                earthquakeDTO.setMagnitude(rs.getDouble(1));
                earthquakeDTO.setLatitude(rs.getDouble(2));
                earthquakeDTO.setLongitude(rs.getDouble(3));
                earthquakeDTO.setDepth(rs.getDouble(4));
//                earthquakeDTO.setD(rs.getDouble(4));
//                earthquakeDTO.setDate(new Date(rs.getTimestamp(5).getTime()));
                earthquakeDTOS.add(earthquakeDTO);

            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        try {
            assert rs != null;
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*EarthquakeDTO dto=new EarthquakeDTO();
        dto.setMagnitude(5.5);
        earthquakeDTOS.add(dto);*/

        return earthquakeDTOS;
    }
}
