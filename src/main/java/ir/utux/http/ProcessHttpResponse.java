package ir.utux.http;

import ir.utux.config.WebConfiguration;
import ir.utux.model.ContentType;
import ir.utux.model.RequestHeader;
import ir.utux.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessHttpResponse {
    private final Logger LOGGER=LoggerFactory.getLogger(this.getClass().getName());

    private static final String VERSION="HTTP/1.0";
    private final RequestHeader requestHeader;
    private final String CRLF="\r\n"; // 13 10
    private List<String> headers=new ArrayList<>();
    private WebConfiguration webConfiguration;
    private byte[] pageBody;

    public ProcessHttpResponse(RequestHeader requestHeader) throws IOException {
        this.requestHeader=requestHeader;
        this.webConfiguration=new WebConfiguration();
        generateResponse();
    }

    private void generateResponse() throws IOException {
        switch (requestHeader.getMethod()) {
            case HEAD -> fillHeader(Status._200);
            case GET -> {
                try {

                    String requestPage=webConfiguration.getWebFolder() + requestHeader.getRequestURI();
                    File request=new File(requestPage);
                    if (request.isDirectory()) {
                        // Check default Page is Exist
                        File defaultPage=new File(requestPage + webConfiguration.getDefaultPage());
                        if (defaultPage.isFile()) {
                            fillHeader(Status._200);
                            fillContentType(requestPage + webConfiguration.getDefaultPage());
                            creatResponseBody(getFileBytes(defaultPage));
                        } else {
                            fillHeader(Status._404);
                            creatResponseBody(Status._404.toString());
                        }
                    } else if (request.isFile()) {
                        fillHeader(Status._200);
                        fillContentType(requestPage + webConfiguration.getDefaultPage());
                        creatResponseBody(getFileBytes(request));
                    } else {
                        LOGGER.info("Bad Request ." + requestHeader.getRequestURI());
                        fillHeader(Status._404);
                        creatResponseBody(Status._404.toString());
                    }
                } catch (RuntimeException e) {
                    LOGGER.info("Error to create response ." + requestHeader.getRequestURI());

                    fillHeader(Status._404);
                    creatResponseBody(Status._404.toString());
                }
            }

            case UNRECOGNIZED -> {
                fillHeader(Status._404);
                creatResponseBody(Status._404.toString());
            }
            default -> {
                LOGGER.info("Server Error ." + requestHeader.getRequestURI());

                fillHeader(Status._501);
                creatResponseBody(Status._501.toString());
            }
        }
    }

    private void creatResponseBody(byte[] fileBytes) {

        pageBody=fileBytes;
    }

    private void creatResponseBody(String fileBytes) {
        pageBody=fileBytes.getBytes();
    }

    private void fillContentType(String filePath) {
        try {
            String extension=filePath.substring(filePath.lastIndexOf(".") + 1);
            headers.add(ContentType.valueOf(extension.toUpperCase()).toString()+CRLF);
        } catch (Exception e) {
            LOGGER.error("content type not found", e);
        }
    }

    private void fillHeader(Status status) {

        headers.add(this.VERSION + " " + status.toString() + CRLF);
        headers.add("Server: utuxWebServer" + CRLF);
        headers.add("Connection: close" + CRLF);

    }

    private byte[] getFileBytes(File file) throws IOException {
        int length=(int) file.length();
        byte[] array=new byte[length];
        InputStream in=new FileInputStream(file);
        int offset=0;
        while (offset < length) {
            int count=in.read(array, offset, (length - offset));
            offset+=count;
        }
        in.close();
        return array;
    }

    public void writeResponse(OutputStream stream) {
        headers.add("Content-Length: "+pageBody.length+CRLF);
        DataOutputStream outputStream=new DataOutputStream(stream);
        headers.forEach(s -> {
            try {
                outputStream.writeBytes(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            outputStream.writeBytes(CRLF);
            if (pageBody != null) {
                outputStream.write(pageBody);
            }
            outputStream.writeBytes(CRLF);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
