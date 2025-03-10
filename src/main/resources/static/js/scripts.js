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
                       placeholder="Quantidade (gramas)" required
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