// *******************************************************************************
// ***  SOMENTE A PARTIR DESTE PONTO VOCÊ PODERÁ ALTERAR O CÓDIGO
// *******************************************************************************


buscarAluno = function () {
    const id = window.location.pathname.split('/')[2];
    let request = new XMLHttpRequest();
    request.open('GET', '/getAluno/' + id, true);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                let aluno = JSON.parse(request.responseText);
                document.getElementById('turma').value = aluno.turma;
                document.getElementById('nome').value = aluno.nome;
                document.getElementById('matricula').value = aluno.matricula;
                document.getElementById('nota').value = aluno.nota;
            } else {
                window.alert('Erro ao buscar aluno');
            }
        }
    }
    request.send();
}

enviaEdit = function () {
    let id = window.location.pathname.split('/')[2];
    let dto = {
        id: id,
        turma: document.getElementById('turma').value,
        nome: document.getElementById('nome').value,
        matricula: document.getElementById('matricula').value,
        nota: document.getElementById('nota').value
    };
    let json = JSON.stringify(dto);
    let request = new XMLHttpRequest();
    request.open('POST', '/updateAluno', true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                window.location.href = '/';
            } else {
                window.alert(request.responseText)
            }
        }
    }
    request.send(json);
    console.log(json);
}

deleteAluno = function () {
    let id = window.location.pathname.split('/')[2];
    let request = new XMLHttpRequest();
    request.open('POST', '/delete', true);
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 200) {
                window.location.href = '/';
            } else {
                window.alert('Erro ao deletar aluno');
            }
        }
    }
    request.send(id);
}

buscarAluno()