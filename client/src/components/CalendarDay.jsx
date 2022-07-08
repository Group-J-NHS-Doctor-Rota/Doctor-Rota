import React from 'react'

import Shift from './Shift'

import styled from 'styled-components'

export default function CalendarDay(){
    return (
        <div className="d-block px-2">
            <Shift type="night" />
            <Shift type="morning" />
        </div>
    )
}
