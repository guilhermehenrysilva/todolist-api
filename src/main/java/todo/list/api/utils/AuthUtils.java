package todo.list.api.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import todo.list.api.domain.user.User;

public class AuthUtils {

    public static User getLoggedUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
