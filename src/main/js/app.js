import DepartureTable from "./departure";
import AircraftTable from './aircraft'
import ArrivalTable from './arrival'

const React = require('react');
const ReactDOM = require('react-dom');

class App extends React.Component {

    render() {
        return (
            <div>
                <h2>Aircraft Status</h2>
                <AircraftTable/>
                <h2>Arrivals</h2>
                <ArrivalTable/>
                <h2>Departures</h2>
                <DepartureTable/>
            </div>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)