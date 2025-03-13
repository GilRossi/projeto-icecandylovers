// Preços do gás (R$/kg)
const PRECOS_GAS = {
    GN: 5.00,
    GLP: 3.50
};

// Função para alternar campos de gás
function toggleCamposGas() {
    const tipoGas = document.getElementById('tipoGas').value;
    const taxaGasInput = document.getElementById('taxaGas');
    const camposConversao = document.querySelectorAll('#kcal, #mj, #kwh');
    const precoGasGN = document.getElementById('precoGasGN');
    const precoGasGLP = document.getElementById('precoGasGLP');

    // Habilita/desabilita campos de energia
    camposConversao.forEach(campo => {
        campo.disabled = (tipoGas === "");
        campo.readOnly = false;
    });

    // Resetar exibição de preços
    precoGasGN.classList.add('d-none');
    precoGasGLP.classList.add('d-none');

    // Define o preço padrão se o campo estiver vazio
    if (taxaGasInput.value === "") {
        if (tipoGas === "GN") {
            taxaGasInput.value = PRECOS_GAS.GN;
        } else if (tipoGas === "GLP") {
            taxaGasInput.value = PRECOS_GAS.GLP;
        }
    }

    // Atualiza badge e valor do gás
    if (tipoGas === "GN") {
        precoGasGN.classList.remove('d-none');
    } else if (tipoGas === "GLP") {
        precoGasGLP.classList.remove('d-none');
    } else {
        taxaGasInput.value = "";
        camposConversao.forEach(campo => campo.value = "");
    }

    // Atualiza cálculos
    calcularKwhKg();
}

// Conversão entre kcal, MJ e kWh
function converterEnergia(event) {
    const input = event.target;
    const valor = parseFloat(input.value) || 0;
    let kcal, mj, kwh;

    // Fatores de conversão
    const KCAL_TO_MJ = 0.004184;
    const KCAL_TO_KWH = 0.00116222;
    const MJ_TO_KWH = 0.277778;

    if (input.id === 'kcal') {
        kcal = valor;
        mj = kcal * KCAL_TO_MJ;
        kwh = kcal * KCAL_TO_KWH;
    } else if (input.id === 'mj') {
        mj = valor;
        kcal = mj / KCAL_TO_MJ;
        kwh = mj * MJ_TO_KWH;
    } else if (input.id === 'kwh') {
        kwh = valor;
        kcal = kwh / KCAL_TO_KWH;
        mj = kwh / MJ_TO_KWH;
    }

    // Atualiza campos (sem disparar eventos)
    document.getElementById('kcal').value = kcal?.toFixed(4) || '';
    document.getElementById('mj').value = mj?.toFixed(4) || '';
    document.getElementById('kwh').value = kwh?.toFixed(4) || '';

    // Força atualização do cálculo de kWh/Kg
    calcularKwhKg();
}

// Atualiza cálculos de kWh/Kg
function calcularKwhKg() {
    const kwh = parseFloat(document.getElementById('kwh').value) || 0;
    const quadrichama = parseFloat(document.getElementById('quadrichama').value) || 0;
    const rapido = parseFloat(document.getElementById('rapido').value) || 0;
    const semirapido = parseFloat(document.getElementById('semirapido').value) || 0;
}

// Mapeia o valor unitário calculado no momento do cadastro do ingrediente
const valorPorUnidade = {}; // Ex: { ingredienteId: custoPorUnidade / estoqueInicial }

// Atualização da função para adicionar ingredientes e recalcular custo
function addIngrediente() {
    const container = document.getElementById('ingredientes-container');
    const index = container.children.length;

    const newIngrediente = `
        <div class="ingrediente-item row g-2 mb-2">
            <div class="col-4">
                <select class="form-select" name="ingredientes[${index}].ingredienteId" required onchange="atualizarUnidadeMedida(this); calcularPrecoCusto()">
                    <option value="">Selecione um ingrediente</option>
                    ${allIngredientes.map(ing => `
                        <option value="${ing.id}" data-custo="${ing.custoPorUnidade}" data-unidade="${ing.unidadeMedida}" data-quantidade-total="${ing.quantidadeTotal}">${ing.nome}</option>
                    `).join('')}
                </select>
            </div>
            <div class="col-4">
                <input type="number" step="0.1" class="form-control"
                       name="ingredientes[${index}].quantidade"
                       placeholder="Quantidade" required
                       onchange="calcularPrecoCusto()">
            </div>
            <div class="col-4">
                <input type="text" class="form-control unidadeMedida" name="ingredientes[${index}].unidadeMedida" readonly>
            </div>
            <div class="col-2">
                <i class="bi bi-x-circle-fill remove-ingrediente"
                   onclick="removeIngrediente(this); calcularPrecoCusto();"></i>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', newIngrediente);
}

// Atualiza a unidade de medida ao selecionar um ingrediente
function atualizarUnidadeMedida(selectElement) {
    const unidadeMedida = selectElement.options[selectElement.selectedIndex]?.getAttribute('data-unidade');
    const unidadeInput = selectElement.closest('.ingrediente-item').querySelector('.unidadeMedida');
    if (unidadeInput) {
        unidadeInput.value = unidadeMedida;
    }
}

// Função para formatar valor monetário em reais
function formatarParaReais(valor) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
}

// Função para limpar a formatação monetária para envio ao backend
function limparFormatacaoReais(valorFormatado) {
    return parseFloat(valorFormatado.replace(/[^0-9,]/g, '').replace(',', '.')) || 0;
}

// Função para calcular preço por unidade ao cadastrar ingredientes
function cadastrarIngrediente(id, custoPorUnidade, estoqueInicial) {
    if (estoqueInicial > 0) {
        valorPorUnidade[id] = custoPorUnidade / estoqueInicial;
    } else {
        valorPorUnidade[id] = 0;
    }
}

// Função para calcular o preço de custo em tempo real considerando ingredientes e taxas
function calcularPrecoCusto() {
    const ingredientes = document.querySelectorAll('#ingredientes-container .ingrediente-item');
    let precoCustoTotal = 0;

    ingredientes.forEach(item => {
        const select = item.querySelector('select[name^="ingredientes"][name$=".ingredienteId"]');
        const quantidadeInput = item.querySelector('input[name^="ingredientes"][name$=".quantidade"]');
        const unidadeInput = item.querySelector('input[name^="ingredientes"][name$=".unidadeMedida"]');

        if (select && quantidadeInput && unidadeInput) {
            const ingredienteId = select.value;
            const quantidade = parseFloat(quantidadeInput.value) || 0;
            const unidadeMedida = unidadeInput.value;
            const valorUnitario = valorPorUnidade[ingredienteId] || 0;

            let custoCalculado = 0;
            if (unidadeMedida === 'unidade') {
                custoCalculado = quantidade * valorUnitario;
            } else if (unidadeMedida === 'grama') {
                custoCalculado = (quantidade / 1000) * valorUnitario;
            } else {
                custoCalculado = quantidade * valorUnitario;
            }

            precoCustoTotal += custoCalculado;
        }
    });

    // Adicionar taxas
    const taxaAgua = parseFloat(document.getElementById('taxaAgua')?.value) || 0;
    const taxaGas = parseFloat(document.getElementById('taxaGas')?.value) || 0;
    const taxaEnergia = parseFloat(document.getElementById('taxaEnergia')?.value) || 0;

    precoCustoTotal += taxaAgua + taxaGas + taxaEnergia;

    // Atualiza o campo "precoCusto" formatado para exibição
    const precoCustoField = document.getElementById('precoCusto');
    if (precoCustoField) {
        precoCustoField.value = formatarParaReais(precoCustoTotal);
        precoCustoField.setAttribute('data-raw-value', precoCustoTotal.toFixed(2)); // Valor sem formatação
    } else {
        console.error('Campo precoCusto não encontrado!');
    }
}

// Antes de enviar o formulário, converter valor formatado para número puro
document.querySelector('form').addEventListener('submit', function() {
    const precoCustoField = document.getElementById('precoCusto');
    if (precoCustoField) {
        precoCustoField.value = limparFormatacaoReais(precoCustoField.value);
    }
});

// Função para remover ingrediente e recalcular custo
function removeIngrediente(element) {
    element.closest('.ingrediente-item').remove();
    calcularPrecoCusto();
}


// Função para calcular o custo total
function calcularTotalCusto() {
    let total = 0;

    // Custo dos ingredientes
    document.querySelectorAll('.ingrediente-item').forEach(row => {
        const custoUnitario = parseFloat(row.querySelector('option:checked')?.dataset.custo || 0);
        const quantidade = parseFloat(row.querySelector('input[type="number"]').value) || 0;
        if (!isNaN(custoUnitario) && !isNaN(quantidade)) {
            total += custoUnitario * quantidade;
        }
    });

    // Custo da água
    const fonteAgua = document.querySelector("[name='fonteAgua']").value;
    const taxaAgua = parseFloat(document.getElementById("taxaAgua").value) || 0;

    if (fonteAgua === "GALAO") {
        const quantidadeGaloes = parseFloat(document.querySelector("[name='quantidadeGaloes']").value) || 0;
        total += quantidadeGaloes * taxaAgua;
    } else if (fonteAgua === "TORNEIRA") {
        const metrosCubicosAgua = parseFloat(document.querySelector("[name='metrosCubicosAgua']").value) || 0;
        total += metrosCubicosAgua * taxaAgua;
    }

    // Custo do gás
    const horasGas = parseFloat(document.querySelector("[name='horasGas']").value) || 0;
    const taxaGas = parseFloat(document.getElementById("taxaGas").value) || 0;
    total += horasGas * taxaGas;

    // Custo da energia (kWh)
    const kwh = parseFloat(document.getElementById('kwh').value) || 0;
    const taxaEnergia = parseFloat(document.getElementById("taxaEnergia").value) || 0;
    total += kwh * taxaEnergia;

    // Atualiza o campo de Preço de Custo
    const precoCustoInput = document.getElementById('precoCusto');
    precoCustoInput.value = total.toFixed(2); // Garante que o valor seja um número válido
    console.log("Preço de Custo calculado:", total.toFixed(2));

    // Feedback visual (opcional)
    precoCustoInput.classList.add('highlight');
    setTimeout(() => precoCustoInput.classList.remove('highlight'), 1000);
}

// Função para remover ingredientes
function removeIngrediente(element) {
    element.closest('.ingrediente-item').remove();
    calcularTotalCusto();
}

// Função para atualizar o custo de um ingrediente
function updateCusto(element) {
    const row = element.closest('.ingrediente-item');
    const custoUnitario = parseFloat(row.querySelector('option:checked')?.dataset.custo || 0);
    const quantidade = parseFloat(element.value) || 0;
    row.dataset.custo = (custoUnitario * quantidade).toFixed(2);
    calcularTotalCusto();
}

// Função para alternar campos de água
function toggleCamposAgua() {
    const fonteAgua = document.querySelector("[name='fonteAgua']").value;
    document.getElementById("aguaGalaoCampos").classList.toggle("d-none", fonteAgua !== "GALAO");
    document.getElementById("aguaTorneiraCampos").classList.toggle("d-none", fonteAgua !== "TORNEIRA");
}

// Função para calcular o custo da água
function calcularCustoAgua() {
    let custoAgua = 0;
    const taxaAgua = parseFloat(document.getElementById("taxaAgua").value) || 0;

    if (document.getElementById("aguaGalaoCheck").checked) {
        const quantidadeGaloes = parseFloat(document.querySelector("[name='quantidadeGaloes']").value) || 0;
        custoAgua += quantidadeGaloes * taxaAgua;
    }

    if (document.getElementById("aguaTorneiraCheck").checked) {
        const metrosCubicos = parseFloat(document.querySelector("[name='metrosCubicosAgua']").value) || 0;
        custoAgua += metrosCubicos * taxaAgua;
    }

    // Chama o cálculo total
    calcularTotalCusto();
}

// Função para salvar novo ingrediente
function salvarNovoIngrediente() {
    const nome = document.getElementById('novoIngredienteNome').value;
    const custo = parseFloat(document.getElementById('novoIngredienteCusto').value);
    const estoque = parseFloat(document.getElementById('novoIngredienteEstoque').value);
    const unidade = document.getElementById('novoIngredienteUnidade').value;

    if (!nome || isNaN(custo) || isNaN(estoque) || !unidade) {
        alert('Preencha todos os campos corretamente!');
        return;
    }

    const saveBtn = document.querySelector('#novoIngredienteModal .btn-ice');
    saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Salvando...';
    saveBtn.disabled = true;

    fetch('/ingredientes/salvar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify({
            nome: nome,
            custoPorUnidade: custo,
            estoqueInicial: estoque,
            unidadeMedida: unidade
        })
    })
    .then(response => {
        if (!response.ok) throw new Error('Erro no servidor: ' + response.status);
        return response.json();
    })
    .then(data => {
        // Atualiza a lista global
        allIngredientes.push(data);

        // Atualiza os selects
        document.querySelectorAll('select[th\\:field*="ingrediente.id"]').forEach(select => {
            const option = document.createElement('option');
            option.value = data.id;
            option.textContent = data.nome;
            option.dataset.custo = data.custoPorUnidade;
            select.appendChild(option);
        });

        // Fecha o modal
        bootstrap.Modal.getInstance(document.getElementById('novoIngredienteModal')).hide();
        document.getElementById('formNovoIngrediente').reset();
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao salvar: ' + error.message);
    })
    .finally(() => {
        saveBtn.innerHTML = '<i class="bi bi-save2-fill me-2"></i>Salvar';
        saveBtn.disabled = false;
    });
}

// Função para abrir o modal de edição e carregar os dados do geladinho
function abrirModalEditarGeladinho(id, sabor, estoqueInicial, estoqueAtual, ingredientes) {
    // Preenche os campos do modal com os dados do geladinho
    document.getElementById('editarGeladinhoId').value = id;
    document.getElementById('editarGeladinhoSabor').value = sabor;
    document.getElementById('editarGeladinhoEstoqueInicial').value = estoqueInicial;
    document.getElementById('editarGeladinhoEstoqueAtual').value = estoqueAtual;

    // Limpa o container de ingredientes
    const ingredientesContainer = document.getElementById('editarGeladinhoIngredientesContainer');
    ingredientesContainer.innerHTML = '';

    // Adiciona os ingredientes ao container
    ingredientes.forEach(ingrediente => {
        const divIngrediente = document.createElement('div');
        divIngrediente.className = 'row g-2 mb-2';
        divIngrediente.innerHTML = `
            <div class="col-5">
                <select class="form-select" name="ingredientes[${ingrediente.id}].ingrediente.id">
                    <option value="">Selecione um ingrediente</option>
                    ${allIngredientes.map(ing => `
                        <option value="${ing.id}" data-custo="${ing.custoPorUnidade}">${ing.nome}</option>
                    `).join('')}
                </select>
            </div>
            <div class="col-5">
                <input class="form-control" name="ingredientes[${ingrediente.id}].quantidade" placeholder="Quantidade" required type="number" value="${ingrediente.quantidade}">
            </div>
            <div class="col-2">
                <i class="bi bi-x-circle-fill text-danger" onclick="removerIngredienteEdicao(this)" style="cursor:pointer; font-size:1.2rem;"></i>
            </div>
        `;
        ingredientesContainer.appendChild(divIngrediente);
    });

    // Abre o modal
    new bootstrap.Modal(document.getElementById('editarGeladinhoModal')).show();
}

// Função para adicionar um novo campo de ingrediente no modal de edição
function adicionarIngredienteEdicao() {
    const ingredientesContainer = document.getElementById('editarGeladinhoIngredientesContainer');
    const novoIngredienteId = Date.now(); // ID único para o novo ingrediente

    const divIngrediente = document.createElement('div');
    divIngrediente.className = 'row g-2 mb-2';
    divIngrediente.innerHTML = `
        <div class="col-5">
            <select class="form-select" name="ingredientes[${novoIngredienteId}].ingrediente.id">
                <option value="">Selecione um ingrediente</option>
                ${allIngredientes.map(ing => `
                    <option value="${ing.id}" data-custo="${ing.custoPorUnidade}">${ing.nome}</option>
                `).join('')}
            </select>
        </div>
        <div class="col-5">
            <input class="form-control" name="ingredientes[${novoIngredienteId}].quantidade" placeholder="Quantidade" required type="number">
        </div>
        <div class="col-2">
            <i class="bi bi-x-circle-fill text-danger" onclick="removerIngredienteEdicao(this)" style="cursor:pointer; font-size:1.2rem;"></i>
        </div>
    `;
    ingredientesContainer.appendChild(divIngrediente);
}

// Função para remover um campo de ingrediente no modal de edição
function removerIngredienteEdicao(element) {
    element.closest('.row').remove();
}

// Função para formatar o valor como R$
function formatarComoReais(valor) {
    return `R$ ${parseFloat(valor).toFixed(2)}`;
}

// Inicializa o preço de custo como R$ 0,00 ao carregar a página
document.addEventListener('DOMContentLoaded', function() {
    const precoCustoInput = document.getElementById('precoCusto');
    precoCustoInput.value = 0.00; // Garante que seja um número

});

// Event Listeners
document.addEventListener('DOMContentLoaded', function() {
    toggleCamposGas();
    toggleCamposAgua();

    // Adiciona listeners para campos de ingredientes
    document.querySelectorAll('.quantidade-input').forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de água
    document.querySelectorAll("[name='quantidadeGaloes'], [name='metrosCubicosAgua']").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de gás
    document.querySelectorAll("[name='horasGas']").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de energia (kcal, MJ, kWh)
    document.querySelectorAll("#kcal, #mj, #kwh").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para checkboxes de queimadores
    document.querySelectorAll("[name='queimadores']").forEach(checkbox => {
        checkbox.addEventListener('change', calcularTotalCusto);
    });

    // Adiciona listeners para a seleção da fonte de água
    document.querySelector("[name='fonteAgua']").addEventListener('change', calcularTotalCusto);

    // Adiciona listeners para os botões de edição
    document.addEventListener('DOMContentLoaded', function() {
        const botoesEditar = document.querySelectorAll('.btn-editar-geladinho');
        const modal = new bootstrap.Modal(document.getElementById('editarGeladinhoModal'));

        botoesEditar.forEach(botao => {
            botao.addEventListener('click', function() {
                document.getElementById('editarGeladinhoId').value = this.getAttribute('data-id');
                document.getElementById('editarGeladinhoSabor').value = this.getAttribute('data-sabor');
                document.getElementById('editarGeladinhoEstoqueInicial').value = this.getAttribute('data-estoque-inicial');
                document.getElementById('editarGeladinhoEstoqueAtual').value = this.getAttribute('data-estoque-atual');
                // Aqui você precisaria também iterar sobre os ingredientes e adicioná-los ao modal.

                modal.show();
            });
        });
    });

document.addEventListener('DOMContentLoaded', function() {
    toggleCamposGas();
    toggleCamposAgua();

    // Adiciona listeners para campos de ingredientes
    document.querySelectorAll('.quantidade-input').forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de água
    document.querySelectorAll("[name='quantidadeGaloes'], [name='metrosCubicosAgua']").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de gás
    document.querySelectorAll("[name='horasGas']").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para campos de energia (kcal, MJ, kWh)
    document.querySelectorAll("#kcal, #mj, #kwh").forEach(input => {
        input.addEventListener('input', calcularTotalCusto);
    });

    // Adiciona listeners para checkboxes de queimadores
    document.querySelectorAll("[name='queimadores']").forEach(checkbox => {
        checkbox.addEventListener('change', calcularTotalCusto);
    });

    // Adiciona listeners para a seleção da fonte de água
    document.querySelector("[name='fonteAgua']").addEventListener('change', calcularTotalCusto);
});

// Atualiza o preço de custo em tempo real consultando o backend
async function atualizarPrecoCusto() {
    const formData = new FormData(document.querySelector('form'));

    try {
        const response = await fetch('/geladinhos/calcularPrecoCusto', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error('Erro ao calcular preço de custo');

        const data = await response.json();
        const precoCustoField = document.getElementById('precoCusto');
        precoCustoField.value = formatarParaReais(data.precoCusto);
        precoCustoField.setAttribute('data-raw-value', data.precoCusto);
    } catch (error) {
        console.error('Erro ao atualizar preço de custo:', error);
    }
}

// Adiciona listeners para recalcular preço de custo ao alterar ingredientes ou taxas
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('#ingredientes-container select, #ingredientes-container input').forEach(element => {
        element.addEventListener('change', atualizarPrecoCusto);
    });
    document.querySelectorAll('#taxaAgua, #taxaGas, #taxaEnergia').forEach(element => {
        element.addEventListener('input', atualizarPrecoCusto);
    });
});

// Função para formatar valor monetário em reais
function formatarParaReais(valor) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
}