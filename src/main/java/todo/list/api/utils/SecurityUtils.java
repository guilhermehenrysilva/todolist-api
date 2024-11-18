package todo.list.api.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import todo.list.api.infra.security.SecurityConfigurations;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityUtils {

    private SecurityUtils() { }

    @Getter
    private static List<AntPathRequestMatcher> allowedRequestMatchers = new ArrayList<>();

    @Autowired
    public SecurityUtils(SecurityConfigurations securityConfigurations) {
        allowedRequestMatchers = securityConfigurations.getAllowedRequestMatchers();
    }

}