package br.com.fpaulino.encurtadordeurl.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "url")
@Table(name = "url")
public class Url {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "urlCompleta")
	private String urlCompleta;

	@Column(name = "urlCurta")
	private String urlCurta;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "dtRegistro")
	private LocalDate dtRegistro;

	public Url() {

	}

	public Url(String urlCompleta, String urlCurta) {
		this.urlCompleta = urlCompleta;
		this.urlCurta = urlCurta;
	}
	
	public Url(String urlCompleta) {
        this.urlCompleta = urlCompleta;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrlCompleta() {
		return urlCompleta;
	}

	public void setUrlCompleta(String urlCompleta) {
		this.urlCompleta = urlCompleta;
	}

	public String getUrlCurta() {
		return urlCurta;
	}

	public void setUrlCurta(String urlCurta) {
		this.urlCurta = urlCurta;
	}

	public LocalDate getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(LocalDate dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	@Override
	public String toString() {
		return "Url [id=" + id + ", urlCompleta=" + urlCompleta + ", urlCurta=" + urlCurta + ", dtRegistro="
				+ dtRegistro + "]";
	}

}
