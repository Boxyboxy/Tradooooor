package ibf2.FinalAssessment.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static ibf2.FinalAssessment.config.Constants.*;

import java.util.List;
import java.util.Optional;

import ibf2.FinalAssessment.models.Shares;
import ibf2.FinalAssessment.models.User;
import ibf2.FinalAssessment.services.EmailSenderService;
import ibf2.FinalAssessment.services.IEXService;
import ibf2.FinalAssessment.services.TradeService;
import ibf2.FinalAssessment.services.UserService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  @Autowired
  @Qualifier(EMAIL_SERVICE)
  public EmailSenderService emailSenderService;

  @Autowired
  @Qualifier(IEX_SERVICE)
  public IEXService iexSvc;

  @Autowired
  @Qualifier(TRADE_SERVICE)
  public TradeService tradeService;

  @Autowired
  @Qualifier(USER_SERVICE)
  public UserService userSvc;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> createUser(@RequestBody String payload) {

    System.out.println(">>>> payload: " + payload);
    User newUser = null;
    JsonObject err;

    try {
      newUser = User.create(payload);
    } catch (Exception ex) {
      System.out.println(">>> getMessage: " + ex.getMessage());
      err = Json.createObjectBuilder()
          .add("error", ex.getMessage())
          .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(err.toString());
    }

    Optional<Integer> opt = userSvc.createUser(newUser);

    if (opt.isEmpty()) {
      emailSenderService.sendEmail(newUser.getEmail(), "Account created at NUS-ISS Broker",
          "Account created at NUS-ISS Broker. Welcome to start trading!");
      JsonObject success;
      success = Json.createObjectBuilder()
          .add("message",
              String.format("Account for %s created", newUser.getEmail()))
          .build();
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(success.toString());

    }

    switch (opt.get()) {
      case 401:
        err = Json.createObjectBuilder()
            .add("error", String.format(
                "Account with email: %s already exists.", newUser.getEmail()))
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(err.toString());
      default:
        err = Json.createObjectBuilder()
            .add("error", "Failed to create trading account")
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(err.toString());

    }
  }

  @PostMapping(path = "/portfolio", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getSharesPortfolio(@RequestBody String payload) {

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

    List<Shares> sharePortfolio = this.userSvc.getShares(user.getEmail());
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    sharePortfolio.stream()
        .map(v -> v.toJson())
        .forEach(arrayBuilder::add);
    return ResponseEntity.ok(arrayBuilder.build().toString());

  }

  @PostMapping(path = "/cash", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getCashBalance(@RequestBody String payload) {
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

    User userWithCashBalance = this.userSvc.getUser(user.getEmail()).get();
    JsonObject result = userWithCashBalance.toJson();
    return ResponseEntity.ok(result.toString());

  }
}
