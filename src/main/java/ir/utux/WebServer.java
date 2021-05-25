package ir.utux;

import ir.utux.config.WebConfiguration;
import ir.utux.http.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass().getName());
    public final int NUMBER_THREAD =5;
    public static void main(String[] args) {
        var webServer=new WebServer();
        webServer.start();

    }

    private void start(){
        ExecutorService service=Executors.newFixedThreadPool(NUMBER_THREAD);
        WebConfiguration webConfiguration=new WebConfiguration();
        LOGGER.info("Web server listening on port " + webConfiguration.getPort() );
        try {
            ServerSocket socket=new ServerSocket(webConfiguration.getPort());
            while(true){
                service.submit(new HttpHandler(socket.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can not open Port %s",webConfiguration.getPort()),e.getCause());
        }

    }
}
