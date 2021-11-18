package jandcode.core.web.undertow;

import jandcode.commons.*;
import jandcode.core.web.test.*;
import org.junit.jupiter.api.*;

import java.net.*;
import java.net.http.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Undertow_Test extends Web_Test {

    @Test
    public void test1() throws Exception {
        utils.logOn();
        //
        UndertowRunner ur = new UndertowRunner();
        ur.setContext("/test");
        ur.setPort(3366);
        ur.start(app);
        //
        HttpClient client = HttpClient.newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3366/test/a1/m1"))
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


    @Test
    public void cookie2() throws Exception {
        // в разных запросах cookie должны быть одинаков в рамках одного сервера
        List<String> q = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            stopwatch.start("" + i);
            int finalI = i;
            web.execAction(request -> {
                String id = request.getHttpRequest().getSession().getId();
                q.add(id);
                System.out.println("" + finalI + "-" + id);
            });
            stopwatch.stop("" + i);
        }
        assertEquals(q.get(0), q.get(1));
        assertEquals(q.get(0), q.get(2));
        assertFalse(UtString.empty(q.get(0)));
    }


}
