package vn.edu.iuh.fit.orderservice.configs.interceptor;

import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import vn.edu.iuh.fit.orderservice.constant.XHeader;

public final class RateLimitErrorHandler {

  private RateLimitErrorHandler() {
    throw new RuntimeException();
  }

  public static void handleTooManyError(
      HttpServletResponse response, ConsumptionProbe consumptionProbe) {
    final long waitForRefill = consumptionProbe.getNanosToWaitForRefill() / 1_000_000_000;
    response.addHeader(XHeader.X_RATE_LIMIT_RETRY_AFTER_SECONDS, String.valueOf(waitForRefill));

    handleResponseError(
        response,
        HttpStatus.TOO_MANY_REQUESTS,
        String.format(
            "You have exhausted your API Request Quota, please try again in [%d] seconds.",
            waitForRefill));
  }

  public static void handleForbiddenError(
      HttpServletResponse response, String header, String headerValue) {
    response.addHeader(header, headerValue);

    handleResponseError(response, HttpStatus.FORBIDDEN, "Forbidden");
  }

  public static void handleNoApiKeyErrorForbiddenError(HttpServletResponse response) {
    handleResponseError(response, HttpStatus.FORBIDDEN, "There is no X-Api-Key in request");
  }

  private static void handleResponseError(
      HttpServletResponse response, HttpStatus httpStatus, String errorMessage) {
    try {
      response.sendError(httpStatus.value(), errorMessage);
    } catch (Exception ex) {
      // Do Nothing, just return false
    }
  }
}
