const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class DepartureTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {departures: [], attributes: [], pageSize: 5, links: {}};
        this.onCreate = this.onCreate.bind(this);
        this.onCompleteNext = this.onCompleteNext.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    loadFromServer(pageSize) {
        client({method: 'GET', path: '/atc/departures'}).done(departureCollection => {
            this.setState({
                departures: departureCollection.entity,
                attributes: this.state.attributes,
                pageSize: pageSize,
                links: this.state.links
            });
        });
    }

    onCreate(aircraftVin) {
        client({method: 'GET', path: '/atc/requestDeparture/?aircraftVin=' + aircraftVin}).done(response => {
            this.loadFromServer(this.state.pageSize);
            this.props.onTableChange();
        });
    }

    onCompleteNext() {
        client({method: 'GET', path: '/atc/departNext'}).done(response => {
            this.loadFromServer(this.state.pageSize);
            this.props.onTableChange();
        });
    }

    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(departureCollection => {
            this.setState({
                departures: departureCollection.entity._embedded.departures,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: departureCollection.entity._links
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
                <button onClick={this.onCompleteNext}>dequeue next</button>
                <DepartureList departures={this.state.departures}
                               links={this.state.links}
                               pageSize={this.state.pageSize}
                               onNavigate={this.onNavigate}/>
            </div>
        )
    }
}

class DepartureList extends React.Component {

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

        var departures = this.props.departures.map(departure =>
            <Departure key={departure.aircraft.vin} departure={departure}/>
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
                        <th>Timestamp</th>
                    </tr>
                    {departures}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }

}

class Departure extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <tr>
                <td>{this.props.departure.aircraft.vin}</td>
                <td>{this.props.departure.aircraft.name}</td>
                <td>{this.props.departure.aircraft.type}</td>
                <td>{this.props.departure.aircraft.size}</td>
                <td>{this.props.departure.timestamp}</td>
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

        var aircraftVin = ReactDOM.findDOMNode(this.refs["vin"]).value.trim();
        this.props.onCreate(aircraftVin);

        // clear out the dialog's inputs
        ReactDOM.findDOMNode(this.refs["vin"]).value = '';

        // Navigate away from the dialog to hide it.
        window.location = "#";
    }

    render() {
        return (
            <div>
                <a href="#createDeparture">New Request</a>
                <div id="createDeparture" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Request departure</h2>
                        <form>
                            <p>
                                <input type="text" placeholder="vin" ref="vin" className="field"/>
                            </p>
                            <button onClick={this.handleSubmit}>Request</button>
                        </form>
                    </div>
                </div>
            </div>
        )
    }

}

export default DepartureTable;