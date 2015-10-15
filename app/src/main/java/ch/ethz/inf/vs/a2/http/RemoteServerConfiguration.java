package ch.ethz.inf.vs.a2.http;


/**
 * Collection of constant definitions for the remote server.
 * 
 * @author Leyna Sadamori
 *
 */
public interface RemoteServerConfiguration {
	public static final String HOST = "vslab.inf.ethz.ch";
	public static final int REST_PORT = 8081;
	public static final int SOAP_PORT = 8080;

    // added by jan
	public static final String SPOT_1_TEMP_URL = "/sunspots/Spot1/sensors/temperature";
	public static final String SPOT_3_URL = "/SunSPOTWebServices/SunSPOTWebservice";
}
