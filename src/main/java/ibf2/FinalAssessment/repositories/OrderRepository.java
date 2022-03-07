package ibf2.FinalAssessment.repositories;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2.FinalAssessment.models.Order;

@Repository
public class OrderRepository {

  private static final String SQL_INSERT_NEW_ORDER = "INSERT INTO orders (email, symbol, shares, price, time_stamp) VALUES (?, ?, ?, ?, ?)";

  private static final String SQL_GET_ALL_ORDERS_BY_EMAIL = "SELECT * FROM orders WHERE email = ( ? )";

  @Autowired
  private JdbcTemplate template;

  public BigDecimal createOrder(Order order) {

    int created = template.update(SQL_INSERT_NEW_ORDER,
        order.getEmail(),
        order.getSymbol(),
        order.getShares(),
        order.getPrice(),
        Timestamp.from(order.getTimeStamp()));
    if (created > 0) {
      return (order.getPrice().multiply(BigDecimal.valueOf(order.getShares())));
    } else {
      BigDecimal zero = new BigDecimal("0.00");
      return zero;
    }
  }

  public List<Order> getOrders(String email) {
    List<Order> result = new LinkedList<>();

    final SqlRowSet rs = template.queryForRowSet(SQL_GET_ALL_ORDERS_BY_EMAIL, email);
    System.out.println(rs);
    while (rs.next())
      result.add(Order.populate(rs));
    return result;
  }

}
