package jandcode.web.gsp;

import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Тестирование многопоточного доступа к map
 */
public class MapSync_Test extends Utils_Test {

    Map map = new HashMap<>();
    Random rnd = new Random();

    class Get1 extends Thread {

        public void run() {
            while (true) {
                try {
                    //Thread.sleep(rnd.nextInt(30));
                } catch (Exception e) {
                    //ignore
                }
                try {
                    String key = "k" + rnd.nextInt(100);
                    map.get(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class Put1 extends Thread {

        public void run() {
            while (true) {
                try {
                    //Thread.sleep(rnd.nextInt(30));
                } catch (Exception e) {
                    //ignore
                }
                try {
                    String key = "k" + rnd.nextInt(100);
                    String value = "v" + rnd.nextInt(100);
                    map.put(key, value);
                    //System.out.println(getName() + " : " + key + "=" + value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test // нужен, иначе с командной строки - ошибка
    public void test1_dummy() throws Exception {
    }

    //@Test
    public void test1() throws Exception {

        for (int i = 0; i < 20; i++) {
            Thread t = new Put1();
            t.setDaemon(true);
            t.start();
        }

//        for (int i = 0; i < 20; i++) {
//            Thread t = new Get1();
//            t.setDaemon(true);
//            t.start();
//        }

        Thread.sleep(15000);

        System.out.println(map.size());
        int n = 0;
        for (Object v : map.values()) {
            n++;
            System.out.println(v);
            if (n > 10) {
                break;
            }
        }

        List<String> r = new ArrayList<>();
        r.addAll(map.keySet());
        Collections.sort(r);
        System.out.println(r);

        r = new ArrayList<>();
        r.addAll(map.values());
        Collections.sort(r);
        System.out.println(r);


    }


}
