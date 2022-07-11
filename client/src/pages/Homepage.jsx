import React, { useState } from 'react'

import DataBar from '../components/DataBar'
import CalendarTable from '../components/CalendarTable'
import styled from 'styled-components'

const Homepage = () => {
    const today = new Date()
    console.log(today)
    const [year, setYear] = useState(today.getFullYear())
    const [month, setMonth] = useState(today.getMonth() + 1)
    const [openFilter, setFilterOpen] = useState(false)

    // calculate next
    const handleNextMonth = () => {
        // '===' instead of '=='
        if (month === 12) { setMonth(1); setYear(year + 1) }
        else { setMonth(month + 1) }

    }
    // calculate previous month
    const handlePreviousMonth = () => {
        if (month === 1) { setMonth(12); setYear(year - 1) }
        else { setMonth(month - 1) }
    }

    return (
        <div>
            <HomeContainer>
                <DataBar />

                {/* switch month and filter line */}
                <div className="d-flex justify-content-between align-items center mt-3" style={{ position: 'relative' }}>
                    <div className="d-flex align-items-center">
                        <div className="m-1">
                            <NavMonthIcon className="bi bi-caret-left-fill"
                                style={{ cursor: 'pointer' }}
                                onClick={() => handlePreviousMonth()} />
                        </div>
                        <div className="m-1">
                            <p className="mb-0">{year}/{month}</p>
                        </div>
                        <div className="m-1">
                            <NavMonthIcon className="bi bi-caret-right-fill"
                                style={{ cursor: 'pointer' }}
                                onClick={() => handleNextMonth()} />
                        </div>
                    </div>

                    <div style={{ fontSize: '24px' }}>
                        <i className="bi bi-funnel-fill" style={{ cursor: 'pointer' }}
                            onClick={() => setFilterOpen(!openFilter)} />
                    </div>
                </div>

                {
                    openFilter &&
                    <div className="d-flex justify-content-end">
                        <FilterCard>
                            <form action="#">
                                {/* rota type */}
                                <FilterTitle className="my-1">Rota Type</FilterTitle>
                                <div className="d-block">
                                    <div>
                                        <input type="radio" id="first_on"
                                            name="rota_type" value="first_on" />
                                        <label className="ms-2" htmlFor="first_on">First On</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="obstetric"
                                            name="rota_type" value="obstetric" />
                                        <label className="ms-2" htmlFor="obstetric">Obstetric</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="second_on"
                                            name="rota_type" value="second_on" />
                                        <label className="ms-2" htmlFor="second_on">Second On</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="third_on"
                                            name="rota_type" value="third_on" />
                                        <label className="ms-2" htmlFor="third_on">Third_on</label>
                                    </div>
                                </div>

                                {/* shift type */}
                                <FilterTitle className="my-1">Shift Type</FilterTitle>
                                <div className="d-block">
                                    <div>
                                        <input type="radio" id="day_shift"
                                            name="rota_type" value="day_shift" />
                                        <label className="ms-2" htmlFor="day_shift">Day</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="night_shift"
                                            name="rota_type" value="night_shift" />
                                        <label className="ms-2" htmlFor="night_shift">Night</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="on_call"
                                            name="rota_type" value="on_call" />
                                        <label className="ms-2" htmlFor="on_call">On Call</label>
                                    </div>
                                </div>

                                {/* Personnel Scope */}
                                <FilterTitle className="my-1">Rota Type</FilterTitle>
                                <div className="d-block">
                                    <div>
                                        <input type="radio" id="self"
                                            name="rota_type" value="self" />
                                        <label className="ms-2" htmlFor="self">Only Me</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="trainees"
                                            name="rota_type" value="trainees" />
                                        <label className="ms-2" htmlFor="trainees">Trainees</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="admin"
                                            name="rota_type" value="admin" />
                                        <label className="ms-2" htmlFor="admin">All(admin)</label>
                                    </div>
                                    <div>
                                        <input type="radio" id="all_staff"
                                            name="rota_type" value="all_staff" />
                                        <label className="ms-2" htmlFor="all_staff">All(staff)</label>
                                    </div>
                                </div>
                            </form>
                        </FilterCard>
                    </div>
                }

                <WeekBar className="my-2">
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Mon</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Tue</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Wed</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Thu</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Fri</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Sat</h3></div>
                    <div className="d-flex justify-content-center">
                        <h3 className="fs-5">Sun</h3></div>
                </WeekBar>

                <CalendarTable year={year} month={month} />
                <Footer className='mt-3' />
            </HomeContainer>
        </div>
    )
}

export default Homepage

// it accepts '.scss' style
const HomeContainer = styled.div`
    margin: 0 auto;
    width: 90%;
`

const NavMonthIcon = styled.i`
    &:hover{
        color: #168082;
    }
`

const FilterCard = styled.div`
    position: absolute;
    z-index: 99;
    padding: 20px 30px;
    min-width: 260px;
    min-height: 250px;
    background-color: #EDFCF9;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
`
const FilterTitle = styled.p`
    font-size: 20px;
    color: #168082;
    font-weight: bold;
`

const WeekBar = styled.div`
    display: none;

    @media (min-width: 915px) {
        display: grid;
        grid-template-columns: repeat(7, 1fr)
    }
`
const Footer = styled.div`
    width: 100%;
    height: 80px;
    background-color: #fff;
`