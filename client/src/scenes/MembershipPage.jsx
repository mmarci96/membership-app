import {useGlobalContext} from "../hooks/useGlobalContext.js";
import {useEffect} from "react";

const MembershipPage = () => {
    const { user } = useGlobalContext()
    useEffect(() => {
        console.log(user)
    }, [user]);
    return(
        <div>
            membership page
        </div>
    )
}
export default MembershipPage