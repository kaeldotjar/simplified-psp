package io.github.zam0k.simplifiedpsp.utils;

import io.github.zam0k.simplifiedpsp.domain.IPayee;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@NoArgsConstructor
public class PaymentNotifier {

  @Async("asyncExecutor")
  public void notifyPayee(IPayee payee, RestTemplate restTemplate) {

    String notifyApiURL = "http://o4d9z.mocklab.io/notify";

    ResponseEntity<String> response;

    try {
      log.info("Preparing to send notification to payee...");

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(APPLICATION_JSON);

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("email", payee.getEmail());

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

      response = restTemplate.postForEntity(notifyApiURL, request, String.class);

      // TO-DO: check if there's a more fitting error for this
      if (response.getStatusCode() != CREATED) {
        log.warn("Could not send notification to payee");
        return;
      }

      log.info("Payment notification sent to payee");

    } catch (RestClientException e) {
      log.warn("Notify API currently unavailable");
    }
  }
}
