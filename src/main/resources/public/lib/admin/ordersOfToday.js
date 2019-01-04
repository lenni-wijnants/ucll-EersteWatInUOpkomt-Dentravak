getOrders();
function getOrders() {
    fetch('/den-travak/orders').then(response => response.json())
        .then(data => {
            filterOrders(data);
        })
}

function filterOrders(data) {
    const table = document.getElementById("OrderTable");
    let today = new Date();
    today.setHours(1, 0, 0);

    let tomorrow = new Date();
    tomorrow.setHours(25, 0, 0);

    let filteredData = data.filter(function (product) {
        const date = new Date(product.creationDate);
        return (date >= today && date <= tomorrow);
    });

    let start = table.childNodes.length;
    for (let i = start; i < filteredData.length; i++) {
        let name = filteredData[i].name + " (" + filteredData[i].breadType + ")";
        let price = filteredData[i].price;
        let number = filteredData[i].mobilePhoneNumber;

        const tableRow = document.createElement("tr");
        const nameCell = document.createElement("td");
        nameCell.innerHTML = name;
        tableRow.appendChild(nameCell);
        const priceCell = document.createElement("td");
        priceCell.innerHTML = price;
        tableRow.appendChild(priceCell);
        const numberCell = document.createElement("td");
        numberCell.innerHTML = number;
        tableRow.appendChild(numberCell);
        table.appendChild(tableRow);
    }
}

function getCsv() {
    getOrders();
    fetch('/den-travak/orders').then(response => response.json())
        .then(data => {
            generateCsv(data);
        })
}

function generateCsv(data) {
    if (!document.getElementById("csvheader")) {
        const tableheader = document.getElementById("TableHeadRow");
        const csvHeader = document.createElement("th");
        csvHeader.innerHTML = "Printed?";
        csvHeader.id = "csvheader";
        tableheader.appendChild(csvHeader);
    }
    const table = document.getElementById("OrderTable");
    for (let i = 0; i < table.childNodes.length; i++) {
        if (table.childNodes[i].childNodes.length === 3) {
            const printedCell = document.createElement("td");
            printedCell.innerHTML = "true";
            table.childNodes[i].appendChild(printedCell);
        }
    };
    let replacer = (key, value) => value === null ? '' : value;
    let header = Object.keys(data[0]);
    let csv = data.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','));
    csv.unshift(header.join(','));
    csv = csv.join('\r\n');
    window.open('data:text/csv;charset=utf-8,' + escape(csv));
}