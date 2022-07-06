import React, { useState } from 'react'

import Navbar from '../element/Navbar'
import DataCard from '../element/DataCard'
import CalendarTable from '../element/CalendarTable'

import styled from 'styled-components'

export default function Homgepage(){
    const today = new Date()

    const [year, setYear] = useState(today.getFullYear())
    const [month, setMonth] = useState(today.getMonth()+1)
    
    function handleNextMonth(){
        if(month == 12){
            setMonth(1)
            setYear(year+1)
        }else{
            setMonth(month+1)
        }
    }

    function handlePreviousMonth(){
        if(month == 1){
            setMonth(12)
            setYear(year-1)
        }else{
            setMonth(month-1)
        }
    }

    return (
        <div>
            <Navbar />

            <Container>
                <DataCard />

                {/* Switch Calendar */}
                <div className="d-flex align-items-center mt-3">
                    <div className="m-1">
                        <Icon className="bi bi-caret-left-fill" style={{cursor: 'pointer'}} onClick={() => handlePreviousMonth()}></Icon>
                    </div>
                    <div className="m-1">
                        <p className="mb-0">{year}/{month}</p>
                    </div>
                    <div className="m-1">
                        <Icon className="bi bi-caret-right-fill" style={{cursor: 'pointer'}} onClick={() => handleNextMonth()}></Icon>
                    </div>
                </div>

                <Week className="my-2">
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Mon</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Tue</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Wed</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Thu</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Fri</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Sat</h3></div>
                    <div className="d-flex justify-content-center"><h3 className="fs-5">Sun</h3></div>
                </Week>
                
                <CalendarTable year={year} month={month} />
            </Container>
        </div>
    )
}

const Container = styled.div`
    margin: 0 auto;
    width: 90%;
`

const Icon = styled.i`
    &:hover{
        color: #168082;
    }
`

const Week = styled.div`    
    display: none;
    @media (min-width: 915px){
        display: grid;
        grid-template-columns: repeat(7, 1fr)
    }
`

