const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

const follow = require('./follow'); // function to hop multiple links by "rel"

const root = '/atc';

class ArrivalTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {arrivals: [], attributes: [], pageSize: 5, links: {}};
        this.onCreate = this.onCreate.bind(this);
        this.onComplete = this.onComplete.bind(this);
        this.onNavigate = this.onNavigate.bind(this);
    }

    loadFromServer(pageSize) {
        follow(client, root, [
            {rel: 'arrivals', params: {size: pageSize}}]
        ).then(arrivalCollection => {
            return client({
                method: 'GET',
                path: arrivalCollection.entity._links.profile.href,
                headers: {'Accept': 'application/schema+json'}
            }).then(schema => {
                this.schema = schema.entity;
                return arrivalCollection;
            });
        }).done(arrivalCollection => {
            this.setState({
                arrivals: arrivalCollection.entity._embedded.arrivals,
                attributes: Object.keys(this.schema.properties),
                pageSize: pageSize,
                links: arrivalCollection.entity._links
            });
        });
    }

    onCreate(aircraftVin) {
        client({method: 'GET', path: '/atc/requestArrival/?aircraftVin=' + aircraftVin}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    onComplete(arrival) {
        client({method: 'GET', path: '/atc/completeArrival/?aircraftVin=' + arrival.aircraft.vin}).done(response => {
            this.loadFromServer(this.state.pageSize);
        });
    }

    onNavigate(navUri) {
        client({method: 'GET', path: navUri}).done(arrivalCollection => {
            this.setState({
                arrivals: arrivalCollection.entity._embedded.arrivals,
                attributes: this.state.attributes,
                pageSize: this.state.pageSize,
                links: arrivalCollection.entity._links
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
                <ArrivalList arrivals={this.state.arrivals}
                             links={this.state.links}
                             pageSize={this.state.pageSize}
                             onComplete={this.onComplete}
                             onNavigate={this.onNavigate}/>
            </div>
        )
    }
}

class ArrivalList extends React.Component {

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

        var arrivals = this.props.arrivals.map(arrival =>
            <Arrival key={arrival._links.self.href} arrival={arrival} onComplete={this.props.onComplete}/>
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
                        <th></th>
                    </tr>
                    {arrivals}
                    </tbody>
                </table>
                <div>
                    {navLinks}
                </div>
            </div>
        )
    }

}

class Arrival extends React.Component {

    constructor(props) {
        super(props);
        this.handleComplete = this.handleComplete.bind(this);
    }

    handleComplete() {
        this.props.onComplete(this.props.arrival);
    }

    render() {
        return (
            <tr>
                <td>{this.props.arrival.aircraft.vin}</td>
                <td>{this.props.arrival.aircraft.name}</td>
                <td>{this.props.arrival.aircraft.type}</td>
                <td>{this.props.arrival.aircraft.size}</td>
                <td>{this.props.arrival.timestamp}</td>
                <td>
                    <button onClick={this.handleComplete}>Complete</button>
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
                <a href="#createArrival">New Request</a>
                <div id="createArrival" className="modalDialog">
                    <div>
                        <a href="#" title="Close" className="close">X</a>
                        <h2>Request arrival</h2>
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

export default ArrivalTable;