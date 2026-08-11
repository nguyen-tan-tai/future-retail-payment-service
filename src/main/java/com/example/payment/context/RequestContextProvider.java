package com.example.payment.context;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestContextProvider {

    private final HttpServletRequest request;

    public RequestContextProvider(HttpServletRequest request) {
        this.request = request;
    }

    public RequestContext get() {
        return new RequestContext(
                this.getClientIp(request),
                request.getHeader("User-Agent"),
                request.getHeader("X-Request-ID"),
                request.getHeader("X-Correlation-ID"));
    }

    public String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()
                && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()
                && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
