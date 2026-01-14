import redis.clients.jedis.Jedis;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            String queueName = "orders";

            while (true) {
                List<String> messages = jedis.blpop(0, queueName);
                String message = messages.get(1);
                System.out.println("Order received: " + message);
            }
        }
    }
}
