package ch.ethz.inf.vs.a2.http;

/**
 * Created by jan on 14.10.15.
 *
 * generates a raw http request with either json or html.
 *
 */
public class HttpRawRequestImpl implements HttpRawRequest{

    private final String host, path;
    private final int port;
    private final boolean json;


    public HttpRawRequestImpl(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.json = false;
    }

    public HttpRawRequestImpl(String host, int port, String path, boolean json) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.json = json;
    }

    @Override
    public String generateRequest() {
        StringBuilder sb = new StringBuilder();
        sb.append("GET "+path+" HTTP/1.1\r\n");
        sb.append("Host: "+host+":"+port+"\r\n");
        if(json)
            sb.append("Accept: application/json\r\n");
        else
            sb.append("Accept: text/html\r\n");
        sb.append("Connection: close\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
