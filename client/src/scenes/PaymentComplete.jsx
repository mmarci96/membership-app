import { useLocation } from 'react-router-dom';
import {useEffect} from "react";
import {useGlobalContext} from "../hooks/useGlobalContext.js";

const PaymentComplete = () => {
    const { user } = useGlobalContext()
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const paymentIntent = queryParams.get('payment_intent');
    const clientSecret = queryParams.get('payment_intent_client_secret');
    const redirectStatus = queryParams.get('redirect_status');


    useEffect(() => {
        console.log(clientSecret)
        console.log(paymentIntent)
        console.log(user)
        console.log(redirectStatus)

        if (redirectStatus === 'succeeded', user.userId) {
            // Call API to update membership
            fetch('/api/membership', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                     Authorization: `Bearer ${user.token}`,
                },
                body: JSON.stringify({
                    userId: user.userId,
                    paymentIntent: paymentIntent,
                    paymentStatus: true
                }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Membership updated:', data);
                    // Optionally redirect to another page after success
                })
                .catch(error => {
                    console.error('Error updating membership:', error);
                });
        }
    }, [paymentIntent, redirectStatus, user]);
    return(
        <div>
            Payment {status === 'ACTIVE' ? <>o</> : <>k</>}
        </div>
    )
}

export default PaymentComplete