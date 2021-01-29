package pl.surveyormanagement.api.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	private final Path root = Paths.get("uploads");

	@Override
	public void init() {
		try {
			if(!Files.exists(root)) {
				Files.createDirectory(root);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload.");
		}
	}

	@Override
	public Path save(MultipartFile file) {
		try {
			String uniqueId = String.valueOf(System.currentTimeMillis() + new Random().nextInt()).substring(0, 12);
			
			Files.copy(file.getInputStream(), this.root.resolve(uniqueId + file.getOriginalFilename()));
			
			Path location = this.root.resolve(uniqueId + file.getOriginalFilename());
			
			return location;
			
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	@ResponseBody
	public Resource load(String location) {
		try {
			Path path= Paths.get(location);
			Resource resource = new UrlResource(path.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file.");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	@Override
	public boolean delete(String location) {
		try {
			Path path= Paths.get(location);
	//		Resource resource = new UrlResource(path.toUri());
			
			return Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not delete the file.");
		}
	}
	
	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files.");
		}
	}
}
