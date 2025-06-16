package org.skomi.pilot.shared.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ControllerLoggingAspect {

    private final ObjectMapper objectMapper;

    public ControllerLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {}

    @Around("restControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        // Log only method arguments if present
        String payload = "no payload";
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
            try {
                payload = objectMapper.writeValueAsString(joinPoint.getArgs()[0]);
            } catch (Exception e) {
                payload = "Could not serialize payload: " + joinPoint.getArgs()[0].toString();
            }
        }

        // Log request details
        logger.info("Request: {} {} - Payload: {}",
                request.getMethod(),
                request.getRequestURI(),
                payload);

        // Execute method
        Object result = joinPoint.proceed();

        // Log response if possible
        try {
            if (result != null) {
                logger.info("Response: {}", objectMapper.writeValueAsString(result));
            } else {
                logger.info("Response: null");
            }
        } catch (Exception e) {
            logger.info("Response: Could not serialize response");
        }

        return result;
    }
}