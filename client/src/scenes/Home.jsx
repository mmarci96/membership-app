import { Link } from 'react-router-dom';

const Home = () => {
    return (
        <div className="Home flex justify-evenly">
            Home page
        Become a member or log in

            <Link to={"/account"}>
                <button>
                    Become a member!
                </button>
            </Link>
            <Link to={"/blog"}>
                <button>
                    Go to blog!
                </button>
            </Link>
        </div>
    )
}
export default Home