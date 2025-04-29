import { Elements } from "@stripe/react-stripe-js";
import { Appearance, loadStripe } from "@stripe/stripe-js";
import CheckoutForm from "./checkout-form.js";
import { useState, useEffect } from "react";

export const StripePayment = () => {
    const stripePublicKey =
        "pk_test_51H59owJmQoVhz82aWAoi9M5s8PC6sSAqFI7KfAD2NRKun5riDIOM0dvu2caM25a5f5JbYLMc5Umxw8Dl7dBIDNwM00yVbSX8uS";
    const [clientSecret, setClientSecret] = useState("");
    const stripePromise = loadStripe(stripePublicKey);
    const appearance: Appearance = {
        theme: "night",
        variables: {
            colorBackground: "black",
            borderRadius: "16px",
        },
    };
    useEffect(() => {
        const token = window.localStorage.getItem("token");
        const userId = window.localStorage.getItem("userId");

        // Fetch clientSecret from your backend
        fetch("/api/stripe/create-payment-intent", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ userId }), // replace with actual
            // userId
        })
            .then((res) => res.json())
            .then((data) => setClientSecret(data.clientSecret));
    }, []);

    return (
        <div>
            {clientSecret && (
                <Elements
                    stripe={stripePromise}
                    options={{ clientSecret, appearance }}
                >
                    <CheckoutForm />
                </Elements>
            )}
        </div>
    );
};
