import styled from "styled-components"
import { Modal, Row, Col } from "react-bootstrap"
import { useState } from "react"

const ManageModal = ({ manage, setManage }) => {
    const [disable, setDisable] = useState(true)

    const toggleDisable = () => setDisable(!disable)
    const handleSubmit = e => e.preventDefault()

    return (
        <>
            <Modal show={manage}>
                <ModalContainer>

                    <div className="d-flex justify-content-between align-items-center">
                        <ModalTitle className="my-5">
                            Dennis
                        </ModalTitle>
                        <EditIcon onClick={toggleDisable}
                        ><i className="bi bi-pencil-square" /></EditIcon>
                    </div>
                    <form action="#">
                        <div className="show-grid align-items-center">
                            <Row>
                                {/* left */}
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-1" className="mt-0">Annual Leave</Label>
                                </Col>
                                {/* right */}
                                <Col xs={12} md={6}>
                                    <input type="text" placeholder="28 days/annum"
                                        className="mt-0" id="field-1" disabled={disable}
                                        autoFocus />
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-2">Study Leave</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <input type="text" placeholder="30 days/annum"
                                        className="mt-2" id="field-2" disabled={disable} />
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-3">Hours Required</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <input type="text" placeholder="45 hours/week"
                                        className="mt-2" id="field-3" disabled={disable} />
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-4">Level</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <Select className="mt-2" id="field-4" disabled={disable}>
                                        <option value="default_level" defaultValue>-Please select-</option>
                                        <option value="junior">Junior</option>
                                        <option value="senior">Senior</option>
                                        <option value="admin">Admin</option>
                                    </Select>
                                </Col>
                            </Row>

                            <Row>
                                <Col xs={12} md={6}>
                                    <Label htmlFor="field-5">Job Type</Label>
                                </Col>
                                <Col xs={12} md={6}>
                                    <Select className="mt-2" id="field-5" disabled={disable}>
                                        <option value="default_type" defaultValue>-Please select-</option>
                                        <option value="fulltime">Full-Time</option>
                                        <option value="parttime">Part-Time</option>
                                    </Select>
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
    font-size: 18px;
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