package ibf2.FinalAssessment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import ibf2.FinalAssessment.services.IEXService;
import jakarta.json.JsonObject;

import static ibf2.FinalAssessment.config.Constants.*;

@RestController
@RequestMapping(path = "/api/stock", produces = MediaType.APPLICATION_JSON_VALUE)
public class IEXRestController {

  @Autowired
  @Qualifier(IEX_SERVICE)
  public IEXService iexSvc;

  @GetMapping(path = "{ticker}")
  public ResponseEntity<String> getTicker(@PathVariable String ticker) {
    JsonObject result = iexSvc.getJson(ticker);

    return ResponseEntity.ok(result.toString());

  }

}
