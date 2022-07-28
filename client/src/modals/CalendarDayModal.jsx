import React from 'react'

import { Modal } from 'react-bootstrap'

import Shift from '../components/Shift'

import styled from "styled-components"

export default function CalendarDayModal({ shifts, date, calendayDay, setCalendarDay, holiday }){

    return (
        <>
            <Modal show={calendayDay}>
                <ModalContainer>
                    {
                        holiday &&
                        <div className="d-block">
                            <ModalTitle className="mt-5">{date}</ModalTitle>
                            <ModalTitle className="mb-5">{holiday}</ModalTitle>
                        </div>
                        ||
                        <ModalTitle className="my-5">{date}</ModalTitle>
                    }

                    <ShiftRegion className="p-4">
                    {
                        shifts.map(shift => (
                            <Shift key={shift.id} data={shift}/>
                        ))
                    }
                    </ShiftRegion>


                    <div className="d-flex justify-content-center my-3">
                        <ConfirmButton className="m-2" onClick={() => setCalendarDay(false)}>
                            OK
                        </ConfirmButton>
                    </div>
                </ModalContainer>
            </Modal>
        </>
    )

}

const ModalContainer = styled.div`
    width: 80%;
    margin: 0 auto;
`

const ModalTitle = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #035eb8;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #035eb8;
    border-radius: 5px;
    border: none;
    color: white;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const ShiftRegion = styled.div`
    overflow: scroll;
    height: 400px;
    border-radius: 5px;
    background-color: white;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`