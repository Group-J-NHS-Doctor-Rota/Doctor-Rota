import React, { useState, useEffect } from 'react'

import useLocalStorage from '../hook/useLocalStorage'
import FormRadio from './FormRadio';

import styled from 'styled-components'

export default function FilterCard({ open, setOpen }){
    const auth = JSON.parse(localStorage.getItem('auth'))
    
    const [values, setValues] = useLocalStorage('filter', {
        rota_type: "10",
        shifts_type: "10",
        member: "10"
    });

    const rotaTypes = [
        {
            id: 'first_on',
            name: 'rota_type',
            value: "1",
            label: 'First on'
        },{
            id: 'obstetric',
            name: 'rota_type',
            value: "2",
            label: 'Obstetric'
        },{
            id: 'second_on',
            name: 'rota_type',
            value: "3",
            label: 'Second on'
        },{
            id: 'third_on',
            name: 'rota_type',
            value: "4",
            label: 'Third on'
        },{
            id: 'all',
            name: 'rota_type',
            value: "10",
            label: 'All'
        }
    ]

    const typesShift = [
        {   id: "theatre",
            name: "shifts_type",
            value: "0",
            label: "Theatre day"
        },{
            id: 'night',
            name: 'shifts_type',
            value: "2",
            label: 'Night shifts'
        },{
            id: "long",
            name: "shifts_type",
            value: "1",
            label: "Long day"
        },{
            id: "off day",
            name: "shifts_type",
            value: "3",
            label: "Trainee off day"
        },{
            id: 'all',
            name: 'shifts_type',
            value: "10",
            label: 'All'
        }
    ]

    const members = [
        {
            id: 'individual',
            name: 'member',
            value: "0",
            label: 'Individual'
        },{
            id: 'all',
            name: 'member',
            value: "10",
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
                    
                    {
                        auth.level == 1 &&
                        <div>
                            <FilterTitle className="my-1">Members</FilterTitle>
                            <div className="d-block">
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
                        </div>
                    }

                    <div className="d-flex justify-content-center mt-3">
                        <Button type="button" onClick={() => setOpen(false)}>Close</Button>
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
    background-color: #f5f9fe;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
`

const FilterTitle = styled.p`
    font-size: 20px;
    color: #035eb8;
    font-weight: bold;
`

const Button = styled.button`
    padding: 5px 25px;
    color: white;
    font-size: 20px;
    font-weight: bold;
    background-color: #035eb8;
    border: none;
    border-radius: 5px;
`