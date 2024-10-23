import {useState} from "react";
import FormInput from "./FormInput.jsx";

const SignupForm = () => {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
        password_confirmation: "",
        accepted: false,
    })
    const [loading, setLoading] = useState(false);
    const [responseData, setResponseData] = useState(null);

    const handleChange = e => {
        const {name, type, value, checked} = e.target;

        setFormData(prev => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const requestBody = {
            email: formData.email,
            password: formData.password,
        }
        const res = await fetch("/api/auth/signup", {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers: {
                "Content-Type": "application/json",
            }
        })
        if (!res.ok) {
            console.error("Failed to sign up");
        }
        const data = await res.json();
        console.log(data);
        setResponseData(data);
        setLoading(false);
    }

    return (
        <div>
            Signup form
            <form onSubmit={handleSubmit}>
                <FormInput
                    label={"Email address"}
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    type="email"
                />
                <FormInput
                    label={"Password"}
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    type="password"
                />
                <FormInput
                    label={"Validate password"}
                    name="password_confirmation"
                    value={formData.password_confirmation}
                    onChange={handleChange}
                    type="password"
                />
                <FormInput
                    label={"Accept terms and conditions"}
                    name="accepted"
                    value={formData.accepted}
                    type="checkbox"
                    onChange={handleChange}
                />
                <button type={"submit"} onClick={handleSubmit}>
                    Sign up
                </button>
            </form>
        </div>
    )
}
export default SignupForm