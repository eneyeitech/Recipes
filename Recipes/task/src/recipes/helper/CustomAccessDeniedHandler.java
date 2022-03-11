package recipes.helper;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import recipes.business.User;
import recipes.business.UserDetailImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            User user = ((UserDetailImpl) auth.getPrincipal()).getUser();

            /*response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(new JSONObject()
                    .put("message", "Access Denied!")
                    .put("timestamp", LocalDateTime.now())
                    .put("error","Forbidden")
                    .put("status", 403)
                    .put("path", request.getRequestURI())
                    .toString());*/

        }
    }
}
