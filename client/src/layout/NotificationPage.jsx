import styled from "styled-components"
import Pagination from "../components/Pagination"
import { useState, useEffect } from "react"
import LeaveDetailModal from "../modals/LeaveDetailModal"

const NotificationPage = () => {
    const [totalPage, setTotalPage] = useState(0)
    const [currentPage, setCurrentPage] = useState(1)
    const [notifications, setNotifications] = useState()
    const [leaveDetail, setLeaveDetail] = useState(false);
    const accountId = 1


    useEffect(() => {
        const url = `https://doctor-rota-spring-develop.herokuapp.com/notification?accountId=${accountId}`

        fetch(url, {
            mode: 'cors',
            method: "GET",
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            }
        })
            .then(response => response.json())
            .then(data => {
                const totalNumber = data.leaveRequests.length
                setTotalPage(totalNumber / 6)
                setNotifications(data.leaveRequests)
            })
    }, [])

    function handleClick() {
        console.log(12)
        setLeaveDetail(true)
    }

    const getContent = () => {
        let content = [];

        notifications.map((notification) => {
            content.push(
                <AlertMessage key={notification.id} className="mt-3">
                    <div className="d-flex">
                        <SenderTag className="px-4">
                            <p className="mb-0">Admin</p>
                        </SenderTag>
                    </div>
                    <MessageInfo className="d-flex px-4 col-12 justify-content-between" onClick={() => handleClick()}>
                        <div className="py-3">
                            <p className="mb-0">{notification.date}</p>
                        </div>

                        <div className="py-3">
                            {
                                notification.type == 0 &&
                                <p className="mb-0">Annual Leave</p>
                                || notification.type == 1 &&
                                <p className="mb-0">Study Leave</p>
                                || notification.type == 2 &&
                                <p className="mb-0">NOC Request</p>
                                || notification.type == 9 &&
                                <p className="mb-0">Other</p>
                            }
                        </div>

                        <div className="py-3">
                            {
                                notification.accountId == 1 && notification.status == 1 &&
                                <ApprovalButton disabled>Approved</ApprovalButton>
                                || notification.accountId == 1 && notification.status == 2 &&
                                <DenialButton disabled>Rejected</DenialButton>
                                || notification.accountId == 1 && notification.status == 0 &&
                                <PendingButton disabled>Pending</PendingButton>
                                || notification.accountId != 1 && notification.status == 0 &&
                                <div className="d-flex">
                                    <DenialButton className="me-1 px-2">
                                        Reject
                                    </DenialButton>
                                    <ApprovalButton className="ms-1 px-2">
                                        Approve
                                    </ApprovalButton>
                                </div>
                            }
                        </div>
                    </MessageInfo >
                </AlertMessage >
            )
        })
        return content;
    }

    return (
        <div>
            <PageContainer>
                {
                    notifications != undefined &&
                    getContent()
                }
                <LeaveDetailModal leaveDetail={leaveDetail} setLeaveDetail={setLeaveDetail} />
            </PageContainer>
            <Pagination currentPage={currentPage} totalPage={totalPage} setCurrentPage={setCurrentPage} />
        </div>
    )
}

export default NotificationPage

const PageContainer = styled.div`
    margin: 0 auto;
    width: 90%;
`

const AlertMessage = styled.div`
    width: 100%;

    &:hover {
        transform: scale(1.02);
    }
`

const SenderTag = styled.div`
    font-size: 16px;
    color: #FFFFFF;
    background-color: #035eb8;
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
    text-align: center;
    min-height: 16px;
    font-weight: 600;
    padding-top: 5px;

    @media (max-width: 575px){
        font-size: 13px
    }
`

const MessageInfo = styled.div`
    font-size: 16px;
    border-top: 4px solid #035eb8;
    width: 100%;
    min-height: 60px;
    cursor: pointer;
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
    border-top-right-radius: 4px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 4px 2px 0 rgba(0, 0, 0, 0.01);

    @media (max-width: 575px){
        font-size: 13px
    }
`

const DenialButton = styled.button`
    background-color: #FFE2E2;
    min-width: 85px;
    color: #B01C2E;
    border: none;
    border-radius: 4px;

    @media (max-width: 575px){
        min-width: 70px;
        font-size: 13px
    }
`

const ApprovalButton = styled.button`
    background-color: #EDFCF9;
    min-width: 85px;
    color: #168082;
    border: none;
    border-radius: 4px;

    @media (max-width: 575px){
        min-width: 70px;
        font-size: 13px
    }
`

const PendingButton = styled.button`
    background-color: #E7E7E7;
    min-width: 85px;
    color: black;
    border: none;
    border-radius: 4px;

    @media (max-width: 575px){
        min-width: 70px;
        font-size: 13px
    }
`
