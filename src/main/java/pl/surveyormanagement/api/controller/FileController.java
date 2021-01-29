package pl.surveyormanagement.api.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import pl.surveyormanagement.api.dao.FileDAO;
import pl.surveyormanagement.api.dao.TaskDAO;
import pl.surveyormanagement.api.entities.File;
import pl.surveyormanagement.api.entities.Task;
import pl.surveyormanagement.api.payload.response.MessageResponse;
import pl.surveyormanagement.api.service.FilesStorageService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class FileController {
	
	@Value("${SurveyorManagement.api.jwtSecret}")
	private String jwtSecret;
	
	private int jwtExpirationMs = 2000;
	
	@Autowired
	TaskDAO taskDAO;
	
	@Autowired
	FilesStorageService fileStorageService;
	
	@Autowired
	FileDAO fileDAO;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("taskId") long taskId) {
		try {
			Path location = fileStorageService.save(file);
			
			Task task = taskDAO.findById(taskId)
					.orElseThrow(() -> new RuntimeException("Error: Task is not found."));
			
			fileDAO.save(new File(location.toString(), task));

			return ResponseEntity.ok(new MessageResponse("Pomyślnie dodano plik"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas dodawania plików"));
		}
	}
	
	@GetMapping("/files/{taskId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getFilesList(@Valid @PathVariable long taskId) {
		try {
			List<File> filesList = new ArrayList<File>();

			fileDAO.findByTaskId(taskId).forEach(filesList::add);

			return new ResponseEntity<>(filesList, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}
	
	@GetMapping("/file/{fileId}/{token}")
	public ResponseEntity<?> getFile(@Valid @PathVariable long fileId, @PathVariable String token) {
		try {
			boolean isTokenValid = validateDownloadToken(token);
			
			if(isTokenValid) {
				File file = fileDAO.findById(fileId).orElseThrow(() -> new RuntimeException("Error: File is not found."));;
				String location = file.getLocation();
				
				Resource resource = fileStorageService.load(location);

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename().substring(12)).body(resource); 
			}
			else {
				return ResponseEntity.badRequest().body(new MessageResponse("Brak uprawnień do pobierania danych"));
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}
	
	@DeleteMapping("/files/{taskId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> deleteFilesByTaskId(@Valid @PathVariable long taskId) {
		try {
			List<File> filesList = new ArrayList<File>();

			fileDAO.findByTaskId(taskId).forEach(filesList::add);
			
			for(File file:filesList) {
				boolean isFileDeleted = fileStorageService.delete(file.getLocation());
				if(isFileDeleted) {
					fileDAO.deleteById(file.getId());
				}
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Wystąpił błąd podczas pobierania danych"));
		}
	}
	
	@GetMapping("/downloadtoken/{fileId}")
	@PreAuthorize("hasRole('MANAGER')")
	public String generateDownloadToken(@Valid @PathVariable long fileId) {

		return Jwts.builder()
				.setSubject(String.valueOf(fileId))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public boolean validateDownloadToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			return false;
		} catch (MalformedJwtException e) {
			return false;
		} catch (ExpiredJwtException e) {
			return false;
		} catch (UnsupportedJwtException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
