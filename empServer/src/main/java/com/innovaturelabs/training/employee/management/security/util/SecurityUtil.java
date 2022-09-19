
package com.innovaturelabs.training.employee.management.security.util;

import com.innovaturelabs.training.employee.management.security.AccessTokenUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    private static AccessTokenUserDetails getToken() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof AccessTokenUserDetails)) {
            return null;
        }

        return ((AccessTokenUserDetails) principal);
    }

    public static Integer getCurrentUserId() {

        AccessTokenUserDetails principal = getToken();

        return principal != null ? principal.userId : null;

    }

    public static String getCurrentUserRole() {

        AccessTokenUserDetails principal = getToken();

        return principal != null ? principal.userRole : null;
    }
}
