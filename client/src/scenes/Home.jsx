import SignupForm from "../components/forms/SignupForm.jsx";
import LoginForm from "../components/forms/LoginForm.jsx";

const Home = () => {
    return (
        <div className="Home flex justify-evenly">
            Home page
            <SignupForm />
            <LoginForm />
        </div>
    )
}
export default Home