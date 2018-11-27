import DenTravakAbstractElement from './travak-abstract-element.js';

class DenTravakSandwichesOrderConfirmation extends DenTravakAbstractElement {

    connectedCallback() {
        super.connectedCallback();
        this.initEventListeners();
    }

    init(sandwich) {
        this.sandwich = sandwich;
    }

    initEventListeners() {
        this.byId('show-sandwich-list').addEventListener('click', e => this.app().dispatchEvent(new Event('show-sandwich-list')));
    }

    get template() {
        return `
            <div>
                <div>
                    <h3>Welkom bij den Travak</h3>
                    <button id="show-sandwich-list">Nieuwe bestelling</button>
                </div>
                <h4>Bedankt!</h4>
                <p>Wij hebben je bestelling goed ontvangen en je kan je broodje komen ophalen vanaf 11u45.</p>
                <p>Tot zo dadelijk!</p>
            </div>
        `;
    }
}

customElements.define('travak-sandwiches-order-confirmation', DenTravakSandwichesOrderConfirmation);