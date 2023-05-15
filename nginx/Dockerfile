FROM nginx

EXPOSE 80
RUN mkdir -p /etc/nginx/certificate
COPY nginx.conf /etc/nginx/nginx.conf
COPY services.conf /etc/nginx/conf.d/default.conf
COPY nginx-certificate.crt /etc/nginx/certificate
COPY nginx.key /etc/nginx/certificate