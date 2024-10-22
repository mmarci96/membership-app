import { Route, Routes, BrowserRouter } from 'react-router-dom';
import Home from "./scenes/Home.jsx";
import Blog from "./scenes/Blog.jsx";


const App = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/home" element={<Home />} />
                <Route path="/blog" element={<Blog />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App
