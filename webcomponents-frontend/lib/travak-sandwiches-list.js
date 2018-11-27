import DenTravakAbstractElement from './travak-abstract-element.js';

class DenTravakSandwichesList extends DenTravakAbstractElement {

    connectedCallback() {
        super.connectedCallback();
        fetch('/api/sandwiches.json')
            .then(resp => resp.json())
            .then(json => this.updateSandwichesList(json));
    }

    updateSandwichesList(sandwiches) {
        let sandwichesList = this.byId('sandwiches');
        //sandwichesList.innerHTML = ``;
        sandwiches.forEach(sandwich => {
            let sandwichEl = htmlToElement(this.getSandwichTemplate(sandwich));
            sandwichEl.addEventListener('click', () => this.app().dispatchEvent(new CustomEvent('checkout', {detail: sandwich})));
            sandwichesList.appendChild(sandwichEl);
        });
    }

    get template() {
        return `
            </style>
            <div>
                <h3>Welkom bij den Travak</h3>
                <h4>Kies je broodje</h4>
                <div>
                <ul id="sandwiches">
                    </ul>
                </div>
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
                <div>
                    <p>${sandwich.price}</p>
                </div>
            </a>
        `;
    }
}

customElements.define('travak-sandwiches-list', DenTravakSandwichesList);