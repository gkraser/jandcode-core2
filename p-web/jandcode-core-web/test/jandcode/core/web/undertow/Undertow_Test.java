package jandcode.core.web.undertow;

import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.net.http.*;

public class Undertow_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        utils.logOn();
        //
        UndertowRunner ur = new UndertowRunner();
        ur.setContext("/test1");
        ur.setPort(3366);
        ur.start(app);
        //
        HttpClient client = HttpClient.newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3366/test1/a1/m1"))
                .build();
        HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        //
        System.out.println(resp);
        //
        System.out.println(resp.body());

        //
        //ur.stop();

    }

    @Test
    public void test2() throws Exception {
        WebServer ws = web.getWebServer();

        WebClientRequest req = ws.createRequest();
        req.setUri("a1/m1");

        WebClientResponse resp = req.exec();

        System.out.println(resp.getBodyText());

    }

}
