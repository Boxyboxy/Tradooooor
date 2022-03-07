package ibf2.FinalAssessment.models;

import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class IEXQuote {
  private String companyName;
  private String symbol;
  private BigDecimal price;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public static IEXQuote create(JsonObject o) {
    final IEXQuote quote = new IEXQuote();
    quote.setCompanyName(o.getString("companyName"));
    BigDecimal price = new BigDecimal(o.getJsonNumber("latestPrice").toString());
    quote.setPrice(price);
    quote.setSymbol(o.getString("symbol"));

    return quote;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("companyName", companyName)
        .add("price", price)
        .add("symbol", symbol)
        .build();
  }

}
