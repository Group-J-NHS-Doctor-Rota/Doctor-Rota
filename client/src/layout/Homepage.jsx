import React, { useState, useEffect, useRef } from 'react'

import DataBar from '../components/DataBar'
import CalendarTable from '../components/CalendarTable'
import FilterCard from '../components/FilterCard'

import styled from 'styled-components'

export default function Homgepage() {
    const today = new Date()
    const filterRef = useRef(null)

    const [year, setYear] = useState(today.getFullYear())
    const [month, setMonth] = useState(today.getMonth() + 1)
    const [open, setOpen] = useState(false)
    
    const handleClickOutside = event => {
        if(filterRef.current && !filterRef.current.contains(event.target) && event.target.id != "icon_filter" ){
            setOpen(false)
        }else{
            setOpen(true)
        }
    }

    useEffect(() => {        
        document.addEventListener('click', handleClickOutside)

        return () => document.removeEventListener('click', handleClickOutside)
    }, [])


    function handleNextMonth() {
        if (month == 12) {
            setMonth(1)
            setYear(year + 1)
        } else {
            setMonth(month + 1)
        }
    }

    function handlePreviousMonth() {
        if (month == 1) {
            setMonth(12)
            setYear(year - 1)
        } else {
            setMonth(month - 1)
        }
    }

    return (
        <div>

            <Container>
                <DataBar />

                {/* Switch Calendar */}
                <div className="d-flex justify-content-between align-items-center mt-3" style={{ position: 'relative' }}>
                    <div className="d-flex align-items-center">
                        <div className="m-1">
                            <Icon className="bi bi-caret-left-fill" style={{ fontSize: '20px', cursor: 'pointer' }} onClick={() => handlePreviousMonth()}></Icon>
                        </div>
                        <div className="m-1">
                            <p className="mb-0" style={{ fontSize: '20px' }}>
                                {year}/
                                {
                                    ((month + '').length) === 2 ? month : ('0' + month)
                                }
                            </p>
                        </div>
                        <div className="m-1">
                            <Icon className="bi bi-caret-right-fill" style={{ fontSize: '20px', cursor: 'pointer' }} onClick={() => handleNextMonth()}></Icon>
                        </div>
                    </div>

                    <div style={{ fontSize: '40px' }}>
                        <Icon id="icon_filter" className="bi bi-filter" style={{ cursor: 'pointer' }} onClick={() => setOpen(!open)}></Icon>
                    </div>
                </div>

                <div ref={filterRef}>
                    {
                        open && <FilterCard id="filter_card" open={open} setOpen={setOpen} />
                    }
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
        color: #035eb8;
    }
`

const Week = styled.div`    
    display: none;
    @media (min-width: 915px){
        display: grid;
        grid-template-columns: repeat(7, 1fr)
    }
`