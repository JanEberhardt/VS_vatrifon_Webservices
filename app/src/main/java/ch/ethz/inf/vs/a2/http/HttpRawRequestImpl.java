package ch.ethz.inf.vs.a2.http;

/**
 * Created by jan on 14.10.15.
 */
public class HttpRawRequestImpl implements HttpRawRequest{

    private final String host, path;
    private final int port;


    public HttpRawRequestImpl(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    @Override
    public String generateRequest() {
        StringBuilder sb = new StringBuilder();
        sb.append("GET "+host+"/"+path+" HTTP/1.1\r\n");
        sb.append("Host: "+host+":"+port+"\r\n");
        sb.append("Accept: text/html\r\n");
        sb.append("Connection: close\r\n");
        sb.append("\r\n\r\n");
        return sb.toString();
    }

    @Override
    public String getHost() {
        return path;
    }

    @Override
    public int getPort() {
        return port;
    }
}
