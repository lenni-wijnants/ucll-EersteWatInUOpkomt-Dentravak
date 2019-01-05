import DenTravakAbstractElement from '../travak-abstract-element.js';

class DenTravakOrderList extends DenTravakAbstractElement {

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
        this.byId('edit-sandwiches-btn').addEventListener('click', (e) => this.app().showSandwichList());
        //this.byId('orders-today-btn').addEventListener('click', (e) => this.dayOrderList());
        this.byId('dl-today-btn').addEventListener('click', (e) => this.csvDayOrderList());
    }

    updateOrderList(orders) {
        let today = new Date();
        today.setHours(0, 0, 0);

        let tomorrow = new Date();
        tomorrow.setHours(23, 59, 59);

        let filteredData = orders.filter(function (product) {
            let date = new Date(product.creationDate);
            return (date >= today && date <= tomorrow);
        });

        let orderList = this.byId('orders');
        orderList.innerHTML = ``;
        filteredData.forEach(order => {
            let orderEl = htmlToElement(this.getOrderTemplate(order));
            orderList.appendChild(orderEl);
        });
    }

    /*dayOrderList() {
        fetch('/den-travak/orders/')
            .then(resp => resp.json())
            .then(json => {
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
            });


    }*/

   csvDayOrderList() {
        fetch('/den-travak/orders')
            .then(resp => resp.json())
            .then(json => {
                let today = new Date();
                today.setHours(0, 0, 0);

                let tomorrow = new Date();
                tomorrow.setHours(23, 59, 59);

                let filteredData = json.filter(function (product) {
                    let date = new Date(product.creationDate);
                    return (date >= today && date <= tomorrow);
                });

                let table = document.getElementById("orders");
                alert($("ul > div").length);
                for (let i = 0; i < $("ul > div").length; i++) {
                    let printedCell = document.getElementById("dlText");
                    printedCell.innerHTML = "true";
                }

                let data = [];
                for(let i = 0; i < filteredData.length; i++) {
                    data.push([filteredData[i].id, filteredData[i].sandwichId,
                        filteredData[i].name, filteredData[i].breadType,
                        filteredData[i].price, filteredData[i].mobilePhoneNumber, filteredData[i].creationDate]);
                }
                let separate = 'sep=,\r\n';
                let csv = separate + 'ID,SandwichID,Name,BreadType,Price,MobilePhoneNumber,CreationDate\n';
                data.forEach(function(row) {
                    csv += row.join(',');
                    csv += "\n";
                });
                let hiddenElement = document.createElement('a');
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
                    <button id="edit-sandwiches-btn" type="button" class="btn btn-primary">Bewerk broodjeslijst</button>
                </div>
                <div>
                    <ul id="orders" class="list-group">
                    </ul>
                </div>
                <button id="dl-today-btn" type="button" class="btn btn-primary">Download bestellingen van vandaag</button>
            </div>
        `;
    }


    /*<div class="animate">
        <div class="travak-header">
            <h4>Den Travak Bestellingen</h4>
            <button id="edit-sandwiches-btn" type="button" class="btn btn-primary">Bewerk broodjeslijst</button>
            <button id="dl-today-btn" type="button" class="btn btn-primary">Download bestellingen van vandaag</button>
        </div>
        <div>
        <ul id="orders" class="list-group">
        </ul>
        </div>
    </div>*/
    getOrderTemplate(order) {
        /*return `
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
        `;*/
        return `
            <div class="bestelling">
                <div class="bestelling-body">
                    <a class="list-group-item">
                        <button type="button" class="btn btn-primary bmd-btn-fab">
                            ${order.name.charAt(0)}
                        </button>
                        <div class="bmd-list-group-col">
                            <p class="list-group-item-heading">${order.mobilePhoneNumber}<span class="creationDate">${dateFns.distanceInWordsToNow(order.creationDate)} ago</span></p>
                            <p class="list-group-item-text">${order.name} - ${order.breadType.toLowerCase()}</p>
                        </div>
                        <div class="dt-order-info">
                            <p class="list-group-item-text">Prijs: €${order.price}</p>
                        </div>
                    </a>
                    <p id="dlText"></p>
                </div>
            </div>
        `;
    }
}

customElements.define('travak-order-list', DenTravakOrderList);