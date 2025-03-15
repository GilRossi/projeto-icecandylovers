// Testa se o JS está carregando corretamente
document.addEventListener('DOMContentLoaded', function() {
    console.log('scripts.js carregado!');

    // Configura botão "Adicionar Linha"
    const addBtn = document.getElementById('addIngredienteBtn');
    if (addBtn) {
        addBtn.addEventListener('click', addIngrediente);
        console.log('Botão Adicionar Linha configurado.');
    } else {
        console.error('Botão "Adicionar Linha" não encontrado.');
    }
});

// Função para adicionar uma nova linha de ingrediente
function addIngrediente() {
    console.log('Função addIngrediente chamada.');

    const container = document.getElementById('ingredientes-container');
    if (!container) {
        console.error('Erro: #ingredientes-container não encontrado.');
        return;
    }

    const index = container.children.length;

    const newRow = `
        <div class="ingrediente-item row g-2 mb-2">
            <div class="col-5">
                <select class="form-select" name="ingredientes[${index}].ingredienteId" required>
                    <option value="">Selecione um ingrediente</option>
                    ${allIngredientes?.map(ing => `
                        <option value="${ing.id}">${ing.nome}</option>
                    `).join('') || ''}
                </select>
            </div>
            <div class="col-5">
                <input type="number" step="0.1" class="form-control"
                       name="ingredientes[${index}].quantidade"
                       placeholder="Quantidade" required>
            </div>
            <div class="col-2 d-flex align-items-center">
                <i class="bi bi-x-circle-fill text-danger"
                   style="cursor:pointer; font-size:1.2rem;"
                   onclick="removeIngrediente(this)"></i>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', newRow);
    console.log('Nova linha de ingrediente adicionada.');
}

// Função para remover uma linha de ingrediente
function removeIngrediente(element) {
    element.closest('.ingrediente-item').remove();
    console.log('Linha removida.');
}

function atualizarIngredientesDropdown() {
    const selects = document.querySelectorAll('select[name^="ingredientes"]');
    selects.forEach(select => {
        const selectedValue = select.value; // Mantém a opção já escolhida
        select.innerHTML = '<option value="">Selecione um ingrediente</option>' +
            window.allIngredientes.map(ing =>
                `<option value="${ing.id}" ${ing.id == selectedValue ? 'selected' : ''}>${ing.nome}</option>`
            ).join('');
    });
}

function salvarNovoIngrediente() {
    const nome = document.getElementById('novoIngredienteNome').value;
    const custoPorUnidade = document.getElementById('novoIngredienteCusto').value;
    const unidadeMedida = document.getElementById('novoIngredienteUnidade').value;
    const estoqueInicial = document.getElementById('novoIngredienteEstoque').value;

    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/ingredientes/salvar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ nome, custoPorUnidade, unidadeMedida, estoqueInicial })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            alert('Erro: ' + data.error);
        } else {
            window.allIngredientes.push(data);
            atualizarIngredientesDropdown();
            limparCamposNovoIngrediente();
            if (data.closeModal) {
                fecharModal('#novoIngredienteModal');
            }
        }
    })
    .catch(error => console.error('Erro ao salvar ingrediente:', error));
}

function limparCamposNovoIngrediente() {
    document.getElementById('novoIngredienteNome').value = '';
    document.getElementById('novoIngredienteCusto').value = '';
    document.getElementById('novoIngredienteUnidade').value = 'quilos'; // padrão para quilos
    document.getElementById('novoIngredienteEstoque').value = '';
}

function fecharModal(modalId) {
    const modalElement = document.querySelector(modalId);
    const modalInstance = bootstrap.Modal.getInstance(modalElement);
    modalInstance.hide();
}

document.getElementById('ingredientes-container').addEventListener('input', calcularPrecoCusto);

function calcularPrecoCusto() {
    let totalCusto = 0;

    document.querySelectorAll('#ingredientes-container .ingrediente-item').forEach(item => {
        const select = item.querySelector('select');
        const quantidade = parseFloat(item.querySelector('input[type="number"]').value) || 0;
        const ingrediente = window.allIngredientes.find(ing => ing.id == select.value);

        if (ingrediente && ingrediente.custoPorUnidade && ingrediente.estoqueInicial && ingrediente.unidadeMedida) {
            const estoqueInicial = parseFloat(ingrediente.estoqueInicial) || 1; // Evita divisão por zero
            let custoUnitario = 0;

            switch (ingrediente.unidadeMedida.toLowerCase()) {
                case 'unidades':
                    if (!Number.isInteger(quantidade)) {
                        alert('A quantidade de unidades deve ser um número inteiro.');
                        return;
                    }
                    custoUnitario = parseFloat(ingrediente.custoPorUnidade) / estoqueInicial;
                    break;
                case 'litros':
                    custoUnitario = parseFloat(ingrediente.custoPorUnidade) / estoqueInicial;
                    break;
                case 'quilos':
                    custoUnitario = parseFloat(ingrediente.custoPorUnidade) / estoqueInicial;
                    break;
                default:
                    console.warn(`Unidade de medida desconhecida: ${ingrediente.unidadeMedida}`);
                    return;
            }

            totalCusto += custoUnitario * quantidade;
        }
    });

    document.getElementById('precoCusto').value = totalCusto.toFixed(2);
}

function salvarGeladinho(event) {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    const form = document.getElementById('formCadastroGeladinho');
    const formData = new FormData(form);

    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/geladinhos/salvar', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Exibe a mensagem de sucesso no modal
            const modalBody = document.querySelector('#mensagemModal .modal-body');
            modalBody.textContent = 'Geladinho salvo com sucesso!';

            // Abre o modal
            const mensagemModal = new bootstrap.Modal(document.getElementById('mensagemModal'));
            mensagemModal.show();

            // Opcional: limpa os campos do formulário
            form.reset();
        } else {
            // Exibe a mensagem de erro no modal
            const modalBody = document.querySelector('#mensagemModal .modal-body');
            modalBody.textContent = 'Erro ao salvar o geladinho: ' + data.error;

            // Abre o modal
            const mensagemModal = new bootstrap.Modal(document.getElementById('mensagemModal'));
            mensagemModal.show();
        }
    })
    .catch(error => {
        console.error('Erro ao salvar geladinho:', error);
        // Exibe a mensagem de erro no modal
        const modalBody = document.querySelector('#mensagemModal .modal-body');
        modalBody.textContent = 'Ocorreu um erro inesperado. Tente novamente mais tarde.';

        // Abre o modal
        const mensagemModal = new bootstrap.Modal(document.getElementById('mensagemModal'));
        mensagemModal.show();
    });
}

// Adiciona o evento de submit ao formulário
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formCadastroGeladinho');
    if (form) {
        form.addEventListener('submit', salvarGeladinho);
    }
});

// Função para atualizar as informações na interface
function atualizarInformacoesGeladinho(geladinho) {
    // Suponha que você tenha um elemento onde está mostrando os detalhes do geladinho
    // Por exemplo, um card ou uma tabela
    const detalhesGeladinho = document.getElementById('detalhesGeladinho');

    if (detalhesGeladinho) {
        // Atualiza as informações exibidas
        detalhesGeladinho.innerHTML = `
            <p><strong>Nome:</strong> ${geladinho.nome}</p>
            <p><strong>Preço de Custo:</strong> R$ ${geladinho.precoCusto.toFixed(2)}</p>
            <p><strong>Ingredientes:</strong></p>
            <ul>
                ${geladinho.ingredientes.map(ingrediente => `<li>${ingrediente.nome} - Quantidade: ${ingrediente.quantidade}</li>`).join('')}
            </ul>
        `;
    } else {
        console.error('Elemento para detalhes do geladinho não encontrado.');
    }

    // Se você tiver uma lista de geladinhos, atualize também essa lista
    const listaGeladinhos = document.getElementById('listaGeladinhos');

    if (listaGeladinhos) {
        const novoItem = document.createElement('li');
        novoItem.textContent = `${geladinho.nome} - Preço de Custo: R$ ${geladinho.precoCusto.toFixed(2)}`;
        listaGeladinhos.appendChild(novoItem);
    } else {
        console.error('Lista de geladinhos não encontrada.');
    }
}
