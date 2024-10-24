import {useGlobalContext} from "../hooks/useGlobalContext.js";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import CheckoutForm from "../components/forms/CheckoutForm.jsx";
import {Elements} from "@stripe/react-stripe-js";
import { loadStripe } from '@stripe/stripe-js';

const MembershipPage = () => {
    const { user } = useGlobalContext()
    const stripePromise = loadStripe(
        'pk_test_51QD8akFDvrnvLO6Q70i4UqzN6zqQ3G9qCOQQGrzkEuFbiom9v' +
        'sf6qVQ8cGC0f3m7hzqmVdhJkJ8hrqF7hjUqoHh600nkbdvpy6'
    );
    const [checkout, setCheckout ] = useState(false);
    useEffect(() => {
        console.log(user)
    }, [user]);
    return(
        <div>
            {checkout &&
                <Elements stripe={stripePromise}>
                    <CheckoutForm />
                </Elements>}
            {user.token ? (
                <Link to={'/payment'} >
                <button >
                    Become a member now
                </button>
                </Link>
             ):(
                 <Link to={'/account'} >
                     <button >
                         Register first
                     </button>
                 </Link>
            )}
        </div>
    )
}
export default MembershipPage