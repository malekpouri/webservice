package ir.utux.Http;

import ir.utux.http.HttpRequestHandler;
import ir.utux.model.Method;
import ir.utux.model.RequestHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class HttpRequestHandlerTest {

    @Test
    void whenPassInputStreamReturnRequest(){
//        HttpRequestHandler request=new HttpRequestHandler(new ByteArrayInputStream("GET / HTTP/1.1\n\n".getBytes()));
        HttpRequestHandler request=new HttpRequestHandler(new ByteArrayInputStream("""
                GET http://localhost:8080/tienda1/imagenes/3.gif/ HTTP/1.1
                User-Agent: Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.8 (like Gecko)
                Pragma: no-cache
                Cache-control: no-cache
                Accept: text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5
                Accept-Encoding: x-gzip, x-deflate, gzip, deflate
                Accept-Charset: utf-8, utf-8;q=0.5, *;q=0.5
                Accept-Language: en
                """.getBytes()));
        RequestHeader header=request.getRequestHeader();
        System.out.println(header);
        List<String> headers=request.getHeaders();
        headers.forEach(System.out::println);
        Assertions.assertEquals(Method.GET,header.getMethod());
    }
    @Test
    void whenPassFiledParameterReturnUnknownHeader() throws MalformedURLException {
        HttpRequestHandler request=new HttpRequestHandler(new ByteArrayInputStream(" GET / HTTP/1.1\n\n".getBytes()));
        RequestHeader header=request.getRequestHeader();
        Assertions.assertEquals(Method.UNRECOGNIZED,header.getMethod());
        String aURIString = "http://somehost:80/path?thequery";
        try {
            URI uri = new URI("/");
            URL url = new URL(aURIString);
            System.out.println(url);
            System.out.println(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
