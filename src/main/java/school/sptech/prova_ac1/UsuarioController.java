package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 1. Cadastro de usuário
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {

        if (usuarioRepository.existsByCpfOrEmail(usuario.getCpf(), usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // 2. Listagem de todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();

        if (listaUsuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(listaUsuarios);
    }

    // 3. Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElse(null);

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarioEncontrado);
    }

    // 4. Exclusão de usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 5. Busca Data de Nascimento maior que
    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        List<Usuario> pesquisaData = usuarioRepository.findByDataNascimentoAfter(nascimento);

        if (pesquisaData.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(pesquisaData);
    }

    // 6. Atualizar usuário (Vale o 10 🤓)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElse(null);

        if (usuarioEncontrado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!usuarioEncontrado.getEmail().equals(usuario.getEmail()) &&
                usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (!usuarioEncontrado.getCpf().equals(usuario.getCpf()) &&
                usuarioRepository.existsByCpf(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioEncontrado.setCpf(usuario.getCpf());
        usuarioEncontrado.setNome(usuario.getNome());
        usuarioEncontrado.setEmail(usuario.getEmail());
        usuarioEncontrado.setSenha(usuario.getSenha());
        usuarioEncontrado.setDataNascimento(usuario.getDataNascimento());

        Usuario atualizado = usuarioRepository.save(usuarioEncontrado);
        return ResponseEntity.status(HttpStatus.OK).body(atualizado);
    }
}
