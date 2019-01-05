import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakDayOrderList extends DenTravakAbstractElement {

    constructor() {
        super('travak-admin-app')
    }

    connectedCallback() {
        super.connectedCallback();
        fetch('/den-travak/orders/')
            .then(resp => resp.json())
            .then(json => this.updateOrderList(json));
        this.initEventListeners();
    }

    initEventListeners() {
        this.byId('show-all-orders-btn').addEventListener('click', (e) => this.app().showOrderList());
        this.byId('download-csv-btn').addEventListener('click', (e) => this.app().getCsv());
    }

    updateOrderList(orders) {
        let today = new Date();
        today.setHours(0, 0, 0);

        let tomorrow = new Date();
        tomorrow.setHours(23, 59, 59);

        let filteredData = orders.filter(function (product) {
            const date = new Date(product.creationDate);
            return (date >= today && date <= tomorrow);
        });

        let orderList = this.byId('orders');
        orderList.innerHTML = ``;
        filteredData.forEach(order => {
            let orderEl = htmlToElement(this.getOrderTemplate(order));
            orderList.appendChild(orderEl);
        });
    }

    getCsv() {
        fetch('/den-travak/orders')
            .then(resp => resp.json())
            .then(json => {
                let today = new Date();
                today.setHours(0, 0, 0);

                let tomorrow = new Date();
                tomorrow.setHours(23, 59, 59);

                let filteredData = json.filter(function (product) {
                    const date = new Date(product.creationDate);
                    return (date >= today && date <= tomorrow);
                });

                const table = document.getElementById("orders");
                for (let i = 0; i < table.childNodes.length; i++) {
                    const printedCell = document.createElement("td");
                    printedCell.innerHTML = "true";
                    table.childNodes[i].appendChild(printedCell);
                };

                var data = []
                for(var i = 0; i < filteredData.length; i++) {
                    data.push([filteredData[i].id, filteredData[i].sandwichId,
                        filteredData[i].name, filteredData[i].breadType,
                        filteredData[i].price, filteredData[i].mobilePhoneNumber, filteredData[i].creationDate]);
                }
                var separate = 'sep=,\r\n';
                var csv = separate + 'ID,SandwichID,Name,BreadType,Price,MobilePhoneNumber,CreationDate\n';
                data.forEach(function(row) {
                    csv += row.join(',');
                    csv += "\n";
                });
                var hiddenElement = document.createElement('a');
                hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
                hiddenElement.target = '_blank';
                hiddenElement.download = 'orders.csv';
                hiddenElement.click();
            });
    }

    get template() {
        return `
            <style>
                div.dt-order-info {
                    margin-left: auto;
                }
                .bmd-list-group-col {
                    width: 70%;
                }
                p.list-group-item-heading {
                    display:flex;
                    justify-content: space-between;
                }
                span.creationDate {
                    display:inline-block;
                    float: right;
                }
                .travak-header {
                    display: flex;
                }
                .travak-header button {
                    margin-left: auto;
                }
            </style>
            <div class="animate">
                <div class="travak-header">
                    <h4>Den Travak Bestellingen</h4>
                    <button id="show-all-orders-btn" type="button" class="btn btn-primary">Bewerk broodjeslijst</button>
                    <button id="download-csv-btn" type="button" class="btn btn-primary">Dowload bestellingen van vandaag</button>
                </div>
                <div>
                <table id="orders" class="list-group">
                </table>
                </div>
            </div>
        `;
    }

    getOrderTemplate(order) {
        return `
            <tr>
                <td>
                    <a class="list-group-item" id="listItem">
                        <button type="button" class="btn btn-primary bmd-btn-fab">
                            ${order.name.charAt(0)}
                        </button>
                        <div class="bmd-list-group-col">
                            <p class="list-group-item-heading">${order.mobilePhoneNumber}<span class="creationDate">${dateFns.distanceInWordsToNow(order.creationDate)} ago</span></p>
                            <p class="list-group-item-text">${order.name} - ${order.breadType.toLowerCase()}</p>
                        </div>
                        <div class="dt-order-info">
                            <p class="list-group-item-text">${order.price}</p>
                        </div>
                    </a>
                </td>
            </tr>
        `;
    }
}

customElements.define('travak-dayOrders-list', DenTravakDayOrderList);