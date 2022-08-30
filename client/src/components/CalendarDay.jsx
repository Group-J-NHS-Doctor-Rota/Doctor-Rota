import React, { useState, useEffect, useCallback } from 'react'

import Shift from './Shift'
import CalendarDayModal from '../modals/CalendarDayModal'

import styled from 'styled-components'

export default function CalendarDay({ allShift, year, month, day, holiday }) {
    const auth = JSON.parse(localStorage.getItem('auth'))
    let filterResult = JSON.parse(localStorage.getItem('filter'))

    const dateIncludeYear = day + "/" + month + "/" + year
    
    const [calendayDay, setCalendarDay] = useState(false)
    const [shifts, setShifts] = useState([])

    let filterRota = ""
    let filterShifts = ""
    let filterMember = ""

    if(filterResult != null){
        filterRota = filterResult.rota_type
        filterShifts = filterResult.shifts_type
        filterMember = filterResult.member  
    }
    
    useEffect(() => {
        setShifts([])
        
        if(allShift != undefined && filterResult == null){
            allShift
            .forEach(shift => {
                isDateMatch(shift.date) && setValue(shift)
            })
        }
        if(filterResult != null){
            if(filterResult.member == 10){
                if(allShift != undefined && filterResult != null && filterResult.rota_type == 10 && filterResult.shifts_type == 10){
                    allShift
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type != 10 && filterResult.shifts_type != 10){
                    allShift
                    .filter(shift => shift.rotaType == filterResult.rota_type && shift.type == filterResult.shifts_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type == 10 && filterResult.shifts_type != 10){
                    allShift
                    .filter(shift => shift.type == filterResult.shifts_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type != 10 && filterResult.shifts_type == 10){
                    allShift
                    .filter(shift => shift.rotaType == filterResult.rota_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
            }
            if(filterResult.member == 0){
                if(allShift != undefined && filterResult != null && filterResult.rota_type == 10 && filterResult.shifts_type == 10){
                    allShift
                    .filter(shift => shift.accountId == auth.id)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type != 10 && filterResult.shifts_type != 10){
                    allShift
                    .filter(shift => shift.accountId == auth.id)
                    .filter(shift => shift.rotaType == filterResult.rota_type && shift.type == filterResult.shifts_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type == 10 && filterResult.shifts_type != 10){
                    allShift
                    .filter(shift => shift.accountId == auth.id)
                    .filter(shift => shift.type == filterResult.shifts_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
                if(allShift != undefined && filterResult != null && filterResult.rota_type != 10 && filterResult.shifts_type == 10){
                    allShift
                    .filter(shift => shift.accountId == auth.id)
                    .filter(shift => shift.rotaType == filterResult.rota_type)
                    .forEach(shift => {
                        isDateMatch(shift.date) && setValue(shift)
                    })
                }
            }
        }
    }, [month, filterRota, filterShifts, filterMember])

    function isDateMatch(date){
        let shiftMonth = date.slice(5, 7)
        let shiftDay = date.slice(8, 10)
        
        if(shiftMonth[0] == 0){
            shiftMonth = date.slice(6, 7)
        }
        if(shiftDay[0] == 0){
            shiftDay = date.slice(9, 10)
        }
        
        if(shiftMonth == month && shiftDay == day){
            return true
        }else{
            return false
        }
    }

    function setValue(newShift){
        setShifts(prevShifts => [
            ...prevShifts, newShift
        ])
    }

    return (
        <div>
            <div className="d-block px-2">
                {
                    shifts.filter((shift, index) => index <= 2).map(shift => (
                        <Shift key={shift.id} data={shift}/>
                    ))
                }

                {
                    shifts.length > 3 &&
                    <List className="d-flex mb-1 p-2" onClick={() => setCalendarDay(true)}>
                        <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{ backgroundColor: '#E7E7E7' }}>
                            <i className="bi bi-three-dots" style={{ color: 'black' }}></i>
                        </div>

                        <div className="d-flex align-items-center ms-2">
                            <p className="mb-0" style={{ fontSize: '13px' }}>More</p>
                        </div>
                    </List>
                    ||
                    shifts.length == 0 &&
                    <List className="p-2">No shift today</List>
                }

                <CalendarDayModal shifts={shifts} date={dateIncludeYear} calendayDay={calendayDay} setCalendarDay={setCalendarDay} holiday={holiday} />
            </div>
        </div>
    )
}

const List = styled.div`
    width: 100%;
    border-radius: 5px;
    cursor: pointer;
    background-color: white;
    font-size: 13px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    &:hover {
        transform: scale(1.05);
        font-weight: bold;
    }
`
