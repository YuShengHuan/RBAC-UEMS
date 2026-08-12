# 用完整的Debian 11（bullseye），不是slim，有完整包源
FROM debian:bullseye-slim



RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jre-headless \
    libreoffice \
    libreoffice-writer \
    libreoffice-calc \
    libreoffice-impress \
    libreoffice-base \
    libreoffice-core \
    libreoffice-common \
    fonts-dejavu \
    fonts-liberation \
    locales \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

#中文
RUN locale-gen zh_CN.UTF-8
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8
RUN localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
ENV LANG zh_CN.UTF-8
ENV LC_ALL zh_CN.UTF-8

# ========== 部署SpringBoot应用 ==========
WORKDIR /app
COPY app.jar /app/app.jar
EXPOSE 8089

# ========== 第五步：启动配置（root用户启动，路径指向软链接后的/usr/lib/libreoffice） ==========
ENTRYPOINT ["java", "-Djodconverter.local.office-home=/usr/lib/libreoffice", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]