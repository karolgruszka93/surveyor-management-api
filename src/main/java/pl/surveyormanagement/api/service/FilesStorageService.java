package pl.surveyormanagement.api.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	  public void init();

	  public Path save(MultipartFile file);

	  public Resource load(String location);

	  public boolean delete(String location);
	  
	  public void deleteAll();

	  public Stream<Path> loadAll();
}
