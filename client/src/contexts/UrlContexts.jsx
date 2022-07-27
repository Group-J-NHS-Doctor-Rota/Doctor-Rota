import React, { useState, useEffect, useContext } from 'react'

const UrlContext = React.createContext()

export function useUrl() {
    return useContext(UrlContext)
}

export function UrlProvider({ children }) {
    const [url, setUrl] = useState()
    
    function getUrl(){
        const url = process.env.REACT_APP_BASE_URL
        setUrl(url)
    }

    const value = {
        url,
        getUrl
    }

    return (
        <UrlContext.Provider value={value}>
            {children}
        </UrlContext.Provider>
    )
}

