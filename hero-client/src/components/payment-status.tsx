import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

export const PaymentStatus = () => {
    const user = {
        userId: window.localStorage.getItem("userId"),
        token: window.localStorage.getItem("token"),
    };
    const [membership, setMembership] = useState({ status: "" });
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    const paymentIntent = queryParams.get("payment_intent");
    const clientSecret = queryParams.get("payment_intent_client_secret");
    const redirectStatus = queryParams.get("redirect_status");

    useEffect(() => {
        console.log(clientSecret);
        console.log(paymentIntent);
        console.log(user);
        console.log(redirectStatus);

        if (redirectStatus === "succeeded" && user.userId) {
            fetch("/api/stripe/payment-status", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${user.token}`,
                },
                body: JSON.stringify({
                    userId: user.userId,
                    paymentIntent: paymentIntent,
                    paymentStatus: true,
                }),
            })
                .then((response) => response.json())
                .then((data) => {
                    console.log("Membership updated:", data);
                    if(data.error !== null){
                        // setMembership(data)
                        console.log("Error: ", data.error);
                        
                    } else {
                        setMembership(data);
                    }
                })
                .catch((error) => {
                    console.error("Error updating membership:", error);
                });
        }
    }, [paymentIntent, redirectStatus, user.userId, user.token]);

    return membership ? (
        <div>
            Payment {membership.status === "ACTIVE" ? <>ok</> : <>failed</>}
        </div>
    ) : (
        <div>Loading...</div>
    );
};
