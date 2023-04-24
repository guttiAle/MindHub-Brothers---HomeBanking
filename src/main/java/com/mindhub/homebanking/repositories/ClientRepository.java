package com.mindhub.homebanking.repositories;
import java.util.List;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // Indica a Spring que debe generar el código necesario para que se pueda administrar la data de
// la aplicación desde el navegador usando JSON.
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
