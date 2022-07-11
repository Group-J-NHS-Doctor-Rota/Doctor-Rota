import styled from "styled-components"
import { Modal } from "react-bootstrap"

const ManageModal = ({ manage, setManage }) => {
    return (
        <>
            <Modal show={false}>
                <ModalContainer>
                    <ModalTitle>
                        Dennis (staff)
                    </ModalTitle>
                    <div>Work in Progress</div>
                    <div>

                    </div>

                    <div className="d-flex justify-content-center my-3">
                        <CloseButton className="m-2" onClick={() => setManage(false)}>
                            Close
                        </CloseButton>

                        <ConfirmButton className="m-2" onClick={() => setManage(false)}>
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

const ColumnName = styled.h3`
    font-size: 16px;
    font-weight: bold;
    color: #168082;
    margin-bottom: 0px;
`

const RowInfo = styled.p`
    font-size: 18px;
    color: #168082;
    margin-bottom: 0px;
`