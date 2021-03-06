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
        this.byId('refresh-btn').addEventListener('click', (e) => this.connectedCallback());
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

                let table = this.byId("orders");

                for(let i = 0; i < table.childElementCount; i++){
                    let dl = document.createElement("p");
                    dl.innerHTML = "true";
                    dl.className = "list-group-item-text";
                    table.children[i].append(dl);
                }

                let data = [["Id", "SandwichId", "Name", "Bread Type", "Price", "Phone Number", "Date"]];
                for(let i = 0; i < filteredData.length; i++) {
                    data.push([filteredData[i].id, filteredData[i].sandwichId,
                        filteredData[i].name, filteredData[i].breadType,
                        filteredData[i].price, filteredData[i].mobilePhoneNumber, filteredData[i].creationDate]);
                }

                let csv = "";
                data.forEach(function(rowArray){
                    let row = rowArray.join(",");
                    csv += row + "\r\n";
                });

                let csvContent = "data:text/csv;charset=utf-8," + csv;
                let encodedUri = encodeURI(csvContent);
                let link = document.createElement("a");
                link.setAttribute("href", encodedUri);
                link.setAttribute("download", "TodaysOrders.csv");
                document.body.appendChild(link); // Required for FF

                link.click();

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
                <button id="refresh-btn" type="button" class="btn btn-primary">Refresh</button>
            </div>
        `;
    }

    getOrderTemplate(order) {
        return `
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
        `;
    }
}

customElements.define('travak-order-list', DenTravakOrderList);