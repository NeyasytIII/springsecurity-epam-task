package com.epamtask.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final String TRANSACTION_ID = "transactionId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionId);
        response.setHeader("X-Transaction-Id", transactionId);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        logRequest(wrappedRequest);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
            MDC.remove(TRANSACTION_ID);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("Incoming request ")
                .append(request.getMethod()).append(" ")
                .append(request.getRequestURI());
        String query = request.getQueryString();
        if (query != null) {
            msg.append("?").append(query);
        }
        msg.append("\nHeaders: ");
        request.getHeaderNames().asIterator().forEachRemaining(name ->
                msg.append(name).append("=").append(request.getHeader(name)).append("; ")
        );
        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            String payload = new String(buf, 0, buf.length, request.getCharacterEncoding());
            msg.append("\nPayload: ").append(payload);
        }
        log.info(msg.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("Outgoing response: status=").append(response.getStatus());
        msg.append("\nHeaders: ");
        response.getHeaderNames().forEach(name ->
                msg.append(name).append("=").append(response.getHeader(name)).append("; ")
        );
        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            String payload = new String(buf, 0, buf.length, response.getCharacterEncoding());
            msg.append("\nPayload: ").append(payload);
        }
        log.info(msg.toString());
    }
}
