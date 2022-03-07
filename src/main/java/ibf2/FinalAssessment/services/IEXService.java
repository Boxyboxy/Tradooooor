package ibf2.FinalAssessment.services;

import static ibf2.FinalAssessment.config.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2.FinalAssessment.models.IEXQuote;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service(IEX_SERVICE)
public class IEXService {
  private final Logger logger = Logger.getLogger(IEXService.class.getName());

  public final String appKey;

  public IEXService() {
    // set iex_api_key=pk_ff3b4f4e74ff41ff8f7af7667e0ade03
    String token = System.getenv(IEX_API_KEY);

    if ((null != token) && (token.trim().length() > 0))
      appKey = token;
    else
      appKey = "abc123";
  }

  public JsonObject getJson(String ticker) {
    String url = UriComponentsBuilder.fromUriString(URL_IEX.formatted(ticker))
        .queryParam("token", appKey).toUriString();
    logger.info("url:" + url);

    final RequestEntity<Void> req = RequestEntity.get(url).build();
    final RestTemplate template = new RestTemplate();
    final ResponseEntity<String> resp = template.exchange(req, String.class);
    logger.info("Status code: " + resp.getStatusCodeValue());
    logger.info("Payload:" + resp.getBody());
    if (resp.getStatusCode() != HttpStatus.OK)
      throw new IllegalArgumentException("Error: status code %s".formatted(resp.getStatusCode().toString()));
    final String body = resp.getBody();
    try (InputStream is = new ByteArrayInputStream(body.getBytes())) {
      final JsonReader reader = Json.createReader(is);
      final JsonObject result = reader.readObject();
      logger.info("Transformed Json Object: " + IEXQuote.create(result).toJson().toString());
      return IEXQuote.create(result).toJson();

    } catch (Exception e) {
    }

    return Json.createObjectBuilder().build();
  }

}
