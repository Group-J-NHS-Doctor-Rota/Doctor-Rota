import styled from "styled-components"
import { Modal, Row, Col } from "react-bootstrap"
import { useState, useEffect } from "react"

const ManageModal = ({ accountId, manage, setManage }) => {
    const [disable, setDisable] = useState(true)
    const [accountDetail, setAccountDetail] = useState()

    const toggleDisable = () => setDisable(!disable)
    const handleSubmit = e => e.preventDefault()

    useEffect(() => {
        if(accountId != undefined){
            fetch(`https://doctor-rota-spring-develop.herokuapp.com/account/${accountId}`, {
                mode: 'cors',
                method: "GET",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                setAccountDetail(data)
            })
        }        
    }, [accountId])

    return (
        <>
            <Modal show={manage}>
                <ModalContainer>

                    <div className="d-flex justify-content-between align-items-center">
                        <ModalTitle className="my-5">
                            {   
                                accountDetail != undefined && accountDetail.username != undefined &&
                                accountDetail.username
                            }
                        </ModalTitle>
                        <EditIcon onClick={toggleDisable}
                        ><i className="bi bi-pencil-square" /></EditIcon>
                    </div>
                    <form action="#">
                        <div className="show-grid align-items-center">
                            <Row>
                                {/* left */}
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-1" className="mt-0">Annual Leave Entitled</Label>
                                </Col>
                                {/* right */}

                                {
                                    accountDetail != undefined && accountDetail.annualLeave != undefined &&
                                    <Col xs={12} md={6}>
                                        <input type="text" placeholder={accountDetail.annualLeave}
                                            className="mt-0" id="field-1" disabled={disable}
                                            autoFocus />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-2">Study Leave Entitled</Label>
                                </Col>
                                
                                {
                                    accountDetail != undefined && accountDetail.studyLeave != undefined &&
                                    <Col xs={12} md={6}>
                                        <input type="text" placeholder={accountDetail.studyLeave}
                                            className="mt-2" id="field-2" disabled={disable} />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-3">Hours Required</Label>
                                </Col>
                
                                {
                                    accountDetail != undefined && accountDetail.workingHours != undefined &&
                                    <Col xs={12} md={6}>
                                        <input type="text" placeholder={accountDetail.workingHours}
                                            className="mt-2" id="field-3" disabled={disable} />
                                    </Col>
                                }
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-3">WTE/FTE</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <input type="text" placeholder="0.6 LTFT"
                                        className="mt-2" id="field-3" disabled={disable} />
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-4">Account Type</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    {
                                        accountDetail != undefined && accountDetail.level == 1 && 
                                        <Select className="mt-2" id="field-4" disabled={disable}>
                                            <option value="junior">Standard</option>
                                            <option value="admin" selected>Admin</option>
                                        </Select>
                                        ||
                                        accountDetail != undefined && accountDetail.level == 0 && 
                                        <Select className="mt-2" id="field-4" disabled={disable}>
                                            <option value="junior" selected>Standard</option>
                                            <option value="admin">Admin</option>
                                        </Select>
                                    }
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-6">Rota Type</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <Select className="mt-2" id="field-6" disabled={disable}>
                                        <option value="default_rota" defaultValue>-Please select-</option>
                                        <option value="first_on">First On</option>
                                        <option value="obstetric">Obstetric</option>
                                        <option value="third_on">Third On</option>
                                        <option value="fourth_on">Fourth On</option>
                                    </Select>
                                </Col>
                            </Row>
                        </div>
                    </form>

                    <div className="d-flex justify-content-center my-3">
                        <CloseButton className="m-2" onClick={() => {
                            setManage(false)
                            setDisable(true)
                        }}>
                            Close
                        </CloseButton>

                        <ConfirmButton className="m-2" onClick={() => {
                            setManage(false)
                            handleSubmit()
                        }}>
                            Update
                        </ConfirmButton>
                    </div>
                </ModalContainer>
            </Modal>
        </>
    )
}

export default ManageModal

const ModalContainer = styled.div`
    width: 80%;
    margin: 0 auto;
`

const ModalTitle = styled.h1`
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

const Label = styled.label`
    display: block;
    font-size: 17px;
    font-weight: bold;
    color: #168082;
    margin: 13px 0;
`

const EditIcon = styled.div`
    font-size: 30px;
    color: #168082;
    font-weight: bold;
    cursor: pointer;
`

const Select = styled.select`
    font-size: 16px;
    height: 36px;
    width: 100%;
    margin-bottom: 6px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`