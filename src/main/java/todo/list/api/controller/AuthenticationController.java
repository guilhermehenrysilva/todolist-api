package todo.list.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import todo.list.api.domain.user.*;
import todo.list.api.infra.security.TokenJWT;
import todo.list.api.infra.security.TokenService;
import todo.list.api.utils.AuthUtils;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @GetMapping
    public void callback(HttpServletResponse httpResponse) throws IOException {
        log.info("Callback");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String tokenJWT = null;

        if (principal instanceof DefaultOAuth2User) {
            log.info("Callback - OAuth2 User");
            User user = userService.insertOAuth2User((DefaultOAuth2User) principal);
            tokenJWT = tokenService.generateToken(user);
        } else {
            log.info("Callback - Api User");
            tokenJWT = tokenService.generateToken((User) principal);
        }

        httpResponse.setHeader(HttpHeaders.SET_COOKIE, "user=" + Base64.getEncoder().encodeToString(tokenJWT.getBytes())+"; Max-Age=86400");
        httpResponse.sendRedirect("http://localhost:3000" + "?token=" + tokenJWT);
    }

    @PostMapping("sign-in")
    public ResponseEntity<TokenJWT> signIn(@RequestBody @Valid SignInRequest data) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJWT(tokenJWT));
    }

    @PostMapping("sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest data, UriComponentsBuilder uriBuilder) {
        User user = userService.insertUser(data.email(), data.password(), data.name());
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new SignUpResponse(user));
    }

    @GetMapping("me")
    public ResponseEntity<UserDataResponse> me() {
        var user = AuthUtils.getLoggedUser();
        return ResponseEntity.ok(new UserDataResponse(user));
    }

}
