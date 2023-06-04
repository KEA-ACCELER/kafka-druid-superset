FROM ubuntu:22.04

RUN apt update
RUN apt install openjdk-17-jdk -y

# sudo snap install gradle --classic
# wget http://www.java2s.com/Code/JarDownload/json-simple/json-simple-1.1.jar.zip
# RUN unzip json-simple-1.1.jar.zip
# build.gradle과 같은 폴더 안으로 옮기기

