package it.objectmethod.login.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.objectmethod.login.entities.Articolo;
import it.objectmethod.login.repositories.ArticoloRepository;

@RestController
@RequestMapping("/api/articoli")
@CrossOrigin
public class ArticoloRestController {

	
	@Autowired
	private ArticoloRepository articoliRepo;

	@GetMapping("/list")
	public List<Articolo> mostraArticoli(@RequestParam(value = "filtro", required = false) String filtroPassato) {
		List<Articolo> listaArticoli = null;
		listaArticoli = articoliRepo.findAll();
		return listaArticoli;
	}

	// Se id articolo = 0 allora fa insert altrimenti update, per update inserire
	@PostMapping("/save")
	public String effettuaModifica(@RequestBody Articolo articolo) {
		String outputMsg = "OPERAZIONE ESEGUITA CON SUCCESSO";
		Articolo articoloSalvato = null;
		int idArticolo = articolo.getId();
		String codiceArticolo = articolo.getCodice();
		String descrizioneArticolo = articolo.getDescrizione();
		if (idArticolo != 0) {
			articoloSalvato = articoliRepo.save(articolo);
		} else {
			Articolo art = articoliRepo.findByCodice(codiceArticolo);
			if (art == null) {
				Articolo articoloInserito = new Articolo();
				articoloInserito.setCodice(codiceArticolo);
				articoloInserito.setDescrizione(descrizioneArticolo);
				articoloSalvato= articoliRepo.save(articoloInserito);
			}
		}
		if (articoloSalvato==null) {
			outputMsg = "OPERAZIONE FALLITA";
		}
		return outputMsg;
	}
	
	@GetMapping("/getById")
	public Optional<Articolo> mostraArticolo(@RequestParam(value = "id", required = true) Integer idArticolo) {
		Optional<Articolo> articolo = null;
		articolo = articoliRepo.findById(idArticolo);
		return articolo;
	}

}
