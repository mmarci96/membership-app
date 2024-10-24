const BlogPost = ({title , content , createdAt }) => {
    const createdDate = new Date(createdAt);

    return (
        <div className="blog-post">
            <div className="blog-post__header">
                <h3 className="blog-post__title">{title}</h3>
            </div>
            <div className="blog-post__content">
                <p className="blog-post__body">
                    {content}
                </p>
                <p className="blog-post__footer">
                    {createdDate.toLocaleDateString()}
                </p>
            </div>
        </div>
    )
}

export default BlogPost