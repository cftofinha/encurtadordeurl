package br.com.fpaulino.encurtadordeurl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.fpaulino.encurtadordeurl.model.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {
	Url findById(int id);
	List<Url> findByDtRegistroContaining(LocalDate dtRegistro);
	
	@Query("SELECT u FROM url u WHERE u.urlCompleta = ?1")
	List<Url> findUrlByUrlCompleta(String urlCompleta);
}
