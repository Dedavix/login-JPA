package it.objectmethod.loginJpa.auth.tables;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import it.objectmethod.loginJpa.entities.User;

@Component
public class AuthenticateTable {
	
	private Map<String, User> authTable;

	public Map<String, User> getAuthTable() {
		if(this.authTable==null) {
			authTable = new HashMap<String, User>();
		}
		return authTable;
	}
		
}