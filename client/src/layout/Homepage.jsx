import React, { useState } from 'react'

import NavBar from '../components/NavBar'
import DataBar from '../components/DataBar'
import CalendarTable from '../components/CalendarTable'
import FilterCard from '../components/FilterCard'

import styled from 'styled-components'

export default function Homgepage() {
    const today = new Date()

    const [year, setYear] = useState(today.getFullYear())
    const [month, setMonth] = useState(today.getMonth() + 1)
    const [open, setOpen] = useState(false)

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
                            <Icon className="bi bi-caret-left-fill" style={{ cursor: 'pointer' }} onClick={() => handlePreviousMonth()}></Icon>
                        </div>
                        <div className="m-1">
                            <p className="mb-0">{year}/{month}</p>
                        </div>
                        <div className="m-1">
                            <Icon className="bi bi-caret-right-fill" style={{ cursor: 'pointer' }} onClick={() => handleNextMonth()}></Icon>
                        </div>
                    </div>

                    <div style={{ fontSize: '24px' }}>
                        <Icon className="bi bi-funnel-fill" style={{ cursor: 'pointer' }} onClick={() => setOpen(!open)}></Icon>
                    </div>
                </div>

                {
                    open && <FilterCard setOpen={setOpen} />
                    // <div className="d-flex justify-content-end">
                    //     <FilterCard>
                    //         <form action="#">
                    //             <FilterTitle className="my-1">Rota type</FilterTitle>
                    //             <div className="d-block">
                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="first_on" name="rota_type" value="first_on" />
                    //                     <label htmlFor="first_on" className="ms-2">First on</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="obstetric" name="rota_type" value="obstetric" />
                    //                     <label htmlFor="obstetric" className="ms-2">obstetric</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="second_on" name="rota_type" value="second_on" />
                    //                     <label htmlFor="second_on" className="ms-2">Second on</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="third_on" name="rota_type" value="third_on" />
                    //                     <label htmlFor="third_on" className="ms-2">Third on</label>
                    //                 </div>
                    //             </div>
                    //             <FilterTitle className="my-1">Types of shift</FilterTitle>
                    //             <div className="d-block">
                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="day" name="shifts_type" value="day" />
                    //                     <label htmlFor="day" className="ms-2">Day shifts</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="night" name="shifts_type" value="night" />
                    //                     <label htmlFor="night" className="ms-2">Night shifts</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="call" name="shifts_type" value="call" />
                    //                     <label htmlFor="call" className="ms-2">On Call</label>
                    //                 </div>
                    //             </div>

                    //             <FilterTitle className="my-1">Members</FilterTitle>
                    //             <div className="d-block">
                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="individual" name="member" value="individual" />
                    //                     <label htmlFor="individual" className="ms-2">Individual</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="trainees" name="member" value="trainees" />
                    //                     <label htmlFor="trainees" className="ms-2">Trainees</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="admin" name="member" value="admin" />
                    //                     <label htmlFor="admin" className="ms-2">Admin view</label>
                    //                 </div>

                    //                 <div className="d-flex align-items-center my-1">
                    //                     <input type="radio" id="all" name="member" value="all" />
                    //                     <label htmlFor="all" className="ms-2">All</label>
                    //                 </div>
                    //             </div>

                    //             <div className="d-flex justify-content-center mt-3">
                    //                 <Button type="submit">Submit</Button>
                    //             </div>
                    //         </form>
                    //     </FilterCard>
                    // </div>
                }

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

// const FilterCard = styled.div`
//     position: absolute;
//     z-index: 99;
//     padding: 20px 30px;
//     min-width: 260px;
//     min-height: 250px;
//     background-color: #EDFCF9;
//     box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
//     border-radius: 5px;
// `

// const FilterTitle = styled.p`
//     font-size: 20px;
//     color: #168082;
//     font-weight: bold;
// `

// const Button = styled.button`
//     padding: 5px 25px;
//     color: white;
//     font-size: 20px;
//     font-weight: bold;
//     background-color: #168082;
//     border: none;
//     border-radius: 5px;
// `

