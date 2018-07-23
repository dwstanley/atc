const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/atc';

class AircraftTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {aircrafts: [], attributes: [], pageSize: 5, links: {}};
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.onDepart = this.onDepart.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'aircrafts', params: {size: pageSize}}]
        ).then(aircraftCollection => {
            return client({
                method: 'GET',
                path: aircraftCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return aircraftCollection;
            });
        }).done(aircraftCollection => {
            this.setState({
                aircrafts: aircraftCollection.entity._embedded.aircrafts,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: aircraftCollection.entity._links
            });
        });
    }

    onCreate(newAircraft) {
        follow(client, root, ['aircrafts']).then(aircraftCollection => {
            return client({
                method: 'POST',
                path: aircraftCollection.entity._links.self.href,
                entity: newAircraft,
                headers: {'Content-Type': 'application/json'}
            })
        }).then(response => {
            return follow(client, root, [
                {rel: 'aircrafts', params: {'size': this.state.pageSize}}]);
        }).done(response => {
            if (typeof response.entity._links.last != "undefined") {
                this.onNavigate(response.entity._links.last.href);
            } else {
                this.onNavigate(response.entity._links.self.href);
            }
        });
    }

    onDelete(aircraft) {
        client({method: 'DELETE', path: aircraft._links.self.href}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    onDepart(aircraft) {
        client({method: 'GET', path: '/atc/requestDeparture/?aircraftVin=' + aircraft.vin}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(aircraftCollection => {
            this.setState({
                aircrafts: aircraftCollection.entity._embedded.aircrafts,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: aircraftCollection.entity._links
            });
        });
    }

    componentDidMount() {
        this.loadFromServer(this.state.pageSize);
    }

    render() {
        return (
            <div>
                <CreateDialog attributes={this.state.attributes} onCreate={this.onCreate}/>
                <AircraftList aircrafts={this.state.aircrafts}
                              links={this.state.links}
                              pageSize={this.state.pageSize}
                              onNavigate={this.onNavigate}
                              onDelete={this.onDelete}
                              onDepart={this.onDepart}/>
            </div>
        )
    }
}

class AircraftList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
    }

    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    render() {

        var aircrafts = this.props.aircrafts.map(aircraft =>
            <Aircraft key={aircraft._links.self.href} aircraft={aircraft} onDelete={this.props.onDelete} onDepart={this.props.onDepart}/>
        );

        var navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                <table>
                    <tbody>
                    <tr>
                        <th>VIN</th>
                        <th>Name</th>
                        <th>Type</th>
                        <th>Size</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                    {aircrafts}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }

}

class Aircraft extends React.Component {

    constructor(props) {
        super(props);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleDepart = this.handleDepart.bind(this);
    }

    handleDelete() {
        this.props.onDelete(this.props.aircraft);
    }

    handleDepart() {
        this.props.onDepart(this.props.aircraft);
    }

    render() {

        // let button;
        // if ('' === this.props.aircraft.status || 'UNKNOWN' === this.props.aircraft.status) {
        //     button = <button onClick={this.handleDepart}>Arrive</button>;
        // } else if ('Arriving' === this.props.aircraft.status) {
        //     button = <button onClick={this.handleDepart}>Depart</button>;
        // }

        return (
            <tr>
                <td>{this.props.aircraft.vin}</td>
                <td>{this.props.aircraft.name}</td>
                <td>{this.props.aircraft.type}</td>
                <td>{this.props.aircraft.size}</td>
                <td>{this.props.aircraft.status}</td>
                <td>
                    <button onClick={this.handleDelete}>Delete</button>
                    {/*<button onClick={this.handleDelete}>Delete</button>&nbsp;{button}*/}
                </td>
            </tr>
        )
    }
}


class CreateDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        var newAircraft = {};
        this.props.attributes.forEach(attribute => {
            newAircraft[attribute] = ReactDOM.findDOMNode(this.refs[attribute]).value.trim();
        });
        this.props.onCreate(newAircraft);

        // clear out the dialog's inputs
        this.props.attributes.forEach(attribute => {
            ReactDOM.findDOMNode(this.refs[attribute]).value = '';
        });

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        return (
            <div>
                <a href="#createAircraft">Add aircraft</a>

                <div id="createAircraft" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>

                        <h2>Create new aircraft</h2>

                        <form>
                            <p>
                                <input type="text" placeholder="vin" ref="vin" className="field"/>
                            </p>
                            <p>
                                <input type="text" placeholder="name" ref="name" className="field"/>
                            </p>
                            <p>
                                <select ref="type" className="dropdown">
                                    <option selected disabled>choose type</option>
                                    <option value="CARGO">CARGO</option>
                                    <option value="EMERGENCY">EMERGENCY</option>
                                    <option value="PASSENGER">PASSENGER</option>
                                    <option value="VIP">VIP</option>
                                </select>
                            </p>
                            <p>
                                <select ref="size" className="dropdown">
                                    <option selected disabled>choose size</option>
                                    <option value="SMALL">SMALL</option>
                                    <option value="MEDIUM">MEDIUM</option>
                                    <option value="LARGE">LARGE</option>
                                </select>
                            </p>
                            <p>
                                <select ref="status" className="dropdown">
                                    <option selected disabled>choose status</option>
                                    <option value="UNKNOWN">UNKNOWN</option>
                                    <option value="LANDED">LANDED</option>
                                </select>
                            </p>
                            <button onClick={this.handleSubmit}>Create</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

}

export default AircraftTable;