package it.objectmethod.login.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.objectmethod.login.entities.Articolo;

@Repository
public interface ArticoloRepository extends JpaRepository<Articolo, Integer> {
	
    public Optional<Articolo> findById(Integer id);
	
	public Articolo findByCodice(String codice);

	public List<Articolo> findAll();

}
