# Práctica: Pipeline CI con Jenkins y Java (Maven)

## Objetivo

Que el alumnado configure un pipeline en Jenkins que, cada vez que se suba código
a un repositorio Git, compile automáticamente un proyecto Java, ejecute sus
pruebas unitarias y genere un `.jar` como artefacto final.

## Material necesario

- Un PC o VM por alumno (o por pareja) con Docker instalado.
- Cuenta de GitHub (gratuita) para cada alumno o grupo.
- El proyecto de ejemplo de esta carpeta (`Calculadora.java`, su test, el
  `pom.xml` y el `Jenkinsfile`).

## Paso 1: Subir el proyecto a un repositorio Git

1. Cada alumno crea un repositorio nuevo en GitHub (por ejemplo,
   `jenkins-demo-java`).
2. Copian dentro los archivos de esta carpeta manteniendo la estructura:

   ```
   jenkins-demo-java/
   ├── pom.xml
   ├── Jenkinsfile
   └── src/
       ├── main/java/com/example/Calculadora.java
       └── test/java/com/example/CalculadoraTest.java
   ```

3. Hacen `git init`, `git add .`, `git commit -m "Proyecto inicial"` y
   `git push` a su repositorio.

## Paso 2: Levantar Jenkins con Docker

En el equipo de cada alumno (o en un servidor común del aula):

```bash
docker run -d --name jenkins \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts-jdk17
```

1. Esperar a que arranque y abrir `http://localhost:8080`.
2. Obtener la contraseña inicial:
   ```bash
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
3. Instalar los plugins sugeridos por defecto y crear el usuario administrador.

## Paso 3: Configurar las herramientas globales (Maven y JDK)

Este paso es clave porque el `Jenkinsfile` referencia estos nombres exactos:

1. Ir a **Administrar Jenkins → Herramientas (Global Tool Configuration)**.
2. En **JDK**, añadir una instalación llamada `JDK17` (marcar "Instalar
   automáticamente" desde adoptium, o apuntar a un JDK ya instalado).
3. En **Maven**, añadir una instalación llamada `Maven3` (también se puede
   instalar automáticamente).
4. Guardar los cambios.

> Si se usan otros nombres, hay que ajustar el bloque `tools {}` del
> `Jenkinsfile` para que coincidan.

## Paso 4: Crear el pipeline en Jenkins

1. En el dashboard, **Nueva Tarea (New Item)**.
2. Nombre: `jenkins-demo-java`. Tipo: **Pipeline**. Aceptar.
3. En la pestaña **Pipeline**, en "Definition" elegir **Pipeline script from
   SCM**.
4. SCM: **Git**. En "Repository URL" pegar la URL del repositorio de GitHub
   del alumno.
5. En "Branch Specifier" dejar `*/main` (o `*/master` según el caso).
6. Script Path: `Jenkinsfile` (ya viene por defecto).
7. Guardar.

## Paso 5: Ejecutar el pipeline

1. Pulsar **Build Now**.
2. Observar en tiempo real cómo se ejecutan las fases: Checkout → Build →
   Test → Package → Archivar artefacto.
3. Revisar la consola de salida (**Console Output**) de cada build.
4. Comprobar en la página del build:
   - Los resultados de las pruebas (**Test Result**).
   - El `.jar` generado en **Artefactos guardados (Build Artifacts)**.

## Paso 6: Provocar un fallo a propósito (muy recomendable en clase)

Para que el alumnado vea el valor real de CI, se les puede pedir que:

1. Modifiquen `Calculadora.java` para introducir un error (por ejemplo, que
   `sumar` reste en vez de sumar).
2. Hagan `git commit` y `git push`.
3. Vuelvan a lanzar el build (o configuren un webhook para que se dispare
   solo, ver Paso 7) y observen cómo la fase **Test** falla y el pipeline se
   marca en rojo.
4. Corrijan el error, hagan push de nuevo y comprueben que el build vuelve a
   ponerse en verde.

## Paso 7 (opcional, más avanzado): Disparo automático con webhook

1. En GitHub, ir a **Settings → Webhooks** del repositorio.
2. Añadir un webhook apuntando a `http://<IP-del-servidor-Jenkins>:8080/github-webhook/`.
3. En el job de Jenkins, en **Configurar**, marcar **GitHub hook trigger for
   GITScm polling**.
4. A partir de ahora, cada `git push` disparará el build automáticamente sin
   pulsar "Build Now".

> Nota: para que GitHub pueda llegar a un Jenkins que corre en `localhost`,
> hace falta exponerlo con algo como `ngrok`, o tener Jenkins en un servidor
> con IP accesible. Es un buen punto para hablar de redes y exposición de
> servicios con el alumnado.

## Ideas para ampliar la práctica

- Añadir una fase de análisis de calidad de código con **Checkstyle** o
  **SpotBugs**.
- Publicar el `.jar` en un repositorio Nexus/Artifactory.
- Construir una imagen Docker del proyecto y subirla a un registro.
- Comparar este pipeline declarativo con uno **Freestyle**, para que el
  alumnado entienda las ventajas del código versionado frente a la
  configuración manual por interfaz.

## Criterios de evaluación sugeridos

| Criterio | Descripción |
|---|---|
| Repositorio | Proyecto subido correctamente con la estructura pedida |
| Configuración Jenkins | JDK y Maven configurados, pipeline creado y conectado al repo |
| Ejecución correcta | Build en verde con tests pasando y `.jar` generado |
| Comprensión del fallo | Capacidad de provocar y corregir un fallo, interpretando los logs |
| (Opcional) Webhook | Build disparado automáticamente tras un push |
