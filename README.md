# Agendify Android

App nativa Android (Java) para gestión de turnos. Consume por HTTPS la API REST del backend desplegado en la primera entrega (WebApp / Railway) y usa **Firebase Authentication** como servicio cloud adicional.
Desarrollada como proyecto integrador para el espacio curricular APLICACIONES MÓVILES Y CLOUD COMPUTING

## Integrantes

- Bahurlet, Micaela
- Cáceres, Tomás
- Dome, Florencia
- Spinetta, Carlos

## Links de la entrega

| Recurso | URL |
|---|---|
| Repositorio Android | https://github.com/ironcharly/Agendify_Android |
| Web app (1.ª entrega) | https://webapp-production-44888520.up.railway.app/ |
| API REST consumida | https://webapp-production-44888520.up.railway.app/backend/api_turnos.php |
| Proveedor de deploy | Railway |
| Video demo (3–5 min) | _PEGUEMOS ACA LUEGO EL LINK DEL VIDEO _ |

## Usuario de prueba (Firebase Auth)

Para ingresar a la app (login requerido):

- **Email:** `testdesdefirebase@test.com`
- **Contraseña:** `Tester1234`

Servicio cloud: **Firebase Authentication** (email/password).

## Qué hace la app

Agendify Android es un cliente móvil sobre la misma base de turnos de la web:

1. **Login / crear cuenta** con Firebase (acceso a la app).
2. **Listar turnos** desde la API (`GET`).
3. **Ver detalle** de un turno por ID (`GET ?id=`).
4. **Crear turno** (`POST`) con selector de fecha/hora.
5. Navegación con Intents, extras (`EXTRA_TURNO_ID`) y resultado al volver de crear.
6. Manejo de errores de red (modo avión / API caída) con Toast, sin cerrar la app.

> Nota: el login Firebase controla el acceso a la app; no reemplaza el login de negocios de la web.

## Pantallas y endpoints / cloud

| Pantalla | Qué hace | API / Cloud |
|---|---|---|
| `LoginActivity` | Inicio de sesión | Firebase Auth `signInWithEmailAndPassword` |
| `CrearCuentaActivity` | Registro de usuario | Firebase Auth `createUserWithEmailAndPassword` |
| `MainActivity` | Lista de turnos + logout + FAB crear | `GET /backend/api_turnos.php` + Firebase `signOut` |
| `DetalleTurnoActivity` | Detalle por ID (Intent extra) | `GET /backend/api_turnos.php?id={id}` |
| `CrearTurnoActivity` | Alta de turno (devuelve `RESULT_OK`) | `POST /backend/api_turnos.php` |

Base URL única (R7): definida en `ApiClient.BASE_URL` → `https://webapp-production-44888520.up.railway.app/`

## Tecnologías

- Java, Android Studio, minSdk 24
- XML layouts + `strings.xml`
- Retrofit + Gson + OkHttp
- RecyclerView / Material Components
- Firebase Authentication (+ `google-services.json` en módulo `app/`)
- Backend PHP + MySQL (Railway) de la 1.ª entrega

## Cómo ejecutar el proyecto

1. Clonar el repo y abrirlo en Android Studio.
2. Verificar que exista `app/google-services.json` (Firebase).
3. Sync Gradle.
4. Correr en emulador/dispositivo (API 24+).
5. Iniciar sesión con el usuario de prueba indicado arriba.


## Decisiones de diseño

- No duplicamos funcionalidad ni diseño de la web original.
- Se reutiliza la API REST de la 1.ª entrega en lugar de duplicar backend.
- Fecha/hora en crear turno con pickers Material para evitar formatos inválidos.
- Firebase Auth como  servicio cloud extra.
