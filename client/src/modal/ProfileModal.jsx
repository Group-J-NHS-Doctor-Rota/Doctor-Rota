import React from 'react'

import { Modal, Form } from 'react-bootstrap'

import '../css/general.css'
import styled from 'styled-components'

export default function ProfileModal({profile, setProfile}){

    return (
        <>
            <Modal show={profile}>
                <Container>
                    <Title className="my-5">Profile</Title>

                    <Form action="#">
                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex">
                                <ColumnName className="me-3">Name:</ColumnName>
                            </div>

                            <div className="d-flex">
                                <Column className="mb-0">Steven Lin</Column>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <div className="d-flex">
                                <ColumnName className="me-3">Staff ID:</ColumnName>
                            </div>

                            <div className="d-flex">
                                <Column className="mb-0">GB12138</Column>
                            </div>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <Form.Label className="d-flex">
                                <ColumnName className="me-3">Department:</ColumnName>
                               
                            </Form.Label>
                            <Form.Control type="text" placeholder="Anaesthesia Department"/>
                        </div>

                        <div className="d-flex align-items-center my-3">
                            <Form.Label className="d-flex">
                                <ColumnName className="me-3">Email:</ColumnName>
                            </Form.Label>
                            <Form.Control type="email" placeholder="email@nbt.nhs.uk"/>
                        </div>

                        <div className="d-flex justify-content-center my-3">
                            <CloseButton className="m-2" onClick={() => setProfile(false)}>
                                Close
                            </CloseButton>

                            <ConfirmButton className="m-2" onClick={() => setProfile(false)}>
                                Update
                            </ConfirmButton>
                        </div>    
                    </Form>
                </Container>
            </Modal>
        </>
    )
}

const Container = styled.div`
    width: 80%;
    margin: 0 auto;
`

const Title = styled.h1`
    font-size: 32px;
    font-weight: bold;
    color: #168082;

    @media (max-width: 575px){
        font-size: 24px;
    }
`

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: white;
    border-radius: 5px;
    border: none;
    color: #168082;
    font-weight: bold;
    padding: 5px 10px;

    @media (max-width: 575px){
        font-size: 16px;
        min-width: 80px;
    }
`

const ConfirmButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: #168082;
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

const ColumnName = styled.h3`
    font-size: 16px;
    font-weight: bold;
    color: #168082;
    margin-bottom: 0px;
`

const Column = styled.p`
    font-size: 16px;
    color: #168082;
    margin-bottom: 0px;
`