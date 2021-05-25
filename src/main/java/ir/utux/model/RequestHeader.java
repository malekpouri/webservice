package ir.utux.model;

import lombok.Data;

@Data
public class RequestHeader {
    private Method method;
    private String requestURI;
    private String protocolVersion;
}
