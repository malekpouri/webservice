package ir.utux.http;

import ir.utux.model.Method;
import ir.utux.model.RequestHeader;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
public class HttpRequestHandler {
    private RequestHeader requestHeader;
    private List<String> headers=new ArrayList<>();
    public HttpRequestHandler(InputStream inputStream) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        requestHeader=new RequestHeader();
        String protocolHeader;
        try {
            protocolHeader=reader.readLine();
//            Stream<String> lines=reader.lines();
//            lines.forEach(s -> addHeaders(s));
//             Object[] toArray=lines.toArray(value -> new String[value]);
            parsProtocolHeader(protocolHeader);
            while (!protocolHeader.equals("")){
                protocolHeader=reader.readLine();
                addHeaders(protocolHeader);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not Read Http Protocol Header. "+e.getMessage());
        }

    }

    private void parsProtocolHeader(String protocolHeader) {
        String[] s=protocolHeader.split("\\s+");
        try {
            requestHeader.setMethod(Method.valueOf(s[0]));
        }catch (Exception e){
            requestHeader.setMethod(Method.UNRECOGNIZED);
        }
        requestHeader.setRequestURI(s[1]);
        requestHeader.setProtocolVersion(s[2]);
    }
    public void addHeaders(String header){
        headers.add(header);
    }
}
