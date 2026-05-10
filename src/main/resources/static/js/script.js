// Funções gerais
function confirmarSair() {
    if (confirm('Tem certeza que deseja sair?')) {
        window.location.href = '/logout';  // Redireciona para logout (Spring Security cuida)
    }
}

function atualizarData() {
    const now = new Date();
    const dataElement = document.getElementById('data');
    if (dataElement) {
        dataElement.textContent = now.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
    }
}
setInterval(atualizarData, 60000);
atualizarData();

document.addEventListener('DOMContentLoaded', function () {
    console.log('JS carregado: Verificando página específica.');

    // Configuração para página de empréstimo (se aplicável)
    const inputPesquisaLivros = document.getElementById('pesquisaLivros');
    const btnPesquisarLivros = document.getElementById('btnPesquisarLivros');
    const btnAlugar = document.getElementById('btnAlugar');
    const formEmprestimo = document.getElementById('formEmprestimo');
    const tblLivros = document.getElementById('tblLivros');

    if (inputPesquisaLivros && btnPesquisarLivros && btnAlugar && formEmprestimo && tblLivros) {
        console.log('Página de empréstimo detectada: Configurando pesquisa e empréstimo.');
        const linhasLivros = tblLivros.querySelectorAll('tbody tr');

        function filtrarLivros() {
            const termo = inputPesquisaLivros.value.toLowerCase().trim();
            linhasLivros.forEach(linha => {
                const titulo = linha.cells[2]?.textContent.toLowerCase() || '';
                linha.style.display = titulo.includes(termo) ? '' : 'none';
            });
        }

        btnPesquisarLivros.addEventListener('click', filtrarLivros);
        inputPesquisaLivros.addEventListener('keyup', filtrarLivros);

        btnAlugar.addEventListener('click', function () {
            const livroSelecionado = document.querySelector('input[name="livroSelecionado"]:checked');
            const clienteSelecionado = document.querySelector('input[name="clienteSelecionado"]:checked');

            if (!livroSelecionado || !clienteSelecionado) {
                alert('Selecione um livro e um cliente para alugar.');
                return;
            }

            document.getElementById('livroId').value = livroSelecionado.value;
            document.getElementById('clienteId').value = clienteSelecionado.value;
            formEmprestimo.submit();
        });

        console.log('Configuração de empréstimo concluída.');
    } else {
        console.log('Página não é de empréstimo: Pulando configuração específica.');
    }

    // Configuração para páginas com pesquisa geral (livros ou clientes)
    const inputPesquisa = document.getElementById('pesquisa');
    const btnPesquisar = document.getElementById('btnPesquisar');
    const btnLimparPesquisa = document.getElementById('btnLimparPesquisa');
    const tblLivrosGeral = document.getElementById('tblLivros');
    const tblClientes = document.getElementById('tblclientes');

    if (inputPesquisa && btnPesquisar) {
        console.log('Página com pesquisa geral detectada.');
        const tabela = tblLivrosGeral || tblClientes;
        if (tabela) {
            const linhas = tabela.querySelectorAll('tbody tr');

            function filtrarTabela() {
                const termo = inputPesquisa.value.toLowerCase().trim();
                linhas.forEach(linha => {
                    let match = false;
                    if (tblLivrosGeral) {
                        const titulo = linha.cells[1]?.textContent.toLowerCase() || '';
                        const autor = linha.cells[2]?.textContent.toLowerCase() || '';
                        match = titulo.includes(termo) || autor.includes(termo);
                    } else if (tblClientes) {
                        const nome = linha.cells[1]?.textContent.toLowerCase() || '';
                        const email = linha.cells[2]?.textContent.toLowerCase() || '';
                        const telefone = linha.cells[3]?.textContent.toLowerCase() || '';
                        match = nome.includes(termo) || email.includes(termo) || telefone.includes(termo);
                    }
                    linha.style.display = match ? '' : 'none';
                });
            }

            function limparPesquisa() {
                inputPesquisa.value = '';
                linhas.forEach(linha => linha.style.display = '');
            }

            btnPesquisar.addEventListener('click', filtrarTabela);
            inputPesquisa.addEventListener('keyup', filtrarTabela);

            if (btnLimparPesquisa) {
                btnLimparPesquisa.addEventListener('click', limparPesquisa);
            }
        }
    }

    // Configuração simples para formulário de login (validação básica, submit padrão)
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;

            // Validação simples (opcional, para UX)
            if (!username || !password) {
                alert('Por favor, preencha login e senha!');
                event.preventDefault();  // Impede submit se inválido
                return;
            }

            
        });
    }
});

   