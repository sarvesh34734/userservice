package dev.sarvesh.userservice;

import dev.sarvesh.userservice.models.Role;
import dev.sarvesh.userservice.models.User;
import dev.sarvesh.userservice.repositories.RoleRepository;
import dev.sarvesh.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.Optional;

@SpringBootApplication
public class UserserviceApplication implements CommandLineRunner {

	@Autowired
	private RegisteredClientRepository jpaRegisteredClientRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<User> user = userRepository.findByEmail("sarvesh_vyas1@outlook.com");
		Role role = new Role();
		role.setRole("SELLER");
		roleRepository.save(role);

		user.get().getRoles().add(role);
		userRepository.save(user.get());
	}
}
