import styled from "styled-components"
import Pagination from "../components/Pagination"
import { useState } from "react"
import LeaveDetailModal from "../modals/LeaveDetailModal"

const NotificationPage = () => {
    // only admin make decisions
    const isAdmin = true
    // const isAdmin = false

    // decision updated from database by admin
    const [decision, setDecision] = useState("Rejected")
    // const [decision, setDecision] = useState("Approved")
    // const [decision, setDecision] = useState("Pending")

    // for normal user view
    const DecisionButton = styled.button`
        background-color: ${() =>
            decision === "Pending" ? "#E7E7E7"
                : (decision === "Approved" ? "#EDFCF9" : (decision === "Rejected" ? "#FFE2E2" : "purple"))
        };
        color: ${() =>
            decision === "Pending" ? '#000000'
                : (decision === "Approved" ? '#168082' : (decision === "Rejected" ? '#B01C2E' : '#ffffff'))
        };
        border: none;
        border-radius: 4px;
        `

    const [leaveDetail, setLeaveDetail] = useState(false);

    const getContent = number => {
        let content = [];
        for (let i = 1; i <= number; i++) {
            content.push(
                <AlertMessage key={i} className="mt-3">
                    <div className="d-flex">
                        <SenderTag className="col-sm-1">
                            {((i % 2 === 0) && <p className="mb-0">Dennis</p>) || <p className="mb-0">Admin</p>}
                        </SenderTag>
                    </div>
                    <MessageInfo className="d-flex col-12 pe-2 justify-content-between">
                        <div className="pt-4 ps-3 col-sm-3">
                            {(((i % 3) === 0) && <p>2022/07/28 - 2022/08/01</p>)
                                || ((i % 3 === 1) && <p>2022/07/21 - 2022/07/21</p>)
                                || ((i % 3 === 2) && <p>2022/07/15 - 2022/07/19</p>)}
                        </div>
                        <div className="pt-4 col-sm-2" onClick={() => setLeaveDetail(true)}
                            style={{ cursor: 'pointer' }}>
                            {(((i % 3) === 0) && <p>Study Leave&nbsp;&nbsp;
                                <i className="bi bi-search" /></p>)
                                || ((i % 3 === 1) && <p>NOC Request&nbsp;&nbsp;
                                    <i className="bi bi-search" /></p>)
                                || ((i % 3 === 2) && <p>Annual Leave&nbsp;&nbsp;
                                    <i className="bi bi-search" /></p>)}
                        </div>
                        {
                            (isAdmin &&
                                (
                                    <>
                                        <div className="fw-bold">
                                            <DenialButton className="my-3"
                                                onClick={() => setDecision("Rejected")}>
                                                Reject
                                            </DenialButton>
                                            <ApprovalButton className="my-3"
                                                onClick={() => setDecision("Approved")}>
                                                Approve
                                            </ApprovalButton>

                                            {/* <ViewButton className="my-3"
                                                onClick={() => setLeaveDetail(true)}>
                                                View
                                            </ViewButton> */}
                                        </div>
                                    </>
                                ))
                            || (
                                <>
                                    <div className="col-sm-2 fw-bold" />
                                    <div className="col-sm-2 fw-bold">
                                        <DecisionButton className="my-3 mx-2 p-2">
                                            {decision}
                                        </DecisionButton></div>
                                </>
                            )
                        }
                    </MessageInfo >
                </AlertMessage >
            )
        }
        return content;
    }


    const [currentPage, setCurrentPage] = useState(1)
    return (
        <div>
            <PageContainer>
                {getContent(6)}
                <LeaveDetailModal leaveDetail={leaveDetail} setLeaveDetail={setLeaveDetail} />
            </PageContainer>
            <Pagination currentPage={currentPage} totalPage={10} setCurrentPage={setCurrentPage} />
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
`

const SenderTag = styled.div`
    font-size: 16px;
    color: #FFFFFF;
    background-color: #168082;
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
    text-align: center;
    min-height: 16px;
    font-weight: 600;
    padding-top: 5px;

    @media (max-width: 575px){
        font-size: 12px
    }
`

const MessageInfo = styled.div`
    font-size: 18px;
    border-top: 5px solid #168082;
    width: 100%;
    min-height: 62px;
    font-weight: 500;
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`

// for admin view
const DenialButton = styled.button`
    background-color: #FFE2E2;
    color: #B01C2E;
    border: none;
    border-radius: 4px;
`

const ApprovalButton = styled.button`
    background-color: #EDFCF9;
    color: #168082;
    border: none;
    border-radius: 4px;
`

// const ViewButton = styled.button`
//     background-color: lightyellow;
//     color: orange;
//     border: none;
//     border-radius: 4px;
// `