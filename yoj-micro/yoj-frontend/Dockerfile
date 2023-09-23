FROM nginx:latest
LABEL authors="joshua"

#设置时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

COPY nginx.conf /etc/nginx/nginx.conf
COPY dist/  /usr/share/nginx/html/yoj

EXPOSE 8080