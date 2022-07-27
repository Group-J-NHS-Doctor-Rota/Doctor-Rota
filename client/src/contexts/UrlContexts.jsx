import React, { useState, useEffect, useContext } from 'react'

const UrlContext = React.createContext()

export function useUrl() {
    return useContext(UrlContext)
}

export function UrlProvider({ children }) {
    
    function getUrl(){
        const url = process.env.REACT_APP_BASE_URL

        return url
    }

    const value = {
        getUrl
    }

    return (
        <UrlContext.Provider value={value}>
            {children}
        </UrlContext.Provider>
    )
}

