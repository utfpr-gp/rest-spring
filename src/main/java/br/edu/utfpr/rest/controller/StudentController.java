/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.controller;

import br.edu.utfpr.rest.model.Student;
import br.edu.utfpr.rest.model.dto.StudentDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import br.edu.utfpr.rest.model.repository.StudentRepository;
import br.edu.utfpr.rest.service.StudentService;
import br.edu.utfpr.rest.util.Response;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ronifabio
 */
@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class StudentController {

    public static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Value("${pagina.quantidade}")
    private int paginationAmount;

    /**
     *
     * Inicializa a base de dados com alguns estudantes
     */
    @GetMapping(value = "/inicializa")
    public void init() {
        studentService.init();
    }

    /**
     * 
     * Não é boa prática! Demonstração do retorno de uma entidade de banco de dados.
     * Também não é boa prática o uso do Repository no Controller.
     * 
     * @return
     */
    @RequestMapping(method = GET)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * 
     * Busca com atributos de paginação e retorno de uma lista de DTOs Uso:
     * /api/alunos/paginacao-dto?pag=1,ord=name Se os parâmetros de query não forem
     * passados, os valores default serão usados. O ResponseEntity carrega o código
     * de status. O Response é usado para padronizar as respostas.
     * 
     * @param page
     * @param order
     * @param direction
     * @return
     */
    @GetMapping(value = "/paginacao-dto")
    public ResponseEntity<Response<List<StudentDTO>>> findAllPagination(
            @RequestParam(value = "pag", defaultValue = "0") int page,
            @RequestParam(value = "ord", defaultValue = "name") String order,
            @RequestParam(value = "dir", defaultValue = "ASC") String direction) {

        log.info("Buscando alunos ordenador por {}, página {}", order, page);

        Response<List<StudentDTO>> response = new Response<>();
        PageRequest pageRequest = PageRequest.of(page, this.paginationAmount, Sort.Direction.valueOf(direction), order);

        Page<Student> students = this.studentRepository.findAll(pageRequest);
        Page<StudentDTO> studentDTOs = students.map(s -> new StudentDTO(s));
        response.setData(studentDTOs.getContent());
        return ResponseEntity.ok(response);
    }

    /**
     * 
     * Busca com parâmetros de paginação e retorno de uma lista de Page Uso:
     * /api/alunos/paginacao-page?pag=1,ord=name Se os parâmetros de query não forem
     * passados, os valores default serão usados. Um Page possui informações
     * adicionais sobre a paginação, tal como o total de registros e se há uma
     * próxima página, entre outros.
     * 
     * @param page
     * @param order
     * @param direction
     * @return
     */
    @GetMapping(value = "/paginacao-page")
    public ResponseEntity<Response<Page<StudentDTO>>> findAllPaginationWithPage(
            @RequestParam(value = "pag", defaultValue = "0") int page,
            @RequestParam(value = "ord", defaultValue = "name") String order,
            @RequestParam(value = "dir", defaultValue = "ASC") String direction) {

        log.info("Buscando alunos ordenador por {}, página {}", order, page);

        Response<Page<StudentDTO>> response = new Response<>();
        PageRequest pageRequest = PageRequest.of(page, this.paginationAmount, Sort.Direction.valueOf(direction), order);

        Page<Student> students = this.studentRepository.findAll(pageRequest);
        Page<StudentDTO> studentDTOs = students.map(s -> new StudentDTO(s));
        response.setData(studentDTOs);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * Trata de variáveis de caminho Uso: api/alunos/cpf/00022233344 Retorna um
     * StudentDTO pelo seu CPF.
     * 
     * @param cpf
     * @return
     */
    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<Response<StudentDTO>> findByCPF(@PathVariable String cpf) {

        Response<StudentDTO> response = new Response<>();

        Student s = studentService.findByCPF(cpf);
        if (s == null) {
            response.addError("Aluno não encontrado.");
            return ResponseEntity.badRequest().body(response);
        }

        StudentDTO dto = new StudentDTO(s);
        response.setData(dto);
        log.info("estudante por cpf {}", s);

        return ResponseEntity.ok(response);
    }

    /**
     *
     * Trata de parâmetros de query Uso: api/alunos/cpf?numero=00000000000 Retorna
     * um StudentDTO pelo seu CPF. Não deve ser usado como exemplo em sua completudo
     * porque não está usando o Response e ResponseEntity para encapsular o DTO.
     * Exemplo apenas demonstrativo de retorno sem Response, também usado em
     * aplicações. O ResponseEntity carrega o código de status.
     * 
     * @param cpf
     * @return
     */
    @GetMapping(value = "/cpf")
    public StudentDTO findByCPFParam(@RequestParam("numero") String cpf) {
        Student s = studentService.findByCPF(cpf);
        StudentDTO dto = new StudentDTO(s);
        return dto;
    }

    @RequestMapping(method = GET, value = "/curso/{course}")
    public List<Student> listByCourse(@PathVariable String course) {
        return studentRepository.findByCourse(course);
    }

    @RequestMapping(method = GET, value = "/sobrenome/{sobrenome}")
    public List<Student> listByNameEndWith(@PathVariable String sobrenome) {
        return studentRepository.findByNameEndsWith(sobrenome);
    }

    /**
     * Busca um aluno pela matrícula. Uso: /api/alunos/2
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<Response<StudentDTO>> get(@PathVariable Long id) {
        Response<StudentDTO> response = new Response<>();

        Student student = studentRepository.getOne(id);
        if (student == null) {
            response.addError("Aluno não encontrado para a matrícula " + id);
            return ResponseEntity.badRequest().body(response);

        }

        StudentDTO studentDTO = new StudentDTO(student);
        response.setData(studentDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * 
     * Atualiza os dados de um aluno. Recebe o id do aluno pela URL, típico de uma
     * URL de edição. Recebe os dados do aluno em um DTO, este com atributos
     * anotados com restrições de validação do BeanValidation Para que tais
     * anotações de validação sejam analisadas, usa-se @Valid O @RequestBody é usado
     * para converter o JSON para o objeto StudentDTO O BindingResult é passado como
     * argumento pelo Spring contendo as informações sobre a validação do DTO.
     * 
     * @param id
     * @param dto
     * @param result
     * @return ResponseEntity<?>, notação mais abstrata.
     */
    // @RequestMapping(value = "/{id}", method = PUT)
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody StudentDTO dto, BindingResult result) {

        Response<StudentDTO> response = new Response<>();
        if (result.hasErrors()) {
            response.setErrors(result);
            return ResponseEntity.badRequest().body(response);
        }

        // busca pelo aluno
        Optional<Student> o = studentRepository.findById(id);
        if (!o.isPresent()) {
            response.addError("Aluno não encontrado pela matrícula");
            return ResponseEntity.badRequest().body(response);
        }
        Student s = o.get();

        // impede atualizar para um CPF já cadastrado
        if (!s.getCpf().equals(dto.getCpf())) {
            if (this.studentRepository.findByCpf(dto.getCpf()) != null) {
                response.addError("Não é possível atualizar o CPF, já está sendo usado.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // proíbe atualizar a matrícula
        if (!s.getRegistration().equals(dto.getRegistration())) {
            response.addError("Não é possível atualizar o número de matrícula.");
            return ResponseEntity.badRequest().body(response);
        }

        // atualiza os dados atribuindo os atributos not null de dto
        s.update(dto);
        s = studentService.save(s);

        // prepara a resposta
        dto = new StudentDTO(s);
        response.setData(dto);

        return ResponseEntity.ok(response);
    }

    /**
     * 
     * Cadastra um aluno. Os dados do aluno são recebidos em um DTO com atributos de
     * validação.
     * 
     * @Valid valida os atributos e @RequestBody converte de JSON
     * @param dto
     * @param result
     * @return ResponseEntity<Response<StudentDTO>>, retorno mais específico
     */
    // @RequestMapping(value = "/{id}", method = POST)
    @PostMapping
    public ResponseEntity<Response<StudentDTO>> post(@Valid @RequestBody StudentDTO dto, BindingResult result) {

        Response<StudentDTO> response = new Response<>();
        if (result.hasErrors()) {
            response.setErrors(result);
            return ResponseEntity.badRequest().body(response);
        }

        // impede usar post para aluno já cadastrado
        if (dto.getRegistration() != null) {
            Optional<Student> o = studentRepository.findById(dto.getRegistration());
            if (o.isPresent()) {
                response.addError("Aluno já cadastrado.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // persiste o aluno
        Student s = new Student(dto);
        try {
            s = studentService.save(s);
        } catch (Exception e) {
            // trata de problemas gerados por não respeito às constraints do banco - CPF
            // único
            response.addError("Houve um erro ao persistir os seus dados.");
            return ResponseEntity.badRequest().body(response);
        }

        // prepara a resposta
        dto = new StudentDTO(s);
        response.setData(dto);

        return ResponseEntity.ok(response);
    }

    // @RequestMapping(value = "/{id}", method = DELETE)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable Long id) {
        log.info("Removendo aluno com id {}", id);

        Response<String> response = new Response<>();
        Optional<Student> o = studentRepository.findById(id);

        if (!o.isPresent()) {
            log.info("Erro ao remover");
            response.addError("Erro ao remover, registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        this.studentRepository.deleteById(id);
        return ResponseEntity.ok(response);
    }
}
