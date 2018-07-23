import DepartureTable from "./departure";

const React = require('react');
const ReactDOM = require('react-dom');
import AircraftTable from './aircraft'
import ArrivalTable from './arrival'

class App extends React.Component {

    constructor(props) {
        super(props);
        this.onDepartureTableChange = this.onDepartureTableChange.bind(this);
    }

    onDepartureTableChange() {
        console.log('onDepartureTableChanged invoked')
        location.reload();
    }

    render() {
        return (
            <div>
                <h2>Aircraft Status</h2>
                <AircraftTable/>
                <h2>Arrivals</h2>
                <ArrivalTable/>
                <h2>Departures</h2>
                <DepartureTable onTableChange={this.onDepartureTableChange}/>
            </div>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)