package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Boolean existsByCpfOrEmail(String cpf, String email);
    Usuario findByCpfOrEmail(String cpf, String email);
    List<Usuario> findByDataNascimentoAfter(LocalDate dataNascimento);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

}
