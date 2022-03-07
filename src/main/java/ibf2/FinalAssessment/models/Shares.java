package ibf2.FinalAssessment.models;

import java.math.BigDecimal;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Shares {
  private String symbol;
  private Integer quantity;
  private BigDecimal totalCost;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;

  }

  public static Shares populate(SqlRowSet rs) {
    final Shares share = new Shares();
    share.symbol = rs.getString("symbol");
    share.quantity = rs.getInt("quantity");
    share.totalCost = rs.getBigDecimal("total_cost");
    return share;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("symbol", symbol)
        .add("quantity", quantity)
        .add("totalCost", totalCost)
        .build();
  }

}
