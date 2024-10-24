import { Route, Routes, BrowserRouter } from 'react-router-dom';
import Home from "./scenes/Home.jsx";
import Blog from "./scenes/Blog.jsx";
import Account from './scenes/Account.jsx';
import MembershipPage from "./scenes/MembershipPage.jsx";
import PaymentPage from "./scenes/PaymentPage.jsx";
import PaymentComplete from "./scenes/PaymentComplete.jsx";


const App = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/"                 element={<Home            />} />
                <Route path="/home"             element={<Home            />} />
                <Route path="/blog"             element={<Blog            />} />
                <Route path="/account"          element={<Account         />} />
                <Route path="/membership"       element={<MembershipPage  />} />
                <Route path="/payment"          element={<PaymentPage     />} />
                <Route path="/payment-complete" element={<PaymentComplete />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App
