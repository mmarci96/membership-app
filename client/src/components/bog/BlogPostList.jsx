import { useEffect, useState } from "react";
import BlogPost from "./BlogPost.jsx";

const BlogPostList = () => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true); // Start loading as true
    const [error, setError] = useState(null);

    const getPosts = async () => {
        try {
            const response = await fetch("/api/blog/posts");
            if (!response.ok) { // Check if the response is not ok
                console.error("Could not find posts");
            }
            const json = await response.json();
            console.log(json);
            setPosts(json); // Set posts only on a successful response
        } catch (error) {
            setError(error.message); // Set the error message
        } finally {
            setLoading(false); // Always set loading to false after the fetch attempt
        }
    };

    useEffect(() => {
        getPosts();
    }, []);

    // Handle loading state
    if (loading) {
        return <div>Loading...</div>;
    }

    // Handle error state
    if (error) {
        return <div>Error: {error}</div>;
    }

    // Render the list of posts
    return (
        <div>
            {posts.length > 0 && posts.map(post => (
                <div key={post?.id}>
                    <BlogPost
                        title={post?.title}
                        content={post?.content}
                        createdAt={post?.createdAt}
                    />
                </div>
            ))}
        </div>
    );
};

export default BlogPostList;
