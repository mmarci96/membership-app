# Stage 1: Build the Vite app
FROM node:18-alpine AS build

# Set working directory inside the container
WORKDIR /app

# Copy the package.json and lock files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the app's source code
COPY . .

# Build the app (this will output files in /app/dist)
RUN npm run build

# Stage 2: Serve with nginx
FROM nginx:alpine

# Copy the built files from the previous stage to the nginx web root
COPY --from=build /app/dist /usr/share/nginx/html

# Expose the port nginx will use
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
