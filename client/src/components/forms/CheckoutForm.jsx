import {useStripe, useElements, PaymentElement} from '@stripe/react-stripe-js';
import {useGlobalContext} from "../../hooks/useGlobalContext.js";

const CheckoutForm = () => {
    const { user } = useGlobalContext();
    const stripe = useStripe();
    const elements = useElements();

    const handleSubmit = async (event) => {
        // We don't want to let default form submission happen here,
        // which would refresh the page.
        event.preventDefault();

        if (!stripe || !elements) {
            // Stripe.js hasn't yet loaded.
            // Make sure to disable form submission until Stripe.js has loaded.
            return;
        }

        const result = await stripe.confirmPayment({
            //`Elements` instance that was used to create the Payment Element
            elements,
            confirmParams: {
                return_url: "http://localhost:5173/payment-complete",
            },
        });

        if (result.error) {
            // Show error to your customer (for example, payment details incomplete)
            console.log(result.error.message);
        } else {
            // Your customer will be redirected to your `return_url`. For some payment
            // methods like iDEAL, your customer will be redirected to an intermediate
            // site first to authorize the payment, then redirected to the `return_url`.
            console.log(result)
            fetch('/api/membership', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authenticate': `Bearer ${user.token}`
                },
                body: JSON.stringify({
                    userId: user.userId,
                    paymentStatus: true
                }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Membership updated:', data);
                })
                .catch(error => {
                    console.error('Error updating membership:', error);
                });
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <PaymentElement />
            <button disabled={!stripe}>Submit</button>
        </form>
    )
};

export default CheckoutForm;
