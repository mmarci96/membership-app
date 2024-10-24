import { useContext } from 'react';
import { GlobalContext } from '../context/GlobalContext.jsx';

export const useGlobalContext = () => {
    return useContext(GlobalContext);
};