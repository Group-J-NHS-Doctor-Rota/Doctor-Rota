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
        {   id: "normal",
            name: "shifts_type",
            value: "normal",
            label: "Normal day"
        },{
            id: 'night',
            name: 'shifts_type',
            value: 'night',
            label: 'Night shifts'
        },{
            id: "long",
            name: "shifts_type",
            value: "long",
            label: "Long day"
        },{
            id: "off day",
            name: "shifts_type",
            value: "off day",
            label: "Trainee off day"
        },{
            id: "leave",
            name: "shifts_type",
            value: "leave",
            label: "On leave"
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