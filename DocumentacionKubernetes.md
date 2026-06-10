

1. .\mvnw.cmd clean package -DskipTests '-Djacoco.skip=true'
2. docker compose up --build
3. http://localhost:15672/ probar que kubernetes sirva. exchange > trackinvest.domain.events