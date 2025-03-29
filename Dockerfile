FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /workspace/app

# Copie du pom.xml et des scripts mvnw pour télécharger les dépendances sans code source
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Rendre le script mvnw exécutable
RUN chmod +x ./mvnw

# Téléchargement des dépendances (cache optimisé pour Docker)
RUN ./mvnw dependency:go-offline -B

# Copie du code source
COPY src src

# Construction du JAR
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Image finale optimisée
FROM eclipse-temurin:17-jre-alpine

# Ajout d'un utilisateur non-root pour des raisons de sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Définition des variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod

# Copie du JAR décompressé
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Exposition du port
EXPOSE 8080

# Définition du point d'entrée
ENTRYPOINT ["java","-cp","app:app/lib/*","com.inventory.stockmanagementapi.StockManagementApiApplication"]
