import DenTravakAbstractElement from './travak-abstract-element.js';

class DenTravakSandwichesCheckout extends DenTravakAbstractElement {

    connectedCallback() {
        super.connectedCallback();
        this.initEventListeners();
    }

    init(sandwich) {
        this.sandwich = sandwich;
        this.byId('sandwiches').innerHTML = ``;
        this.byId('sandwiches').appendChild(htmlToElement(this.getSandwichTemplate(this.sandwich)));
    }

    initEventListeners() {
        this.byId('order-button').addEventListener('click', e => this.orderSandwich());
        this.byId('back-button').addEventListener('click', e => this.app().showSandwichList());
    }

    orderSandwich() {
        //todo: call backend via fetch api
        this.app().dispatchEvent(new CustomEvent('order-succeeded', {detail: this.sandwich}));
    }

    get template() {
        return `
            <style>
                .form-group {
                    margin-bottom: 2rem;
                }
                .dt-header {
                    display: flex;
                }
                .dt-header button {
                    margin-left: auto;
                }
                div.dt-sandwich-info {
                    margin-left: auto;
                }
            </style>
            <div>
                <div>
                    <h3>Welkom bij den Travak</h3>
                    <button id="back-button" type="button" class="btn btn-primary">Terug</button>
                </div>
                <h4>Je geselecteerde broodje</h4>
                <div>
                <ul id="sandwiches" class="list-group"></ul>
                </div>
                <div>
                    <label for="typeBrood"><h4>Kies het type brood</h4></label>
                    <div>
                        <input type="radio" name="typeBrood" id="radioBoterhammekes" value="option1">
                        <label for="radioBoterhammekes">
                            Boterhammekes
                        </label>
                    </div>
                    <div>
                        <input type="radio" name="typeBrood" id="radioWrap" value="option2">
                        <label for="radioWrap">
                            Wrap
                        </label>
                    </div>
                    <div>
                        <input type="radio" name="typeBrood" id="radioTurksBrood" value="option3">
                        <label for="radioTurksBrood">
                            Turks brood
                        </label>
                    </div>
                </div>
                <div>
                    <label for="mobile-phone-number"><h4>Je GSM Nummer</h4></label>
                    <input type="text" id="mobile-phone-number" placeholder="0487/12 34 56">
                </div>

                <button id="order-button">Bestellen</button>
            </div>
        `;
    }

    getSandwichTemplate(sandwich) {
        return `
            <a>
                <button type="button">
                    ${sandwich.name.charAt(0)}
                </button>
                <div>
                    <p>${sandwich.name}</p>
                    <p>${sandwich.ingredients}</p>
                </div>
                <div class="dt-sandwich-info">
                    <p>${sandwich.price}</p>
                </div>
            </a>
        `;
    }
}

customElements.define('travak-sandwiches-checkout', DenTravakSandwichesCheckout);