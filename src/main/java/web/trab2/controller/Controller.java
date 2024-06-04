package web.trab2.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import web.trab2.config.Utils;
import web.trab2.model.Aluno;
import web.trab2.model.AlunoDto;
import web.trab2.repository.AlunoRepository;

@RestController
public class Controller {

    @Autowired
    private AlunoRepository repository;

    Logger logger = LoggerFactory.getLogger(Controller.class);

    // buscar aliunos por id no parametro da url
    @GetMapping("/getAluno/{id}")
    public ResponseEntity<Object> getAluno(@PathVariable Long id) {
        this.logger.info("Buscando aluno por id");
        Optional<Aluno> aluno = this.repository.findById(id);
        if (aluno.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(aluno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há aluno com este id");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ArrayList<Aluno>> getAll() {
        ArrayList<Aluno> al = this.repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(al);
    }

    @PostMapping("/updateAluno")
    public ResponseEntity<Object> updateAluno(@RequestBody AlunoDto dto) {
        Optional<Aluno> aluno;
        this.logger.info("Atualizando aluno");
        this.logger.info(dto.toString());
        if (dto.id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id não informado");
        } else {
            if (Integer.parseInt(dto.turma) > 2 || Integer.parseInt(dto.turma) < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Turma inválida");
            }
            aluno = this.repository.findById(Long.parseLong(dto.id));
            if (aluno.isPresent()) {
                Aluno a = aluno.get();
                a.setNome(dto.nome);
                a.setTurma(Integer.parseInt(dto.turma));
                a.setMatricula(dto.matricula);
                a.setNota(Double.parseDouble(dto.nota));
                this.repository.save(a);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há aluno com este id");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("/novoAluno")
    public ResponseEntity<Object> novoAluno(@RequestBody AlunoDto dto) {
        this.logger.info("Criando novo aluno");
        this.logger.info(dto.toString());
        if (this.repository.countAlunoByTurma(Integer.parseInt(dto.turma)) >= 10) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Dados em excesso");
        }
        if (dto.nome == null || dto.turma == null || dto.matricula == null || dto.nota == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados inválidos");
        }
        Aluno a = new Aluno();
        a.setNome(dto.nome);
        a.setTurma(Integer.parseInt(dto.turma));
        a.setMatricula(dto.matricula);
        a.setNota(Double.parseDouble(dto.nota));
        this.repository.save(a);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteAluno(@RequestBody String id) {
        Long alunoId = Long.parseLong(id);
        this.repository.deleteById(alunoId);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("/reset")
    public ResponseEntity<Object> reset() {
        this.repository.deleteAll();
        Utils.startDb(this.repository);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("/zerar")
    public ResponseEntity<Object> zerar() {
        this.repository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
