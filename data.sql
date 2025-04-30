INSERT INTO membership_packages (id, name, description) VALUES
(1, 'Basic Plan', 'Access to basic features and limited content.'),
(2, 'Pro Plan', 'Includes all features of Basic plus advanced modules.'),
(3, 'Elite Plan', 'Full access to everything, including premium support.');

INSERT INTO membership_modules (id, title, content, membership_package_id) VALUES
(1, 'Getting Started', 'Introduction to the platform.', 1),
(2, 'Advanced Analytics', 'Deep dive into analytics features.', 2),
(3, 'Expert Webinars', 'Monthly webinars with industry leaders.', 3),
(4, 'Community Access', 'Join and interact with the community.', 2),
(5, 'Premium Support', '24/7 support from our expert team.', 3);

INSERT INTO blog_posts (id, title, content, created_at, updated_at) VALUES
(1, 'Welcome to the Platform!', 'We’re excited to have you here.', NOW(), NOW()),
(2, 'Top 5 Tips to Maximize Your Membership', 'Discover how to get the most out of your plan.', NOW(), NOW()),
(3, 'Feature Spotlight: Analytics', 'A closer look at the Analytics Module.', NOW(), NOW());
