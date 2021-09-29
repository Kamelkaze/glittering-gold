const createConversionUrl = (source, target, amount) => `convertCurrency?sourceCurrency=${source}&targetCurrency=${target}&amount=${amount}`;
const CURRENCIES_URL = '/currencies';

initializePage();

function initializePage() {
    requestCurrencies();
    document.getElementById("convert").disabled = true;
}

async function requestCurrencies() {
    let result = await fetch(CURRENCIES_URL);

    if (!result.ok) {
        document.getElementById("background").innerText = 'Server error, please try again later.'
        return;
    }
    console.log(result.headers.get('Server-Timing'))

    let currencies = await result.json();
    let sourceSelector = document.getElementById("source");
    let targetSelector = document.getElementById("target");

    let addOption = (value, selector) => {
        let option = document.createElement("option");
        option.value = value;
        option.text = value;
        selector.add(option, null);
    }

    currencies.forEach(currency => {
        addOption(currency, sourceSelector);
        addOption(currency, targetSelector);
    });

}

async function convertCurrency() {
    
    let source = document.getElementById('source').value;
    let target = document.getElementById('target').value;
    let amount = document.getElementById('amount').value;

    let response = await fetch(createConversionUrl(source, target, amount));
    if (!response.ok) {
        document.getElementById('output').innerHTML = "No response from server, please try again later.";
        return;
    }
    
    let convertedAmount = await response.json();
    let sourceAmount = new Intl.NumberFormat(undefined, {style: "currency", currency: source}).format(amount);
    let targetAmount = new Intl.NumberFormat(undefined, {style: "currency", currency: target}).format(convertedAmount);
    document.getElementById('output').textContent = sourceAmount + " is worth " + targetAmount;
}

function getValueFromSelect(id) {
    let select = document.getElementById(id);
    return select.options[select.selectedIndex].value;
}

function validateAmount() {
    let amount = document.getElementById("amount").value;
    document.getElementById('convert').disabled = (amount === "");
}