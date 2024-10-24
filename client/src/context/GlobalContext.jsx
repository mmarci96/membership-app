import { createContext } from 'react';
import useAuthToken from '../hooks/useAuthToken.js';

export const GlobalContext = createContext(null);

export const GlobalContextProvider = ({ children }) => {
    const auth = useAuthToken();

    return (
        <GlobalContext.Provider value={auth}>{children}</GlobalContext.Provider>
    );
};