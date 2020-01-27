package it.objectmethod.loginJpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.objectmethod.loginJpa.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByEmailAndPassword(String email, String password);
	
	public List<User> findByEmail(String email);

}
