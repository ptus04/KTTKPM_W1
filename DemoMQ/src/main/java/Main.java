import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] orders = {
                "Order 01",
                "Order 02",
                "Order 03",
                "Order 04",
                "Order 05",
        };

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String queueName = "orders";

            for (int i = 0; i < 100; i++) {
                int index = new Random().nextInt(0, 4);
                String message = orders[index];
                jedis.rpush(queueName, message);
                System.out.println("Sent: " + message);
                Thread.sleep(750);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
