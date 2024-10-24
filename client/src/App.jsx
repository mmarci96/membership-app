import { Route, Routes, BrowserRouter } from 'react-router-dom';
import { Suspense, lazy } from 'react';  // Import Suspense and lazy
import Navbar from "./components/navbar/Navbar.jsx";

// Lazy load the components
const Home = lazy(() => import('./scenes/Home.jsx'));
const Blog = lazy(() => import('./scenes/Blog.jsx'));
const Account = lazy(() => import('./scenes/Account.jsx'));
const MembershipPage = lazy(() => import('./scenes/MembershipPage.jsx'));
const PaymentPage = lazy(() => import('./scenes/PaymentPage.jsx'));
const PaymentComplete = lazy(() => import('./scenes/PaymentComplete.jsx'));

const App = () => {

    return (
        <BrowserRouter>
            <Navbar />
            {/* Wrap routes with Suspense and provide a fallback */}
            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route path="/"                 element={<Home            />} />
                    <Route path="/home"             element={<Home            />} />
                    <Route path="/blog"             element={<Blog            />} />
                    <Route path="/account"          element={<Account         />} />
                    <Route path="/membership"       element={<MembershipPage  />} />
                    <Route path="/payment"          element={<PaymentPage     />} />
                    <Route path="/payment-complete" element={<PaymentComplete />} />
                </Routes>
            </Suspense>
        </BrowserRouter>
    )
}

export default App;
