package todo.list.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import todo.list.api.domain.user.*;
import todo.list.api.infra.security.TokenResponse;
import todo.list.api.infra.security.TokenService;
import todo.list.api.utils.AuthUtils;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Value("${api.security.token.exp}")
    private String tokenExp;

    @GetMapping("/")
    public void callback(HttpServletResponse httpResponse, @AuthenticationPrincipal DefaultOAuth2User principal, OAuth2AuthenticationToken authentication) throws IOException {
        if (principal == null || principal.getName().equals("anonymousUser") || authentication == null) {
            log.info("Callback - anonymousUser - Redirect to /login");
            httpResponse.sendRedirect("/login");
            return;
        }

        String tokenJWT;
        String provider = authentication.getAuthorizedClientRegistrationId();
        AuthenticationProviderEnum providerEnum = AuthenticationProviderEnum.getProvider(provider);
        log.info("Callback - OAuth2 User - {}", providerEnum.name());

        User user = userService.insertOAuth2User(principal, providerEnum);
        tokenJWT = tokenService.generateToken(user);

        httpResponse.setHeader(HttpHeaders.SET_COOKIE, "user=" + Base64.getEncoder().encodeToString(tokenJWT.getBytes())+"; Max-Age=" + tokenExp);
        httpResponse.sendRedirect("http://localhost:3000/sign-in" + "?token=" + tokenJWT);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody @Valid SignInRequest data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(tokenJWT));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest data, UriComponentsBuilder uriBuilder) {
        User user = userService.insertUser(data.email(), data.password(), data.name());
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new SignUpResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataResponse> me() {
        var user = AuthUtils.getLoggedUser();
        return ResponseEntity.ok(new UserDataResponse(user));
    }

}
