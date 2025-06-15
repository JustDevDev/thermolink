# —————— 1. Build phase ——————
FROM node:18-alpine AS builder
WORKDIR /app

# 1a) Install npm dependencies
COPY package*.json ./
RUN npm ci

# 1b) Copy the rest of the repo (including env.template.js at project root)
COPY . .

# 1c) Build the app (Vite will output to dist/)
RUN npm run build

# 1d) Copy your env.template.js into dist/ as env.js placeholder
RUN cp public/.env.template.js dist/env.js

# —————— 2. Production phase ——————
FROM nginx:stable-alpine

# 2a) Install envsubst utility
RUN apk add --no-cache gettext

# 2b) Copy built static files (including dist/env.js) into Nginx html dir
COPY --from=builder /app/dist/ /usr/share/nginx/html/

# 2c) Add entrypoint script for runtime env var injection
COPY docker-entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# 2d) Expose HTTP port
EXPOSE 80

# 2e) Run entrypoint (which will generate real env.js), then start Nginx
ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
