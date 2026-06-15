# ============================================================
#  Dockerfile — Sistema Cleo (Java puro, sem Maven/Gradle)
# ============================================================

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copia todo o projeto para dentro do container
COPY . .

# Compila todos os .java usando os jars da pasta lib/ no classpath
RUN mkdir -p out && \
    find . -name "*.java" > fontes.txt && \
    javac -encoding UTF-8 -cp "lib/*" -d out @fontes.txt

# O Render injeta a variável PORT automaticamente
EXPOSE 8080

# Inicia o servidor
CMD ["sh", "-c", "java -cp out:lib/* main.Main"]
