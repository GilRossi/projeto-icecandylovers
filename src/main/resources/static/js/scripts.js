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
function salvarNovoIngrediente() {
    const nome = document.getElementById('novoIngredienteNome').value;
    const custo = document.getElementById('novoIngredienteCusto').value;
    const unidade = document.getElementById('novoIngredienteUnidade').value;
    const estoque = document.getElementById('novoIngredienteEstoque').value;

    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    fetch('/ingredientes/salvar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({ nome, custo, unidade, estoque })
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            window.allIngredientes.push(data); // Atualiza a lista global
            atualizarIngredientesDropdown();
            fecharModal('#novoIngredienteModal');
        } else {
            console.error('Erro ao salvar ingrediente:', data);
        }
    })
    .catch(error => console.error('Erro:', error));
}

function atualizarIngredientesDropdown() {
    const selects = document.querySelectorAll('select[name^="ingredientes"]');
    selects.forEach(select => {
        select.innerHTML = '<option value="">Selecione um ingrediente</option>' +
            window.allIngredientes.map(ing =>
                `<option value="${ing.id}">${ing.nome}</option>`
            ).join('');
    });
}

function fecharModal(modalId) {
    const modal = bootstrap.Modal.getInstance(document.querySelector(modalId));
    modal.hide();
}

document.getElementById('ingredientes-container').addEventListener('input', calcularPrecoCusto);

function calcularPrecoCusto() {
    let totalCusto = 0;

    document.querySelectorAll('#ingredientes-container .ingrediente-item').forEach(item => {
        const select = item.querySelector('select');
        const quantidade = parseFloat(item.querySelector('input[type="number"]').value) || 0;
        const ingrediente = window.allIngredientes.find(ing => ing.id == select.value);

        if (ingrediente && ingrediente.custoPorUnidade) {
            totalCusto += quantidade * parseFloat(ingrediente.custoPorUnidade);
        }
    });

    document.getElementById('precoCusto').value = totalCusto.toFixed(2);
}
