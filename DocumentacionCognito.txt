=============================================
DOCUMENTACIÓN AWS COGNITO
=============================================

1. CREAR UN CLIENTE DE APLICACIÓN EN GCP. HACER CLICK EN API & SERVICES > OAUTH CONSENT SCREEN > CREATE PROYECT:
            - Nombre del proyecto: TrackInvestApi
            - Correo electrónico del soporte: diegoaarm.dev@gmail.com
            - Tipo de usuario: Externo
            - Crear.

            - Agregar Usuarios de prueba.
            - Agregar scopes: email, profile, openid.

        IR A API & SERVICES > CREDENTIALS > CREATE CREDENTIALS > OAUTH CLIENT ID:
            - Tipo de aplicación: Web application
            - Nombre: cognito-client
            - Authorized JavaScript origins: https://cognito-idp.us-east-1.amazonaws.com
            - Authorized redirect URIs: https://cognito-idp.us-east-1.amazonaws.com/oauth2/idpresponse
            - Crear.

2. CONFIGURAR SPRINGBOOT. AGREGAR DEPENDENCIAS DE SPRING SECURITY Y OAUTH2 RESOURCE SERVER EN EL POM.XML.:
            - Configurar el SecurityConfig. Agregar la función CorsCofuiguration cors.
            - En SecurityConfig agregar la función SecurityFilterChain security para configurar las rutas públicas y privadas.
            - En el application.properties agregar las siguientes propiedades que luego completaremos:
                spring.security.oauth2.resourceserver.jwt.jwk-set-uri=
                spring.security.oauth2.resourceserver.jwt.issuer-uri=
                spring.security.oauth2.resourceserver.jwt.clientId=
                spring.security.oauth2.resourceserver.jwt.clientSecret=
                auth.cognitoUri=

3. CONFIGURAR AUTHCONTROLLER DE AWS COGNITO:
            - Crear el adaptador CognitoAuthAdapter y el port out IdentityProviderPort. Estos se van a encargar de comunicarse con AWS Cognito
                para generar la url de acceso y validar el token.
            - Crear el port in AuthWithCodePort y GenerateAuthUrlPort y sus respectivos usecase. Estos se encargaran de usar las funciones
                de AWS Cognito y se las entregará al AuthController.
            - Crear un AuthController con la función url que se encargará de generar la url que usará el front para acceder
                a través de cognito. Para esto usaremos las variables cognitoUri, clientId y clientSecret que configuramos
                en el application.properties.
            - Crear la función callback que se encargará de validar el token.

4. CONFIGURAR AWS COGNITO. PARA ESTO CREAREMOS UNA USERPOOL EN AWS COGNITO:
            - Aplicación web tradicional
            - Crear un User Pool con el nombre TrackInvestApi.
            - Opciones de login: Email, name
            - Url de redirección: http://localhost:4200/oauth2/idpresponse
            - Después de creado ir a administrar proveedores:
                - Agregar un nuevo proveedor de identidad: Google
                - Client ID: el que generamos en el paso 1.
                - Client Secret: el que generamos en el paso 1.
                - ambitos autorizados(scopes): "email openid profile"
            - Ir a clientes de aplicación, páginas de inicio de sesión y editar Ámbitos de OpenId connect: eleminar phone y agregar profile
            - Agregar además en sign-out url https://localhost:8081/login

5. RELLENAMOS LA INFORMACIÓN EN EL APPLICATION.PROPERTIES:
            - spring.security.oauth2.resourceserver.jwt.jwk-set-uri={URL de clave de firma de token}
            - spring.security.oauth2.resourceserver.jwt.issuer-uri={URL del emisor del token menos lo ultimo desde well-known}
            - spring.security.oauth2.resourceserver.jwt.clientId={ir a clientes de aplicación y client id}
            - spring.security.oauth2.resourceserver.jwt.clientSecret={ir a clientes de aplicación y client secret}
            - auth.cognitoUri={ir a dominio y cognito domain}

6. TERMINAR DE CONFIGURAR GCP. IR A API & SERVICES > CREDENTIALS:
            - Agregar a Origenes autorizados js: {cognito_domain}
            - Agregar a URIs de redireccionamiento autorizados: {cognito_domain}/oauth2/idpresponse

7. CONFIGURAR FRONTEND (REACT):
            - Configurar archivo .env con la url del backend y la url del front donde se redigirá después del login.
                - VITE_API_URL=http://localhost:8080
                - VITE_REDIRECT_URI=http://localhost:8081/auth/idpresponse
            - Configurar App.tsx para que tenga una ruta de login y una ruta de callback.
                - <Route path="/login" element={<Login />} />
                - <Route path="/oauth2/idpresponse" element={<AuthCallback />} />
                - A los demás componentes agregar agregar el <protectedRoute> para protegerlos.
                    - <Route
                        path="/transactions"
                        element={
                          <ProtectedRoute>
                            <DashboardLayout>
                              <Transactions />
                            </DashboardLayout>
                          </ProtectedRoute>
                        }
                      />

                - Configurar contexts/AuthContext.tsx ya que este va a tener funciones que manejen la sesión del
                    usuario, como guardar el token, validar el token, obtener el nombre del usuario,
                    login, logout, etc.
                - Configurar components/ProtectedRoute.tsx para que validen en cada página el token usando
                    las funciones de AuthContext.tsx.
                - Al components/DashboardLayout.tsx agregar user y email para mostrar el nombre del usuario logueado.
                    Además un botón para el logout. Estas 3 funciones usan a AuthContext.tsx
                - Configurar lib/api.ts ya que este nos ayudará a hacer las llamadas al backend.
                - Configurar services/authService.ts para que tenga las llamadas al backend. la función de getUrl
                    del login de cognito o la función callback para validar el code que nos pasó cognito.
                - Configurar pages/Login.tsx. Aqui podemos configurar credenciales demo, además de usar las funciones
                    de AuthService.ts y AuthContext.tsx para hacer el login.
                - Configurar pages/AuthCallback.tsx para que valide el code que nos pasó cognito.
                - Ya el resto de páginas no hay que configurarlas, solo agregar el <ProtectedRoute> para protegerlas.
