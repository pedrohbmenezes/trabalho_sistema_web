package web.trab2.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import web.trab2.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    ArrayList<Aluno> findAll();

    int countAlunoByTurma(int turma);

}
