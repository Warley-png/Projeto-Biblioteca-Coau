package com.api.Coau.controller;

import com.api.Coau.model.AluguelLivro;
import com.api.Coau.model.Cliente;
import com.api.Coau.model.ClienteRepository;
import com.api.Coau.model.Livro;
import com.api.Coau.model.LivroRepository;
import com.api.Coau.model.Usuario;
import com.api.Coau.model.aluguelLivroRepository;
import com.api.Coau.model.usuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
//@RequestMapping("/livros")
public class CoauController {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private aluguelLivroRepository aluguelLivroRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping("/")
    public String redirecionaLogin(){
        return "redirect:/livros/telaLogin";
    }

    @GetMapping("/livros/telaLogin")
    public String telaLogin() {
        return "telaLogin";
    }

    @GetMapping("/livros/telaprincipal")
    public String telaPrincipal(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        if ("true".equals(request.getParameter("accessDenied"))) {
            model.addAttribute("erro", "Acesso negado: Você não tem permissão para realizar esta ação.");
        }

        return "telaprincipal";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livros/lista")
    public String listar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        List<Livro> livros = repository.findAll();
        model.addAttribute("livros", livros);
        model.addAttribute("fullFooter", true);
        return "lista";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livros/disponiveis")
    public String listarDisponiveis(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        List<Livro> livros = repository.findByDisponivelTrue();
        model.addAttribute("livros", livros);
        model.addAttribute("mensagem", "Apenas livros disponíveis listados.");
        model.addAttribute("fullFooter", true);
        return "lista-livros";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livros/cadastro-livros")
    public String cadastroForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        System.out.println("Tentando acessar /cadastro-livros");
        model.addAttribute("livro", new Livro());
        model.addAttribute("fullFooter", true);
        return "cadastro-livros";
    }

    
   
    @GetMapping("/livros/cadastroUsuario")
    public String cadastroUsuarioForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("fullFooter", true);
        return "cadastroUsuario";
    }

    
    @PostMapping("/livros/salvarUsuario")
    public String salvarUsuario(@RequestParam String usuario, @RequestParam String login,
            @RequestParam String senha, @RequestParam String perfil,
            RedirectAttributes redirectAttributes) {

        System.out.println("Recebido: usuario=" + usuario + ", login=" + login + ", senha=" + (senha != null ? "[oculta]" : "null") + ", perfil=" + perfil);

        if (usuario == null || usuario.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nome de usuário é obrigatório.");
            return "redirect:/livros/cadastroUsuario";
        }
        if (login == null || login.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Login é obrigatório.");
            return "redirect:/livros/cadastroUsuario";
        }
        if (senha == null || senha.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Senha é obrigatória.");
            return "redirect:/livros/cadastroUsuario";
        }
        if (senha.length() < 6) {
            redirectAttributes.addFlashAttribute("erro", "Senha deve ter pelo menos 6 caracteres.");
            return "redirect:/livros/cadastroUsuario";
        }
        if (perfil == null || perfil.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Perfil é obrigatório.");
            return "redirect:/livros/cadastroUsuario";
        }

        if (usuarioRepository.findByLogin(login).isPresent()) {
            redirectAttributes.addFlashAttribute("erro", "Login já cadastrado.");
            return "redirect:/livros/cadastroUsuario";
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setUsuario(usuario.trim());
        novoUsuario.setLogin(login.trim());
        String senhaHashed = passwordEncoder.encode(senha.trim());
        System.out.println("Senha hashed: " + senhaHashed);
        novoUsuario.setSenha(senhaHashed);
        novoUsuario.setPerfil(perfil);

        try {
            usuarioRepository.save(novoUsuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
            return "redirect:/livros/telaLogin";
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/livros/cadastroUsuario";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/listaUsuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "listaUsuarios";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/resetarSenhaUsuario/{id}")
    public String resetarSenhaUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário inválido:" + id));

            // Define a senha padrão "mudar123" criptografada
            usuario.setSenha(passwordEncoder.encode("mudar123"));
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("mensagem", "Senha do usuário " + usuario.getUsuario() + " resetada para: mudar123");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao resetar senha: " + e.getMessage());
        }
        return "redirect:/livros/listaUsuarios";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/livros/cadastro")
    public String salvar(@Valid @ModelAttribute Livro livro, BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
             System.out.println("ERRO DE VALIDAÇÃO: " + result.getAllErrors());
            model.addAttribute("isAdmin", false);
            model.addAttribute("fullFooter", false);
            return "cadastro-livros";
        }
        repository.save(livro);
        redirectAttributes.addFlashAttribute("mensagem", "Livro cadastrado com sucesso!");
        return "redirect:/livros/listaLivro";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livros/listaLivro")
    public String listarLivros(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        List<Livro> livros = repository.findAll();
        model.addAttribute("livros", livros);
        model.addAttribute("fullFooter", true);
        return "listaLivro";
    }
    // --- MÉTODOS DE EDIÇÃO E EXCLUSÃO DE LIVROS ---

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/livros/editarLivro/{id}")
    public String editarLivro(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        Optional<Livro> livroOpt = repository.findById(id);
        if (livroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Livro não encontrado!");
            return "redirect:/livros/listaLivro";
        }

        model.addAttribute("livro", livroOpt.get());
        model.addAttribute("fullFooter", true);
        return "cadastro-livros"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/excluirLivro/{id}") 
    public String excluirLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            repository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível excluir o livro.");
        }
        return "redirect:/livros/listaLivro";
    }

   
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/cadastroCliente")
    public String cadastroClienteForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("fullFooter", true);
        return "cadastroCliente";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/cliente")
    public String salvarCliente(@Valid @ModelAttribute Cliente cliente, BindingResult result, RedirectAttributes redirectAttributes) {
        if (clienteRepository.findByEmailCliente(cliente.getEmailCliente()).isPresent()) {
            result.rejectValue("emailCliente", "error.cliente", "Email já cadastrado.");
        }
        if (result.hasErrors()) {
            return "cadastroCliente";
        }
        clienteRepository.save(cliente);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
        return "redirect:/livros/cadastroCliente";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/lista-clientes")
    public String listarClientes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        model.addAttribute("fullFooter", true);
        return "lista-clientes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/editarCliente/{id}")
    public String editarCliente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Cliente não encontrado!");
            return "redirect:/livros/cadastroCliente";
        }
        Cliente cliente = clienteOpt.get();
        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("fullFooter", true);
        return "cadastroCliente";
    }

  
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/excluirCliente/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir cliente: ");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/livros/cadastroCliente";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/emprestimo")
    public String telaEmprestimo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        System.out.println("Endpoint GET /livros/emprestimo chamado!");
        List<Livro> livros = repository.findAll();
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("livros", livros);
        model.addAttribute("clientes", clientes);
        model.addAttribute("fullFooter", true);
        System.out.println("Livros carregados: " + livros.size());
        return "emprestimo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/emprestar")
    public String emprestarLivro(@RequestParam Long livroId, @RequestParam Long clienteId, RedirectAttributes redirectAttributes) {
        try {
            Livro livro = repository.findById(livroId)
                    .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            if (!livro.isDisponivel()) {
                redirectAttributes.addFlashAttribute("erro", "Livro não disponível.");
                return "redirect:/livros/emprestimo";
            }
            AluguelLivro aluguel = new AluguelLivro();
            aluguel.setLivro(livro);
            aluguel.setCliente(cliente);
            aluguel.setDataEmprestimo(LocalDate.now());
            aluguel.setStatus("EMPRESTADO");
            aluguelLivroRepository.save(aluguel);
            livro.setDisponivel(false);
            repository.save(livro);
            redirectAttributes.addFlashAttribute("mensagem", "Livro alugado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao alugar livro: " + e.getMessage());
        }
        return "redirect:/livros/emprestimo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/livros/lista-emprestimo")
    public String listarEmprestimos(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated()) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        }
        model.addAttribute("isAdmin", isAdmin);

        List<AluguelLivro> emprestimos = aluguelLivroRepository.findAll();
        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("fullFooter", true);
        return "lista-emprestimo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/livros/devolver/{id}")
    public String devolverLivro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<AluguelLivro> emprestimoOpt = aluguelLivroRepository.findById(id);
        if (emprestimoOpt.isPresent()) {
            AluguelLivro emprestimo = emprestimoOpt.get();

            emprestimo.getLivro().setDisponivel(true);
            repository.save(emprestimo.getLivro());

            aluguelLivroRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagem", "Livro devolvido com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Empréstimo não encontrado!");
        }
        return "redirect:/livros/lista-emprestimo";
    }

}
