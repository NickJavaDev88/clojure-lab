# Imagen oficial de Clojure con la herramienta CLI (deps.edn) sobre JDK 21.
FROM clojure:temurin-21-tools-deps

WORKDIR /app

# Cacheamos las dependencias primero: si solo cambia el código fuente,
# Docker no vuelve a bajar las libs.
COPY deps.edn .
RUN clojure -P -A:dev

# Copiamos el resto del proyecto.
COPY . .

# Puerto del servidor nREPL (Calva se conecta aquí).
EXPOSE 7888
# Puerto de la UI de Portal (inspector de datos).
EXPOSE 5678

# Por defecto arrancamos el REPL de desarrollo.
CMD ["clojure", "-M:dev"]
