/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.rest.controller;

import br.edu.utfpr.rest.model.entity.Student;
import br.edu.utfpr.rest.model.dto.StudentDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import br.edu.utfpr.rest.model.repository.StudentRepository;
import br.edu.utfpr.rest.model.service.StudentService;
import br.edu.utfpr.rest.util.Response;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;

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
     * <p>Não é boa prática! </p>
     * <p>Demonstração do retorno de uma entidade de banco de dados.</p>
     * <p>Também não é boa prática o uso do Repository no Controller.</p>
     * 
     * @return
     */
    @RequestMapping(method = GET)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * 
     * <p>Busca com atributos de paginação e retorno de uma lista de DTOs. </p> 
     * <p>Uso: <pre>/api/alunos/paginacao-dto?pag=1&ord=name</pre> </p>
     * <p>Se os parâmetros de query não forem passados, os valores default serão usados. </p>
     * <p>O ResponseEntity carrega o código de status. O Response é usado para padronizar as respostas.</p>
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
     * <p>Busca com parâmetros de paginação e retorno de uma lista de Page. </p> 
     * <p>Uso: <pre>
     * /api/alunos/paginacao-page?pag=1&ord=name</pre></p> 
     * <p>Se os parâmetros de query não forem
     * passados, os valores default serão usados. </p>
     * <p>Um Page possui informações
     * adicionais sobre a paginação, tal como o total de registros e se há uma
     * próxima página, entre outros.</p>
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
     * <p>Busca com paginação recebendo como argumento um Pageable.</p>
     * <p>@PageableDefault é opcional, serve para definir parâmetros default.</p>
     * <p>Uso: <pre>/api/alunos/paginacao-pageable?page=0&size=10</pre></p>
     * 
     * @param pageable
     * @return
     */
    @GetMapping(value = "/paginacao-pageable")
    public ResponseEntity<Response<Page<StudentDTO>>> findAllPaginationWithPageable(
    		@PageableDefault(page=0, size=3, direction = Direction.ASC) Pageable pageable) {        

        Response<Page<StudentDTO>> response = new Response<>();       

        Page<Student> students = this.studentRepository.findAll(pageable);
        Page<StudentDTO> studentDTOs = students.map(s -> new StudentDTO(s));
        response.setData(studentDTOs);
        return ResponseEntity.ok(response);
    }   
    

    /**
     *
     * <p>Trata de variáveis de caminho.</p> 
     * <p>Uso: <pre>api/alunos/cpf/00022233344 </pre></p> 
     * <p>Retorna um StudentDTO pelo seu CPF.</p>
     * 
     * @param cpf
     * @return
     */
    @GetMapping(value = "/cpf/{cpf}")
    public ResponseEntity<Response<StudentDTO>> findByCPF(@PathVariable String cpf) {

        Response<StudentDTO> response = new Response<>();

        Student s = studentService.findByCPF(cpf);
        if (s == null) {
            //response.addError("Aluno não encontrado.");
            throw new EntityNotFoundException();
            //return ResponseEntity.badRequest().body(response);
        }

        StudentDTO dto = new StudentDTO(s);
        response.setData(dto);
        log.info("estudante por cpf {}", s);

        return ResponseEntity.ok(response);
    }

    /**
     *
     * <p>Trata de parâmetros de query.</p> 
     * <p>Uso: api/alunos/cpf?numero=00000000000 </p> 
     * <p> Retorna um StudentDTO pelo seu CPF. </p>
     * <p> Não deve ser usado como exemplo em sua completude
     * porque não está usando o Response e ResponseEntity para encapsular o DTO.</p>
     * <p>Exemplo apenas demonstrativo de retorno sem Response, também usado em
     * aplicações. O ResponseEntity carrega o código de status.</p>
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
     * Busca um aluno pela matrícula. 
     * <p>Uso: <pre>/api/alunos/2</pre></p>
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
     * <p>Atualiza os dados de um aluno. </p>
     * <p>Recebe o id do aluno pela URL, típico de uma URL de edição.</p>     * 
     * <p>Recebe os dados do aluno em um DTO, este com atributos
     * anotados com restrições de validação do BeanValidation. </p>
     * <p> Para que tais anotações de validação sejam analisadas, usa-se @Valid.</p> 
     * <p> O @RequestBody é usado para converter o JSON para o objeto StudentDTO. </p> 
     * <p> O BindingResult é passado como argumento pelo Spring contendo as informações sobre a validação do DTO.</p>
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
        // NÃO trata de problemas gerados por não respeito às constraints do banco - CPF
        // único - o erro será DataIntegrityViolationException e será tratado por GlobalExceptionHandler
        Student s = new Student(dto);
        s = studentService.save(s);
        
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


    /**
     *
     * Exemplo de tratamento de exception no Controller.
     * É uma opção mais específica e portanto com maior prioridade do que
     * se tratado de forma global na classe <code>GlobalExceptionHandler</code>
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleException(DataIntegrityViolationException exception,
                                             HttpServletRequest request) {
        log.error("Error in process request: " + request.getRequestURL() + " cause by: "
                + exception.getClass().getSimpleName());
        Response response = new Response();
        response.addError("Oppss!!! Ocorreu um erro de restrições no banco de dados.");

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
