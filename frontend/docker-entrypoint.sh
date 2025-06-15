#!/bin/sh
envsubst \
  < /usr/share/nginx/html/env.js \
  > /usr/share/nginx/html/env.generated.js

mv /usr/share/nginx/html/env.generated.js /usr/share/nginx/html/env.js

exec "$@"