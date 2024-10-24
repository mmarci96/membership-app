import { Link } from "react-router-dom";
import { useGlobalContext } from "../../hooks/useGlobalContext.js";

const Navbar = () => {
    const { user } = useGlobalContext();

    const logout = () => {
        localStorage.clear();
        window.location.reload();
    }
    
    return(
        <nav>
            <div className={'nav_buttons flex flex-wrap justify-evenly'}>
                <Link to={"/home"}>
                    <button>
                        Home
                    </button>
                </Link>
                <Link to={"/blog"}>
                    <button>
                        Blog
                    </button>
                </Link>
                <Link to={"/account"}>
                    <button>
                        Account
                    </button>
                </Link>
                {user.token && 
                    <button onClick={() => logout()}>
                        Logout
                    </button>
                }
                <Link to={"/membership"}>
                    <button>
                        Membership
                    </button>
                </Link>
            </div>
        </nav>
    )
}

export default Navbar