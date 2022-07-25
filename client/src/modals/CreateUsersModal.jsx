import { Modal, Form } from "react-bootstrap"
import styled from "styled-components"
import { useState, useEffect } from "react"

const CreateUsersModal = ({ create, setCreate }) => {
    const [userList, setUserList] = useState([
        { username: '', email: '' },
    ])
    const onChange = (e, index) => {
        const list = [...userList]
        list[index][e.target.name] = e.target.value
        // setUserList({ ...userList, [e.target.name]: e.target.value })
        setUserList(list)
    }

    const handleAddInput = () => {
        setUserList([...userList, { username: '', email: '' }])
    }

    const handleRemoveInput = index => {
        const list = [...userList]
        list.splice(index, 1)
        setUserList(list)
    }


    return (
        <>
            <Modal show={create}>
                <ModalContainer>
                    <div className="d-flex justify-content-between">
                        <ModalTitle className="my-5">Create Users</ModalTitle>
                    </div>

                    {userList.map((user, index) => {
                        return (
                            index + 1 <= 10 &&
                            (<>
                                <div key={index} className="d-block mb-3">
                                    <div className="d-flex justify-content-between align-items-center">
                                        <ColumnName>New User&nbsp;{index + 1}</ColumnName>
                                        <div className="d-flex justify-content-center align-items-center">
                                            {
                                                userList.length - 1 === index &&
                                                (
                                                    <>
                                                        <Icon className="bi bi-plus-circle"
                                                            onClick={index => handleAddInput(index)} />
                                                    </>
                                                )
                                            }
                                            {
                                                userList.length !== 1 &&
                                                (
                                                    <>
                                                        <Icon className="bi bi-dash-circle"
                                                            onClick={handleRemoveInput} />
                                                    </>
                                                )
                                            }
                                        </div>
                                    </div>
                                    <Input
                                        className="my-1"
                                        type="text"
                                        placeholder="Username"
                                        name="username"
                                        value={user.username}
                                        autoComplete="off"
                                        onChange={e => onChange(e, index)} />
                                    <Input
                                        className="my-1"
                                        type="text"
                                        placeholder="Email"
                                        name="email"
                                        value={user.email}
                                        autoComplete="off"
                                        onChange={e => onChange(e, index)} />
                                </div>
                            </>
                            )
                            || <p style={{ color: 'red' }}>The maximum is 10 users in one go!</p>
                        )
                    })}

                    <div className="d-flex justify-content-center my-5">
                        <CloseButton className="m-2" onClick={() => setCreate(false)}>
                            Close
                        </CloseButton>
                        <ConfirmButton className="m-2" onClick={() => setCreate(false)}>
                            Create
                        </ConfirmButton>
                    </div>
                </ModalContainer>
            </Modal>
        </>
    )
}

export default CreateUsersModal

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

const CloseButton = styled.button`
    min-width: 100px;
    font-size: 20px;
    background-color: white;
    border-radius: 5px;
    border: none;
    color: #035eb8;
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

const ColumnName = styled.h3`
    font-size: 16px;
    font-weight: bold;
    color: #035eb8;
    margin-bottom: 0px;
`

const Input = styled.input`
    font-size: 16px !important;
    width: 100%;
    height: 36px;
    border: none;
    border-radius: 5px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

const Icon = styled.i`
    font-size: 26px;
    margin-left: 12px;
    cursor: pointer;
`