// Preços do gás (R$/kg)
const PRECOS_GAS = {
    GN: 5.00,
    GLP: 3.50
};

function toggleCamposGas() {
    const tipoGas = document.getElementById('tipoGas').value;
    const taxaGasInput = document.getElementById('taxaGas');
    const camposConversao = document.querySelectorAll('#kcal, #mj, #kwh'); // Seleciona os campos de energia
    const precoGasGN = document.getElementById('precoGasGN');
    const precoGasGLP = document.getElementById('precoGasGLP');

    // Habilita/desabilita campos de energia
    camposConversao.forEach(campo => {
            campo.disabled = (tipoGas === ""); // Desabilita apenas se não houver tipo
            campo.readOnly = false; // Sempre permite edição (não bloqueia a exibição)
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
        camposConversao.forEach(campo => campo.value = ""); // Limpa campos se não houver tipo
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

// Atualiza cálculos de kWh/Kg com os valores convertidos
function calcularKwhKg() {
    const kwh = parseFloat(document.getElementById('kwh').value) || 0;
    const quadrichama = parseFloat(document.getElementById('quadrichama').value) || 0;
    const rapido = parseFloat(document.getElementById('rapido').value) || 0;
    const semirapido = parseFloat(document.getElementById('semirapido').value) || 0;
}

// Funções para adicionar/remover ingredientes
function addIngrediente() {
    const container = document.getElementById('ingredientes-container');
    const index = container.children.length;

    const newIngrediente = `
        <div class="ingrediente-item row g-2">
            <div class="col-5">
                <select class="form-select" name="ingredientes[${index}].ingrediente.id">
                    <option value="">Selecione um ingrediente</option>
                    ${allIngredientes.map(ing => `
                        <option value="${ing.id}" data-custo="${ing.custoPorUnidade}">${ing.nome}</option>
                    `).join('')}
                </select>
            </div>
            <div class="col-5">
                <input type="number" step="0.1" class="form-control"
                       name="ingredientes[${index}].quantidade"
                       placeholder="Quantidade" required
                       onchange="updateCusto(this)">
            </div>
            <div class="col-2">
                <i class="bi bi-x-circle-fill remove-ingrediente"
                   onclick="removeIngrediente(this)"></i>
            </div>
        </div>
    `;

    container.insertAdjacentHTML('beforeend', newIngrediente);
}

// Função para calcular o custo total
function calcularTotalCusto() {
    let total = 0;

    // Custo dos ingredientes
    document.querySelectorAll('.ingrediente-item').forEach(row => {
        const custo = parseFloat(row.dataset.custo) || 0;
        total += custo;
    });

    // Custo da água
    const custoAgua = parseFloat(document.querySelector("[name='precoCusto']").value) || 0;
    total += custoAgua;

    // Custo do gás
    const horasGas = parseFloat(document.querySelector("[name='horasGas']").value) || 0;
    const taxaGas = parseFloat(document.getElementById('taxaGas').value) || 0;
    total += horasGas * taxaGas;

    // Atualiza o campo
    document.querySelector("[name='precoCusto']").value = total.toFixed(2);
}

function removeIngrediente(element) {
    element.closest('.ingrediente-item').remove();
    calcularTotalCusto();
}

// Atualiza o custo total
function updateCusto(element) {
    const row = element.closest('.ingrediente-item');
    const custoUnitario = parseFloat(row.querySelector('option:checked')?.dataset.custo || 0);
    const quantidade = parseFloat(element.value) || 0;
    row.dataset.custo = (custoUnitario * quantidade).toFixed(2);
    calcularTotalCusto();
}
document.addEventListener('DOMContentLoaded', function() {
    toggleCamposGas(); // Executa ao carregar a página
});
// Atualiza a tabela com o valor convertido de kWh
document.getElementById('resultadoKwh').textContent = kwh?.toFixed(4) + ' kWh';
// Adicione um listener para o evento de submit do formulário
document.querySelector('form').addEventListener('submit', function() {
    // Força a conversão final antes de enviar
    const kwh = document.getElementById('kwh').value;
    // Garante que o valor está correto
    if (!kwh) {
        alert('Preencha os campos de energia antes de salvar.');
        event.preventDefault();
    }
});

function toggleCamposAgua() {
    const fonteAgua = document.querySelector('[th\\:field="*{fonteAgua}"]').value;
    document.getElementById('aguaGalaoCampos').classList.toggle('d-none', fonteAgua !== 'GALAO');
    document.getElementById('aguaTorneiraCampos').classList.toggle('d-none', fonteAgua !== 'TORNEIRA');
}

document.addEventListener("DOMContentLoaded", function () {
    toggleCamposAgua();
    calcularCustoAgua();

    // Monitora mudanças na seleção da fonte de água
    document.querySelector("[name='fonteAgua']").addEventListener("change", function () {
        toggleCamposAgua();
        calcularCustoAgua();
    });

    // Monitora entrada de valores nos campos de quantidade de água
    document.querySelectorAll("[name='quantidadeGaloes'], [name='metrosCubicosAgua']").forEach(input => {
        input.addEventListener("input", calcularCustoAgua);
    });
});

// Alterna exibição dos campos de acordo com a fonte de água
function toggleCamposAgua() {
    const fonteAgua = document.querySelector("[name='fonteAgua']").value;
    document.getElementById("aguaGalaoCampos").classList.toggle("d-none", fonteAgua !== "GALAO");
    document.getElementById("aguaTorneiraCampos").classList.toggle("d-none", fonteAgua !== "TORNEIRA");
}

// Calcula o custo da água baseada na seleção e na quantidade
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
// Alterna exibição dos campos conforme os checkboxes marcados
function toggleCamposAgua() {
    const aguaGalaoCheck = document.getElementById("aguaGalaoCheck").checked;
    const aguaTorneiraCheck = document.getElementById("aguaTorneiraCheck").checked;

    document.getElementById("aguaGalaoCampos").classList.toggle("d-none", !aguaGalaoCheck);
    document.getElementById("aguaTorneiraCampos").classList.toggle("d-none", !aguaTorneiraCheck);
}

// Calcula o custo da água com base nos checkboxes marcados
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

    document.querySelector("[name='precoCusto']").value = custoAgua.toFixed(2);
}

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
