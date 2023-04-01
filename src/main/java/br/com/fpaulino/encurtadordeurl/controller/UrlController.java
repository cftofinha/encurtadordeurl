package br.com.fpaulino.encurtadordeurl.controller;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fpaulino.encurtadordeurl.common.UrlUtil;
import br.com.fpaulino.encurtadordeurl.dto.UrlCompleta;
import br.com.fpaulino.encurtadordeurl.dto.UrlCurta;
import br.com.fpaulino.encurtadordeurl.error.InvalidUrlError;
import br.com.fpaulino.encurtadordeurl.model.Url;
import br.com.fpaulino.encurtadordeurl.repository.UrlRepository;
import br.com.fpaulino.encurtadordeurl.service.BaseConversion;
import br.com.fpaulino.encurtadordeurl.service.DateTimeService;
import br.com.fpaulino.encurtadordeurl.service.UrlService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class UrlController {
	
	private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
	protected final UrlService urlService;
	
	@Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

	@Autowired
	DateTimeService dateTimeService;

	@Autowired
	UrlRepository urlRepository;
	
	@Autowired
	BaseConversion conversion;

	@PostMapping("/encurtar-url")
    public ResponseEntity<Object> saveUrl(@RequestBody UrlCompleta urlCompleta, HttpServletRequest request) {

        // Validation checks to determine if the supplied URL is valid
        UrlValidator validator = new UrlValidator(
                new String[]{"http", "https"}
        );
        String url = urlCompleta.getUrlCompleta();
        if (!validator.isValid(url)) {
            logger.error("URL mal formada fornecida");

            InvalidUrlError error = new InvalidUrlError("url", urlCompleta.getUrlCompleta(), "URL inválida");

            // returns a custom body with error message and bad request status code
            return ResponseEntity.badRequest().body(error);
        }
        String baseUrl = null;

        try {
            baseUrl = UrlUtil.getBaseUrl(request.getRequestURL().toString());
        } catch (MalformedURLException e) {
        	logger.error("URL de solicitação mal formada");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL de solicitação é inválida", e);
        }

        // Retrieving the Shortened url and concatenating with protocol://domain:port
        UrlCurta urlCurta = urlService.getUrlCurta(urlCompleta);
        urlCurta.setUrlCurta(baseUrl + urlCurta.getUrlCurta());

        logger.debug(String.format("A url curta para a url completa %s é %s", urlCompleta.getUrlCompleta(), urlCurta.getUrlCurta()));

        return new ResponseEntity<>(urlCurta, HttpStatus.OK);
    }
	
	@GetMapping("/urls-encurtadas")
	public ResponseEntity<List<Url>> getTodasUrls(@RequestParam(required = false) String dtRegistro) {
		try {
			List<Url> urls = new ArrayList<Url>();

			if (dtRegistro == null) {
				urlRepository.findAll().forEach(urls::add);
			}
			else {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String data = "31/03/2023";
				Date date = null;
				try {
					date = formatter.parse(data);
					System.out.println(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//urlRepository.findByDtRegistroContaining(date).forEach(urls::add);
			}
			if (urls.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(urls, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/urls-encurtadas2")
	public ResponseEntity<List<Url>> getUrlsPosts(@RequestParam("dtRegistro") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtRegistro) {
		try {
			List<Url> urls = new ArrayList<Url>();
			//("dtRegistro") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dtRegistro

			if (dtRegistro == null) {
				urlRepository.findAll().forEach(urls::add);
			}
			else {
				//Url _url = new Url(url.getUrlCompleta(), url.getUrlCurta(), new Date());
				System.out.println(dtRegistro);
				dateTimeService.processDate(dtRegistro);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date dataFormatada = null;
				try {
					dataFormatada = formatter.parse(dtRegistro.toString());
					System.out.println("data formatada = " + dataFormatada);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				urlRepository.findByDtRegistroContaining(dtRegistro).forEach(urls::add);
			}
			if (urls.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(urls, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name,
            @RequestParam(value = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return String.format("Hello %s! %s", name, date);
	}
	
	@RequestMapping(value = "date", method = RequestMethod.POST)
    public void processDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		logger.info("Processing date: {}", date);
        dateTimeService.processDate(date);
    }
	
	@GetMapping("/urls-encurtadas/{id}")
	public ResponseEntity<Url> getTutorialById(@PathVariable("id") long id) {
		Optional<Url> urlData = urlRepository.findById(id);

		if (urlData.isPresent()) {
			return new ResponseEntity<>(urlData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/criar-url-encurtada")
	public ResponseEntity<Url> createUrlEncurtada(@RequestBody Url url) {
		
		try {
			Url _url = urlRepository.save(new Url(url.getUrlCompleta(), url.getUrlCurta()));
			conversion.encode(_url.getId());
			
			return new ResponseEntity<>(_url, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
