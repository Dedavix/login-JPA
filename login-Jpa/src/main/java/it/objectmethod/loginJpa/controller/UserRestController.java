package it.objectmethod.loginJpa.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.objectmethod.loginJpa.auth.tables.AuthenticateTable;
import it.objectmethod.loginJpa.entities.User;
import it.objectmethod.loginJpa.repositories.UserRepository;
import it.objectmethod.loginJpa.statsMsg.LoginStatusMsg;

@RestController
@RequestMapping("api/user")
public class UserRestController {
	
	@Autowired
	public UserRepository userRepo;
	
	@Autowired
	public AuthenticateTable tokenTable;
	
	@PostMapping("/signin")
	public ResponseEntity<String> getUser(@RequestParam(value = "email",required = true) String email, @RequestParam(value = "password", required= true) String password) {
		User user = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		String statusMsg;
		user = userRepo.findByEmailAndPassword(email, password);
		ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.ACCEPTED);
		if (user== null) {
			user = new User();
			statusMsg=LoginStatusMsg.KO_LOGIN.toString();
			response = ResponseEntity.badRequest().body(statusMsg);
		} else {
			String uniqueID = UUID.randomUUID().toString();
			tokenTable.getAuthTable().put(uniqueID, user);
			responseHeaders.set("token", uniqueID);
			statusMsg = LoginStatusMsg.OK_LOGIN.toString();
			response = ResponseEntity.ok()
				      .headers(responseHeaders)
				      .body(statusMsg);
		}
		return response	;
	}
	
	@PostMapping("/save")
	public String registerUser(@RequestBody User user) {
		String nome = user.getNome();
		String cognome = user.getCognome();
		String email= user.getEmail();
		String password = user.getPassword();
		String outputMsg = null;
		
		if(nome == null || cognome == null || email == null || password == null) {
			outputMsg = LoginStatusMsg.ERR_REGISTRAZIONE.toString();
		}else {
			List<User> userList = userRepo.findByEmail(email);
			if (!userList.isEmpty()) {
				outputMsg = LoginStatusMsg.KO_REGISTRAZIONE.toString();
			} else {
			    User rs = userRepo.save(user);
				if(rs != null) {
					outputMsg = LoginStatusMsg.OK_REGITRAZIONE.toString();
				}else {
					outputMsg = LoginStatusMsg.ERR_REGISTRAZIONE.toString();
				}		
			}	
		}	
		return outputMsg;	
	}
	
	@GetMapping("/getNome")
	public String getNome(@RequestHeader(value="token")String token) {
		
		User user = tokenTable.getAuthTable().get(token);
		
		return "Ciao " + user.getNome();
		
	}
	
	

}
