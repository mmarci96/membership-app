import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import CheckoutForm from '../components/forms/CheckoutForm.jsx';
import { useState, useEffect } from 'react';
import {useGlobalContext} from "../hooks/useGlobalContext.js";

const PaymentPage = () => {
    const stripePublicKey = 'pk_test_51QD8akFDvrnvLO6Q70i4UqzN6zqQ3G9qCOQQGrzkEuFbiom9vsf6qVQ8cGC0f3m7hzqmVdhJkJ8hrqF7hjUqoHh600nkbdvpy6';
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
