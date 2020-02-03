package it.objectmethod.login.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.objectmethod.login.auth.tables.AuthenticateTable;
import it.objectmethod.login.constants.LoginStatusMsg;
import it.objectmethod.login.entities.User;
import it.objectmethod.login.repositories.UserRepository;

@RestController
@RequestMapping("api/user")
public class UserRestController {

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public AuthenticateTable tokenTable;

	@PostMapping("/signin")
	public ResponseEntity<String> getUser(@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "password", required = true) String password) {
		User user = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		user = userRepo.findByEmailAndPassword(email, password);
		ResponseEntity<String> response = null;
		if (user == null) {
			response = ResponseEntity.badRequest().body(LoginStatusMsg.KO_LOGIN.toString());
		} else {
			String uniqueID = UUID.randomUUID().toString();
			tokenTable.getAuthTable().put(uniqueID, user);
			responseHeaders.set("token", uniqueID);
			responseHeaders.set("admin",user.getAdmin().toString());
			response = ResponseEntity.ok().headers(responseHeaders).body(LoginStatusMsg.OK_LOGIN.toString());
		}
		return response;
	}

	@PostMapping("/save")
	public String registerUser(@RequestBody User user) {
		String nome = user.getNome();
		String cognome = user.getCognome();
		String email = user.getEmail();
		String password = user.getPassword();
		String outputMsg = null;

		if (nome == null || cognome == null || email == null || password == null) {
			outputMsg = LoginStatusMsg.ERR_REGISTRAZIONE.toString();
		} else {
			User existingUser = userRepo.findByEmail(email);
			if (existingUser != null) {
				outputMsg = LoginStatusMsg.KO_REGISTRAZIONE.toString();
			} else {
				 user = userRepo.save(user);
				 outputMsg= LoginStatusMsg.OK_REGITRAZIONE.toString();
			}
		}
		return outputMsg;
	}

	@GetMapping("/getNome")
	public String getNome(@RequestHeader(value = "token") String token) {

		User user = tokenTable.getAuthTable().get(token);

		return "Ciao " + user.getNome();

	}

}
