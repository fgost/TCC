# Use a imagem base do Maven para compilar o código
FROM maven:3.8.7-eclipse-temurin-19-alpine AS build

# Instalar Node.js
RUN apk add --no-cache nodejs npm

# Definir o diretório de trabalho
WORKDIR /usr/src/fghost

# Copiar o arquivo pom.xml e baixar as dependências (para aproveitar o cache do Docker)
COPY pom.xml .

# Baixar as dependências Maven
RUN mvn dependency:go-offline

# Copiar o restante do código
COPY . .

# Compilar o projeto (não executa os testes)
RUN mvn clean package -Dmaven.test.skip=true

# Segunda fase - criar a imagem com apenas o JAR gerado
FROM openjdk:19-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado da fase de build
COPY --from=build /usr/src/fghost/target/carview1.0-1.0-SNAPSHOT.jar /app/carview-api-java.jar

# Expor a porta em que a aplicação vai rodar
EXPOSE 5003

# Comando para rodar a aplicação Java
ENTRYPOINT ["java", "-jar", "/app/carview-api-java.jar"]
