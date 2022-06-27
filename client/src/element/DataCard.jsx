import React from 'react'

import styled from 'styled-components'

export default function DataCard(){
    return (
        <div className="row">
            <div className="col-12 col-md-4">
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center">
                    <p style={{position: 'absolute', bottom: '50%', left: '5%'}}>Annual Leave:</p>
                    <Text>
                        <p className="mb-0">XX days</p>
                    </Text>
                </Card>
            </div>
            <div className="col-12 col-md-4">
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center"> 
                    <p style={{position: 'absolute', bottom: '50%', left: '5%'}}>Study Leave:</p>

                    <Text>
                        <p className="mb-0">XX days</p>
                    </Text>
                </Card>
            </div>
            <div className="col-12 col-md-4">     
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center">
                    <p style={{position: 'absolute', bottom: '50%', left: '5%'}}>Working Hours:</p>

                    <Text>
                        <p className="mb-0">XX hours</p>
                    </Text>
                </Card>
            </div>
        </div>
    )
}

const Card = styled.div`
    position: relative;
    width: 100%;
    min-height: 100px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border-radius: 5px;
    border-top: 4px solid #168082;
`

const Text = styled.div`
    p{
        font-size: 32px;
        color: #168082;
    }
`

// position: absolute;
// top: 35%;
// left: 35%;
