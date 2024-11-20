package todo.list.api.infra.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import todo.list.api.domain.user.UserRepository;
import todo.list.api.infra.security.TokenService;
import todo.list.api.utils.SecurityUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<AntPathRequestMatcher> excludedMatchers = SecurityUtils.getAllowedRequestMatchers();
        return excludedMatchers.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            var tokenJWT = getToken(request);
            var subject = tokenService.getSubject(tokenJWT);
            var user = repository.findByEmail(subject);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private String getToken(HttpServletRequest request) {
        String tokenFromHeader = getTokenFromHeader(request);
        if (tokenFromHeader != null)
            return tokenFromHeader;

        String tokenFromCookie = getTokenFromCookie(request);
        if (tokenFromCookie != null)
            return tokenFromCookie;

        throw new RuntimeException("Token not found in this request.");
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        var tokenFromHeader = request.getHeader("Authorization");
        if (tokenFromHeader != null) {
            return tokenFromHeader.replace("Bearer ", "");
        }
        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "user".equals(cookie.getName()))
                .map(Cookie::getValue)
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .map(this::decodeToken)
                .orElse(null);
    }

    private String decodeToken(String tokenBase64) {
        byte[] tokenDecode = Base64.getDecoder().decode(tokenBase64);
        return new String(tokenDecode);
    }
}
