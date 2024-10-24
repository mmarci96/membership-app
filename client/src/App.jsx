import { Route, Routes, BrowserRouter } from 'react-router-dom';
import Home from "./scenes/Home.jsx";
import Blog from "./scenes/Blog.jsx";
import Account from './scenes/Account.jsx';
import MembershipPage from "./scenes/MembershipPage.jsx";


const App = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/"             element={<Home           />}    />
                <Route path="/home"         element={<Home           />}    />
                <Route path="/blog"         element={<Blog           />}    />
                <Route path="/account"      element={<Account        />}    />
                <Route path="/membership"   element={<MembershipPage />}    />
            </Routes>
        </BrowserRouter>
    )
}

export default App
