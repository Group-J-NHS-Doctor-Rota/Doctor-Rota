import styled from "styled-components"

const DataBar = () => {
    return (
        <>
            <div className="row">
                <div className="col-md-4">
                    <DataCard className="d-flex mt-3 justify-content-center align-items-center">
                        <DataCardTitle>Annual Leave</DataCardTitle>
                        <CardText>
                            <p className="mb-0">11.5 days</p>
                        </CardText>
                    </DataCard>
                </div>
                <div className="col-md-4">
                    <DataCard className="d-flex mt-3 justify-content-center align-items-center">
                        <DataCardTitle>Study Leave</DataCardTitle>
                        <CardText>
                            <p className="mb-0">7 days</p>
                        </CardText>
                    </DataCard>
                </div>
                <div className="col-md-4">
                    <DataCard className="d-flex mt-3 justify-content-center align-items-center">
                        <DataCardTitle>Weekly Worked</DataCardTitle>
                        <CardText>
                            <p className="mb-0">27 hours</p>
                        </CardText>
                    </DataCard>
                </div>
            </div>
        </>
    )

}

export default DataBar

const DataCard = styled.div`
    position:relative;
    width: 100%;
    min-height: 100px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    border-top: 4px solid #168082;
`
const DataCardTitle = styled.div`
    position: absolute;
    top: 8%;
    left: 5%;
`

const CardText = styled.div`
    p {
        font-size: 32px;
        color: #168082;
    }
`