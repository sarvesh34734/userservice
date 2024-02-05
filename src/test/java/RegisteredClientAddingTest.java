import dev.sarvesh.userservice.UserserviceApplication;
import dev.sarvesh.userservice.models.Role;
import dev.sarvesh.userservice.models.User;
import dev.sarvesh.userservice.repositories.RoleRepository;
import dev.sarvesh.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootTest(classes = UserserviceApplication.class)
public class RegisteredClientAddingTest {

    @Autowired
    private RegisteredClientRepository jpaRegisteredClientRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

//    @Test
//    @Commit
//    public void addRegisteredClient(){
//        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("productservice")
//                .clientSecret(bCryptPasswordEncoder.encode("passwordofproductserviceclient"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
//                .redirectUri("https://oauth.pstmn.io/v1/callback")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();
//
//        jpaRegisteredClientRepository.save(oidcClient);
//    }

    @Test
    @Commit
    public void addUserRole(){
        Optional<User> user = userRepository.findByEmail("sarvesh_vyas@outlook.com");
        Role role = new Role();
        role.setRole("ADMIN");
        roleRepository.save(role);
        user.get().getRoles().add(role);
        userRepository.save(user.get());
    }


}
