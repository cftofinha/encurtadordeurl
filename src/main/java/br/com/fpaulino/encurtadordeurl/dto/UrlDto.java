package br.com.fpaulino.encurtadordeurl.dto;

import java.util.Date;

public class UrlDto {

	private String urlCompleta;
	private String urlCurta;
	private String dtRegistro;

	public String getUrlCompleta() {
		return urlCompleta;
	}

	public String getUrlCurta() {
		return urlCurta;
	}

	public void setUrlCurta(String urlCurta) {
		this.urlCurta = urlCurta;
	}

	public String getDtRegistro() {
		return dtRegistro;
	}

	public void setDtRegistro(String dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public void setUrlCompleta(String urlCompleta) {
		this.urlCompleta = urlCompleta;
	}
}
