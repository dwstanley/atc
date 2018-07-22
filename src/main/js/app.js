const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {aircrafts: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/atc/aircrafts'}).done(response => {
            this.setState({aircrafts: response.entity._embedded.aircrafts});
        });
    }

    render() {
        return (
            <AircraftList aircrafts={this.state.aircrafts}/>
        )
    }
}

class AircraftList extends React.Component{
    render() {
        var aircrafts = this.props.aircrafts.map(aircraft =>
            <Aircraft key={aircraft._links.self.href} aircraft={aircraft}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Size</th>
                    <th>Status</th>
                </tr>
                {aircrafts}
                </tbody>
            </table>
        )
    }
}

class Aircraft extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.aircraft.name}</td>
                <td>{this.props.aircraft.type}</td>
                <td>{this.props.aircraft.size}</td>
                <td>UNKNOWN</td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)