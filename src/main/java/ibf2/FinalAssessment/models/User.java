package ibf2.FinalAssessment.models;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class User {
  private String email;

  private BigDecimal cash;

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public BigDecimal getCash() {
    return cash;
  }

  public void setCash(BigDecimal cash) {
    this.cash = cash;
  }

  public static User populate(SqlRowSet rs) {
    final User u = new User();
    u.setEmail(rs.getString("email"));
    u.setCash(rs.getBigDecimal("cash"));
    return u;
  }

  public static User create(String jsonString) throws Exception {
    JsonReader r = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));

    JsonObject o = r.readObject();
    final User newUser = new User();

    try {
      newUser.email = o.getString("email");
    } catch (Exception e) {
    }

    return newUser;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
        .add("email", email)
        .add("cash", cash)
        .build();
  }

}
