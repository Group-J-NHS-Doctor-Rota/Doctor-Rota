import React, { useState } from 'react'

import Shift from './Shift'
import CalendarDayModal from '../modals/CalendarDayModal'

import styled from 'styled-components'

export default function CalendarDay({ month, day, holiday }){
    const date = month + "/" + day
    const [calendayDay, setCalendarDay] = useState(false)
    return (
        <div>
            <div className="d-block px-2">
                <Shift type="night" />
                <Shift type="morning" />
                <MoreList className="d-flex mb-1 p-2">
                    <div className="d-flex justify-content-center align-items-center px-1 py-2 rounded" style={{backgroundColor: '#E7E7E7'}}>
                        <i className="bi bi-three-dots" style={{color: 'black'}}></i>
                    </div>

                    <div className="d-flex align-items-center ms-2" onClick={() => setCalendarDay(true)}>
                        <p className="mb-0" style={{fontSize: '13px'}}>More</p>
                    </div>
                </MoreList>
                
                <CalendarDayModal date={date} calendayDay={calendayDay} setCalendarDay={setCalendarDay} holiday={holiday} />
            </div>
        </div>
    )
}

const MoreList = styled.div`
    width: 100%;
    border-radius: 5px;
    cursor: pointer;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);

    &:hover {
        transform: scale(1.05);
        font-weight: bold;
    }
`
