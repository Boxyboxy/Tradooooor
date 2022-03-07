package ibf2.FinalAssessment.models;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Order {
  private Integer orderId;
  private String email;
  private String symbol;
  private Integer shares;
  private BigDecimal price;
  private Date timeStamp;

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Integer getShares() {
    return shares;
  }

  public void setShares(Integer shares) {
    this.shares = shares;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public static Order populate(SqlRowSet rs) {
    final Order order = new Order();
    order.orderId = rs.getInt("order_id");
    order.email = rs.getString("email");
    order.symbol = rs.getString("symbol");
    order.shares = rs.getInt("shares");
    order.price = rs.getBigDecimal("price");
    // System.out.println(rs.getTimestamp("time_stamp"));
    // order.timeStamp = rs.getObject("time_stamp", Date.class);
    // order.timeStamp =
    // java.sql.Timestamp.valueOf(rs.getTimestamp("time_stamp").toLocalDateTime());
    return order;
  }

  public static Order create(String jsonString) throws Exception {
    JsonReader r = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));

    JsonObject o = r.readObject();
    final Order tradeOrder = new Order();

    try {
      tradeOrder.orderId = o.getInt("orderId", -1);
    } catch (Exception e) {
    }
    tradeOrder.email = o.getString("email");
    tradeOrder.symbol = o.getString("symbol");
    tradeOrder.shares = o.getInt("shares");
    tradeOrder.price = (o.getJsonNumber("price").bigDecimalValue());
    tradeOrder.timeStamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(o.getString("timeStamp"));
    return tradeOrder;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("orderId", orderId)
        .add("email", email)
        .add("symbol", symbol)
        .add("shares", shares)
        .add("price", price)
        // .add("timeStamp", timeStamp.toString())
        .build();
  }
}
