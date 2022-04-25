package it.unipi.dstm;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String region = "region1";
        RSDataCollection rs=new RSDataCollection("server@localhost",region,"servermailbox");
        try {
            rs.receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OtpErlangDecodeException e) {
            e.printStackTrace();
        } catch (OtpErlangExit e) {
            e.printStackTrace();
        }
    }
}
