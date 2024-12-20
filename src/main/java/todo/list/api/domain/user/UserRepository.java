package todo.list.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0
            THEN true
            ELSE false
            END
            FROM User u
            WHERE u.email = :email
            AND
            u.authenticationProvider <> :authenticationProvider
            """)
    boolean existsByEmailAndIsDifferentFromAuthenticationProvider(@Param("email") String email, @Param("authenticationProvider") AuthenticationProviderEnum authenticationProvider);

    Optional<User> findByEmailAndAuthenticationProvider(String email, AuthenticationProviderEnum authenticationProvider);
}
