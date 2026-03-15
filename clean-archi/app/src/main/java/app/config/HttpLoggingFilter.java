package app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Custom HTTP request/response logging filter for debugging
 * Logs all incoming requests and outgoing responses with details
 */
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String requestId = String.valueOf(System.nanoTime());

        // Log incoming request
        logIncomingRequest(request, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Log outgoing response
            long duration = System.currentTimeMillis() - startTime;
            logOutgoingResponse(response, requestId, duration);
        }
    }

    private void logIncomingRequest(HttpServletRequest request, String requestId) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String contentType = request.getContentType();

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
        sb.append(String.format("REQUEST [%s] %s %s\n", requestId, method, uri));
        if (queryString != null) {
            sb.append(String.format("Query String: %s\n", queryString));
        }
        sb.append(String.format("Content-Type: %s\n", contentType != null ? contentType : "N/A"));
        sb.append("Headers:\n");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            // Don't log sensitive headers fully
            if (isSensitiveHeader(headerName)) {
                headerValue = "***REDACTED***";
            }
            sb.append(String.format("  %s: %s\n", headerName, headerValue));
        }

        sb.append(String.format("Remote Address: %s\n", request.getRemoteAddr()));
        sb.append("═══════════════════════════════════════════════════════════════════════════════");

        logger.debug(sb.toString());
    }

    private void logOutgoingResponse(HttpServletResponse response, String requestId, long duration) {
        int status = response.getStatus();
        String contentType = response.getContentType();

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("───────────────────────────────────────────────────────────────────────────────\n");
        sb.append(String.format("RESPONSE [%s] HTTP %d\n", requestId, status));
        sb.append(String.format("Content-Type: %s\n", contentType != null ? contentType : "N/A"));
        sb.append(String.format("Duration: %dms\n", duration));
        sb.append("Headers:\n");

        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            if (isSensitiveHeader(headerName)) {
                headerValue = "***REDACTED***";
            }
            sb.append(String.format("  %s: %s\n", headerName, headerValue));
        });

        sb.append("───────────────────────────────────────────────────────────────────────────────");

        // Log at different levels based on status code
        if (status >= 400) {
            logger.warn(sb.toString());
        } else if (status >= 300) {
            logger.info(sb.toString());
        } else {
            logger.debug(sb.toString());
        }
    }

    /**
     * Check if a header should be redacted for security
     */
    private boolean isSensitiveHeader(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.contains("authorization") ||
                lower.contains("cookie") ||
                lower.contains("password") ||
                lower.contains("secret") ||
                lower.contains("token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Optionally skip logging for health check endpoints
        String uri = request.getRequestURI();
        return uri.contains("/actuator/health") || uri.contains("/health");
    }
}

