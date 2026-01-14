package edu.iuh.fit.demoorder;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import redis.clients.jedis.Jedis;

import java.io.IOException;

@WebServlet(urlPatterns = "/orders", asyncSupported = true)
public class OrderServlet extends HttpServlet {
    private final OrderDB orderDB = new OrderDB();
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext ctx = request.startAsync();
        ctx.start(() -> {
            try {
                var list = orderDB.getOrders();
                var string = jsonb.toJson(list);
                ctx.getResponse().setContentType("application/json");
                ctx.getResponse().getWriter().println(string);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ctx.complete();
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = new Order();
        order.setCustomerName(req.getParameter("customerName"));
        orderDB.addOrder(order);

        try (Jedis jedis = new Jedis("localhost", 6379)) {
            jedis.rpush("orders", order.toString());
        } catch (Exception e) {
        }

        System.out.println(order);

        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}