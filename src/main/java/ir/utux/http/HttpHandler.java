package ir.utux.http;

import ir.utux.model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class HttpHandler implements Runnable {
    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass().getName());

    private Socket socket;
    public HttpHandler(Socket socket) {
            this.socket=socket;
    }

    @Override
    public void run() {
        try  {
            HttpRequestHandler httpRequestHandler=new HttpRequestHandler(socket.getInputStream());
            ProcessHttpResponse response=new ProcessHttpResponse(httpRequestHandler.getRequestHeader());
            response.writeResponse(socket.getOutputStream());
            socket.close();

        } catch (IOException e) {
            LOGGER.error("Error in Running.");
            e.printStackTrace();
        }
    }
}
