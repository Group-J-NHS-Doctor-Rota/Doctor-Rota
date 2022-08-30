import React, { useState, useEffect } from 'react'

import { useUrl } from '../contexts/UrlContexts' 

import styled from 'styled-components'

export default function DataBar() {
    const [values, setValues] = useState({
        annualLeave: 0,
        studyLeave: 0
    })

    const auth = JSON.parse(localStorage.getItem('auth'))

    const { getUrl } = useUrl()

    const url =  getUrl()

    useEffect(() => {
        if(url != undefined){
            const newUrl = `${url}leaves?accountId=${auth.id}`
            
            fetch(newUrl, {
                mode: 'cors',
                method: 'GET',
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'token': auth.token
                }
            })
            .then(response => response.json())
            .then(data => setValues({
                annualLeave: data.annualLeave,
                studyLeave: data.studyLeave
            }))
        }
    }, [])

    return (
        <div className="row">
            <div className="col-12 col-md-4">
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center">
                    <p style={{ position: 'absolute', bottom: '50%', left: '5%' }}>Annual Leave Remaining</p>
                    <Text>
                        <p className="mb-0">{values.annualLeave} days</p>
                    </Text>
                </Card>
            </div>
            <div className="col-12 col-md-4">
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center">
                    <p style={{ position: 'absolute', bottom: '50%', left: '5%' }}>Study Leave Remaining</p>

                    <Text>
                        <p className="mb-0">{values.studyLeave} days</p>
                    </Text>
                </Card>
            </div>
            <div className="col-12 col-md-4">
                <Card className="mt-3 p-3 d-flex justify-content-center align-items-center">
                    <p style={{ position: 'absolute', bottom: '50%', left: '5%' }}>Avg Weekly Hours</p>

                    <Text>
                        <p className="mb-0">48 hours</p>
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
    border-top: 4px solid #035eb8;
`

const Text = styled.div`
    p{
        font-size: 32px;
        color: #035eb8;
    }
`
