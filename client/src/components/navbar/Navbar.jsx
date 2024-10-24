import {Link} from "react-router-dom";

const Navbar = () => {
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