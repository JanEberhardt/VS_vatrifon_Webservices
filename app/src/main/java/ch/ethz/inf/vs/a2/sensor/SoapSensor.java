package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by jan on 16.10.15.
 *
 * soap request using ksoap2
 */
public class SoapSensor extends XmlSensor {

    @Override
    protected void setHttpClient() {}

    @Override
    public void getTemperature() throws NullPointerException {
        AsyncWorker w = new AsyncWorker(){
            // todo: I get a HTTP server error 500 back?
            // this is a internal server error, th error message says it's a null pointer dereference...

            // todo: not sure about any of those!
            // filled in the ones that made the most sense from the wsdl file!
            private final String NAMESPACE = "http://webservices.vslecture.vs.inf.ethz.ch/";
            private final String URL = "http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice";
            private final String METHOD_NAME = "getSpot";
            private final String SOAP_ACTION = "http://webservices.vslecture.vs.inf.ethz.ch/SunSPOTWebservice/getSpotRequest";

                @Override
                protected String doInBackground(Object... params) {
                    // Create request
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    // Property which holds input parameters
                    PropertyInfo pi = new PropertyInfo();
                    // Set Name
                    pi.setName("id");
                    // Set Value
                    pi.setValue("Spot3");
                    // Set dataType
                    pi.setType(String.class);
                    // Add the property to request object
                    request.addProperty(pi);
                    // Create envelope
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setAddAdornments(false);
                    envelope.implicitTypes = true;
                    // Set output SOAP object
                    envelope.setOutputSoapObject(request);
                    // Create HTTP call object
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    try {
                        // Invoke web service
                        androidHttpTransport.call(SOAP_ACTION, envelope);
                        Log.d("###", "request: "+androidHttpTransport.requestDump);
                        Log.d("###", "response: "+androidHttpTransport.responseDump);
                        // Get the response
                        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                        // return the string
                        return response.toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                        return "error";
                    }
                }
            };
        w.execute();
    }
}
