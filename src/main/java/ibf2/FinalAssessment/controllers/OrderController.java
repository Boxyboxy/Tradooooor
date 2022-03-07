package ibf2.FinalAssessment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2.FinalAssessment.models.Order;
import ibf2.FinalAssessment.models.User;
import ibf2.FinalAssessment.services.IEXService;
import ibf2.FinalAssessment.services.TradeService;
import ibf2.FinalAssessment.services.UserService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import static ibf2.FinalAssessment.config.Constants.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/trade", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

  @Autowired
  @Qualifier(IEX_SERVICE)
  public IEXService iexSvc;

  @Autowired
  @Qualifier(TRADE_SERVICE)
  public TradeService tradeSvc;

  @Autowired
  @Qualifier(USER_SERVICE)
  public UserService userSvc;

  @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> history(@RequestBody String payload) {
    System.out.println(">>>> payload: " + payload);

    User user = null;
    JsonObject err;

    try {
      user = User.create(payload);
    } catch (Exception ex) {
      System.out.println(">>> getMessage: " + ex.getMessage());
      err = Json.createObjectBuilder()
          .add("error", ex.getMessage())
          .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(err.toString());
    }

    // todo get list of orders
    List<Order> orderHistory = userSvc.getOrderHistory(user.getEmail());
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    orderHistory.stream().map(v -> v.toJson()).forEach(arrayBuilder::add);
    return ResponseEntity.ok(arrayBuilder.build().toString());
  }

  @PostMapping(path = "/buy", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> buy(@RequestBody String payload) {
    System.out.println(">>>> payload: " + payload);

    Order order = null;
    JsonObject err;
    try {
      order = Order.create(payload);
    } catch (Exception ex) {
      System.out.println(">>> getMessage: " + ex.getMessage());
      err = Json.createObjectBuilder()
          .add("error", ex.getMessage())
          .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(err.toString());
    }
    System.out.println(order.getTimeStamp());
    Optional<Integer> opt = tradeSvc.buy(order);

    if (opt.isEmpty()) {
      JsonObject success;

      success = Json.createObjectBuilder()
          .add("message",
              String.format("%d share(s) of %s bought at %s USD", order.getShares(), order.getSymbol(),
                  order.getPrice()))
          .build();
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(success.toString());
    }

    switch (opt.get()) {
      case 401:
        err = Json.createObjectBuilder()
            .add("error", String.format(
                "User with email: %s not found", order
                    .getEmail()))
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(err.toString());

      case 402:
        err = Json.createObjectBuilder()
            .add("error",
                "Account cash balance insufficient")
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(err.toString());
      default:
        err = Json.createObjectBuilder()
            .add("error", "Failed to create new trade")
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(err.toString());
    }
  }

  @PostMapping(path = "/sell", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> sell(@RequestBody String payload) {

    // todo: logic for selling stocks
    // validate quantity of stocks first
    // payload shall be an order
    // verify shares held by account is more than order quantity before selling
    System.out.println(">>>> payload: " + payload);

    Order order = null;
    JsonObject err;
    try {
      order = Order.create(payload);
    } catch (Exception ex) {
      System.out.println(">>> getMessage: " + ex.getMessage());
      err = Json.createObjectBuilder()
          .add("error", ex.getMessage())
          .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(err.toString());
    }

    Optional<Integer> opt = tradeSvc.sell(order);

    if (opt.isEmpty()) {
      JsonObject success = Json.createObjectBuilder()
          .add("message",
              String.format("%d share(s) of %s  sold at %s USD", -(order.getShares()), order.getSymbol(),
                  order.getPrice()))
          .build();
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(success.toString());
    }

    switch (opt.get()) {
      case 401:
        err = Json.createObjectBuilder()
            .add("error", String.format(
                "User with email: %s not found", order
                    .getEmail()))
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(err.toString());

      case 402:
        err = Json.createObjectBuilder()
            .add("error",
                "Account stock balance insufficient")
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(err.toString());
      default:
        err = Json.createObjectBuilder()
            .add("error", "Failed to create new trade")
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(err.toString());
    }
  }
}
