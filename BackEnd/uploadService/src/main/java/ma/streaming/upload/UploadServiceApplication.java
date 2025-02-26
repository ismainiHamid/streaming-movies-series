package ma.streaming.upload;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@OpenAPIDefinition(
        info = @Info(
                title = "CineWave API",
                version = "1.0.0",
                description = "APIs for upload and managing movies, series, and subtitles"
        ),
        servers = {
                @Server(url = "http://localhost:7070/cinewave/api", description = "Local development server"),
                @Server(url = "http://localhost:7070/cinewave/api", description = "Production server")
        }
)
public class UploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class, args);
    }

}
