import Shift from './Shift'

export default function Calendar() {
    return (
        <div className="d-block px-2">
            <Shift type="night" />
            <Shift type="morning" />
        </div>
    )
}
