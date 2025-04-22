import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import CheckoutForm from '../components/forms/CheckoutForm.jsx';
import { useState, useEffect } from 'react';
import { useGlobalContext } from "../hooks/useGlobalContext.js";

const PaymentPage = () => {
    const stripePublicKey = 'pk_test_51H59owJmQoVhz82aWAoi9M5s8PC6sSAqFI7KfAD2NRKun5riDIOM0dvu2caM25a5f5JbYLMc5Umxw8Dl7dBIDNwM00yVbSX8uS';
    const { user } = useGlobalContext()
    const [clientSecret, setClientSecret] = useState('');
    const stripePromise = loadStripe(stripePublicKey);

    useEffect(() => {
        // Fetch clientSecret from your backend
        fetch('/api/stripe/create-payment-intent', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${user.token}`,
            },
            body: JSON.stringify({ userId: user.userId }), // replace with actual
            // userId
        })
            .then((res) => res.json())
            .then((data) => setClientSecret(data.clientSecret));
    }, []);

    return (
        <div>
            {clientSecret && (
                <Elements stripe={stripePromise} options={{ clientSecret }}>
                    <CheckoutForm />
                </Elements>
            )}
        </div>
    );
};

export default PaymentPage;
