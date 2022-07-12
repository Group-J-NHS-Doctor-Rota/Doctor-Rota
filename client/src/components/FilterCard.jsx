import React, { useState } from 'react'

import useLocalStorage from '../hook/useLocalStorage'
import FormRadio from './FormRadio';

import styled from 'styled-components'

export default function FilterCard({ setOpen }){
    const [values, setValues] = useLocalStorage('filter', {
        rota_type: "",
        shifts_type: "",
        member: ""
    });

    const rotaTypes = [
        {
            id: 'first_on',
            name: 'rota_type',
            value: 'first_on',
            label: 'First on'
        },{
            id: 'obstetric',
            name: 'rota_type',
            value: 'obstetric',
            label: 'Obstetric'
        },{
            id: 'second_on',
            name: 'rota_type',
            value: 'second_on',
            label: 'Second on'
        },{
            id: 'third_on',
            name: 'rota_type',
            value: 'third_on',
            label: 'Third on'
        }
    ]

    const typesShift = [
        {
            id: 'day',
            name: 'shifts_type',
            value: 'day',
            label: 'Day shifts'
        },{
            id: 'night',
            name: 'shifts_type',
            value: 'night',
            label: 'Night shifts'
        },{
            id: 'call',
            name: 'shifts_type',
            value: 'call',
            label: 'On Call'
        }
    ]

    const members = [
        {
            id: 'individual',
            name: 'member',
            value: 'individual',
            label: 'Individual'
        },{
            id: 'trainees',
            name: 'member',
            value: 'trainees',
            label: 'Trainees'
        },{
            id: 'admin',
            name: 'member',
            value: 'admin',
            label: 'Admin view'
        },{
            id: 'all',
            name: 'member',
            value: 'all',
            label: 'All'
        }
    ]

    const handleClick = (e) => {
        setValues({ ...values, [e.target.name]: e.target.value });
    };

    return(
        <div className="d-flex justify-content-end">
            <FilterRegion>
                <form action="#">
                    <FilterTitle className="my-1">Rota type</FilterTitle>
                    <div className="d-block">
                        {/* <div className="d-flex align-items-center my-1">
                            {
                                values.rota_type === 'first_on' &&
                                <input type="radio" id="first_on" name="rota_type" value="first_on" onClick={(e) => handleClick(e)} defaultChecked/>
                                ||
                                <input type="radio" id="first_on" name="rota_type" value="first_on" onClick={(e) => handleClick(e)}/>
                            }
                            <label htmlFor="first_on" className="ms-2">First on</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="obstetric" name="rota_type" value="obstetric" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="obstetric" className="ms-2">Obstetric</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="second_on" name="rota_type" value="second_on" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="second_on" className="ms-2">Second on</label>
                        </div>
                                    
                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="third_on" name="rota_type" value="third_on" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="third_on" className="ms-2">Third on</label>
                        </div> */}

                        {
                            rotaTypes.map((rotaType)=>(
                                <FormRadio 
                                    key={rotaType.id}
                                    {...rotaType}
                                    values={values}
                                    handleClick={handleClick}
                                />
                            ))
                        }
                    </div>

                    <FilterTitle className="my-1">Types of shift</FilterTitle>
                    <div className="d-block">
                        {/* <div className="d-flex align-items-center my-1">
                            <input type="radio" id="day" name="shifts_type" value="day" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="day" className="ms-2">Day shifts</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="night" name="shifts_type" value="night" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="night" className="ms-2">Night shifts</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="call" name="shifts_type" value="call" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="call" className="ms-2">On Call</label>
                        </div> */}

                        {
                            typesShift.map((typeShift)=>(
                                <FormRadio 
                                    key={typeShift.id}
                                    {...typeShift}
                                    values={values}
                                    handleClick={handleClick}
                                />
                            ))
                        }
                    </div>

                    <FilterTitle className="my-1">Members</FilterTitle>
                    <div className="d-block">
                        {/* <div className="d-flex align-items-center my-1">
                            <input type="radio" id="individual" name="member" value="individual" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="individual" className="ms-2">Individual</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="trainees" name="member" value="trainees" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="trainees" className="ms-2">Trainees</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="admin" name="member" value="admin" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="admin" className="ms-2">Admin view</label>
                        </div>

                        <div className="d-flex align-items-center my-1">
                            <input type="radio" id="all" name="member" value="all" onClick={(e) => handleClick(e)}/>
                            <label htmlFor="all" className="ms-2">All</label>
                        </div> */}

                        {
                            members.map((member)=>(
                                <FormRadio 
                                    key={member.id}
                                    {...member}
                                    values={values}
                                    handleClick={handleClick}
                                />
                            ))
                        }
                    </div>

                    <div className="d-flex justify-content-center mt-3">
                        <Button type="submit" onClick={() => setOpen(false)}>Submit</Button>
                    </div>
                </form>
            </FilterRegion>
        </div>
    )
}

const FilterRegion = styled.div`
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

const Button = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #168082;
    border: none;
    border-radius: 5px;
`