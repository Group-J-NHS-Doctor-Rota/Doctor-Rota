import React from 'react'

export default function SwitchCalendar(){

    return (
        <div className="d-flex align-items-center mt-3">
            <div className="m-1">
                <i className="bi bi-caret-left-fill" style={{cursor: 'pointer'}}></i>
            </div>
            <div className="m-1">
                <p className="mb-0">2022/06</p>
            </div>
            <div className="m-1">
                <i className="bi bi-caret-right-fill" style={{cursor: 'pointer'}}></i>
            </div>
        </div>
    )
}