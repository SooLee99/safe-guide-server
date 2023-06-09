package com.example.backend.safe_guide.configuration;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        String error = exception.getMessage();
        System.out.println("A failed login attempt with userId: " + userId + ". Reason: " + error);

        String redirectURL = request.getContextPath() + "/login?error";
        super.setDefaultFailureUrl(redirectURL);
        super.onAuthenticationFailure(request, response, exception);
    }
}
