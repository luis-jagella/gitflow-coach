package com.luisjagella.gitflowcoach.config;

import com.luisjagella.gitflowcoach.repository.ChecklistItemRepository;
import com.luisjagella.gitflowcoach.repository.ProjetoRepository;
import com.luisjagella.gitflowcoach.repository.TarefaRepository;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class DevProfileApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ServletContext servletContext;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void deveIniciarContextoComPerfilDevEDataSourceH2() throws Exception {
        assertNotNull(applicationContext);
        assertTrue(environment.matchesProfiles("dev"));

        try (Connection connection = dataSource.getConnection()) {
            assertEquals("H2", connection.getMetaData().getDatabaseProductName());
            assertTrue(connection.getMetaData().getURL().startsWith("jdbc:h2:mem:gitflow_coach_dev"));
        }
    }

    @Test
    void deveCarregarRepositories() {
        assertNotNull(projetoRepository);
        assertNotNull(tarefaRepository);
        assertNotNull(checklistItemRepository);
    }

    @Test
    void deveManterDocumentoOpenApiDisponivel() throws Exception {
        HttpResponse<String> response = get("/v3/api-docs");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("GitFlow Coach API"));
    }

    @Test
    void deveRegistrarEDisponibilizarConsoleH2() throws Exception {
        boolean consoleRegistrado = servletContext.getServletRegistrations()
                .values()
                .stream()
                .flatMap(registration -> registration.getMappings().stream())
                .anyMatch("/h2-console/*"::equals);

        assertTrue(consoleRegistrado);
        assertRespostaDisponivel(get("/h2-console"));
        assertRespostaDisponivel(get("/h2-console/"));
    }

    private HttpResponse<String> get(String caminho) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + caminho))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void assertRespostaDisponivel(HttpResponse<String> response) {
        assertTrue(
                response.statusCode() >= 200 && response.statusCode() < 400,
                "O console H2 retornou HTTP " + response.statusCode()
        );
    }
}
