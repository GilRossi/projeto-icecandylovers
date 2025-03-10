// Preços do gás (R$/kg)
const PRECOS_GAS = {
    GN: 5.00,
    GLP: 3.50
};

// Habilita campos e define preço inicial do gás
function toggleCamposGas() {
    const tipoGas = document.getElementById('tipoGas').value;
    const taxaGasInput = document.getElementById('taxaGas');
    const camposConversao = document.querySelectorAll('#kcal, #mj, #kwh');
    const taxaGasInput = document.getElementById('taxaGas');
    const precoGasGN = document.getElementById('precoGasGN');
    const precoGasGLP = document.getElementById('precoGasGLP');

    // Habilita campos de energia se um tipo de gás for selecionado
    camposEnergia.forEach(campo => campo.disabled = (tipoGas === ""));

    // Resetar exibição de preços
    precoGasGN.classList.add('d-none');
    precoGasGLP.classList.add('d-none');

    // Habilitar campos de conversão
    camposConversao.forEach(campo => campo.disabled = (tipoGas === ""));

    // Só define o valor se o campo estiver vazio
    if (taxaGasInput.value === "") {
                if (tipoGas === "GN") {
                    taxaGasInput.value = 5.00;
                } else if (tipoGas === "GLP") {
                    taxaGasInput.value = 3.50;
           }
       }

    // Definir preço do gás e exibir badge
    if (tipoGas === "GN") {
        taxaGasInput.value = PRECOS_GAS.GN;
        precoGasGN.classList.remove('d-none');
    } else if (tipoGas === "GLP") {
        taxaGasInput.value = PRECOS_GAS.GLP;
        precoGasGLP.classList.remove('d-none');
    } else {
        taxaGasInput.value = "";
    }

    // Limpar campos se nenhum tipo for selecionado
    if (tipoGas === "") {
        camposConversao.forEach(campo => campo.value = "");
    }

    // Atualizar cálculos
    calcularKwhKg();
}

// Conversão entre kcal, MJ e kWh
function converterEnergia() {
    const input = event.target;
    const valor = parseFloat(input.value) || 0;
    let kcal, mj, kwh;

    // Lógica de conversão (exemplo)
    if (input.id === 'kcal') {
        kcal = valor;
        mj = kcal * 0.004184;
        kwh = kcal / 860;
    } else if (input.id === 'mj') {
        mj = valor;
        kcal = mj / 0.004184;
        kwh = mj / 3.6;
    } else if (input.id === 'kwh') {
        kwh = valor;
        kcal = kwh * 860;
        mj = kwh * 3.6;
    }

    // Atualiza campos
    document.getElementById('kcal').value = kcal?.toFixed(2) || '';
    document.getElementById('mj').value = mj?.toFixed(2) || '';
    document.getElementById('kwh').value = kwh?.toFixed(2) || '';
}

// Cálculo final dos kWh/Kg
function calcularKwhKg() {
    const kwh = parseFloat(document.getElementById('kwh').value) || 0;
    const quadrichama = parseFloat(document.getElementById('quadrichama').value) || 0;
    const rapido = parseFloat(document.getElementById('rapido').value) || 0;
    const semirapido = parseFloat(document.getElementById('semirapido').value) || 0;

    // Cálculos
    document.getElementById('kwhKgQuadrichama').value = (quadrichama / kwh).toFixed(2);
    document.getElementById('kwhKgRapido').value = (rapido / kwh).toFixed(2);
    document.getElementById('kwhKgSemirapido').value = (semirapido / kwh).toFixed(2);
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