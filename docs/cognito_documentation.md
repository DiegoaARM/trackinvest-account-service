=============================================
AWS COGNITO DOCUMENTATION
=============================================

1. CREATE AN APPLICATION CLIENT IN GCP. CLICK ON API & SERVICES > OAUTH CONSENT SCREEN > CREATE PROJECT:
            - Project name: TrackInvestApi
            - Support email: diegoaarm.dev@gmail.com
            - User type: External
            - Create.

            - Add test Users.
            - Add scopes: email, profile, openid.

        GO TO API & SERVICES > CREDENTIALS > CREATE CREDENTIALS > OAUTH CLIENT ID:
            - Application type: Web application
            - Name: cognito-client
            - Authorized JavaScript origins: https://cognito-idp.us-east-1.amazonaws.com
            - Authorized redirect URIs: https://cognito-idp.us-east-1.amazonaws.com/oauth2/idpresponse
            - Create.

2. CONFIGURE SPRING BOOT. ADD SPRING SECURITY AND OAUTH2 RESOURCE SERVER DEPENDENCIES TO THE POM.XML:
            - Configure the SecurityConfig. Add the CorsConfiguration cors function.
            - In SecurityConfig add the SecurityFilterChain security function to configure public and private routes.
            - In application.properties add the following properties that we will fill in later:
                spring.security.oauth2.resourceserver.jwt.jwk-set-uri=
                spring.security.oauth2.resourceserver.jwt.issuer-uri=
                spring.security.oauth2.resourceserver.jwt.clientId=
                spring.security.oauth2.resourceserver.jwt.clientSecret=
                auth.cognitoUri=

3. CONFIGURE THE AWS COGNITO AUTHCONTROLLER:
            - Create the CognitoAuthAdapter and the out port IdentityProviderPort. These will be in charge of communicating with AWS Cognito
                to generate the access url and validate the token.
            - Create the in ports AuthWithCodePort and GenerateAuthUrlPort and their respective use cases. These will be in charge of using the
                AWS Cognito functions and handing them over to the AuthController.
            - Create an AuthController with the url function that will be in charge of generating the url that the front end will use to access
                through cognito. For this we will use the cognitoUri, clientId and clientSecret variables that we configured
                in application.properties.
            - Create the callback function that will be in charge of validating the token.

4. CONFIGURE AWS COGNITO. FOR THIS WE WILL CREATE A USER POOL IN AWS COGNITO:
            - Traditional web application
            - Create a User Pool with the name TrackInvestApi.
            - Login options: Email, name
            - Redirect url: http://localhost:4200/oauth2/idpresponse
            - After creating it, go to manage providers:
                - Add a new identity provider: Google
                - Client ID: the one generated in step 1.
                - Client Secret: the one generated in step 1.
                - Authorized scopes: "email openid profile"
            - Go to app clients, login pages and edit OpenId Connect Scopes: remove phone and add profile
            - Also add in sign-out url https://localhost:8081/login

5. FILL IN THE INFORMATION IN THE APPLICATION.PROPERTIES:
            - spring.security.oauth2.resourceserver.jwt.jwk-set-uri={token signing key URL}
            - spring.security.oauth2.resourceserver.jwt.issuer-uri={token issuer URL minus everything after well-known}
            - spring.security.oauth2.resourceserver.jwt.clientId={go to app clients and client id}
            - spring.security.oauth2.resourceserver.jwt.clientSecret={go to app clients and client secret}
            - auth.cognitoUri={go to domain and cognito domain}

6. FINISH CONFIGURING GCP. GO TO API & SERVICES > CREDENTIALS:
            - Add to Authorized JavaScript origins: {cognito_domain}
            - Add to Authorized redirect URIs: {cognito_domain}/oauth2/idpresponse

7. CONFIGURE FRONTEND (REACT):
            - Configure the .env file with the backend url and the front url where it will redirect after login.
                - VITE_API_URL=http://localhost:8080
                - VITE_REDIRECT_URI=http://localhost:8081/auth/idpresponse
            - Configure App.tsx so it has a login route and a callback route.
                - <Route path="/login" element={<Login />} />
                - <Route path="/oauth2/idpresponse" element={<AuthCallback />} />
                - Add the <protectedRoute> to the other components to protect them.
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

                - Configure contexts/AuthContext.tsx since this will have functions that manage the user's
                    session, such as saving the token, validating the token, getting the user's name,
                    login, logout, etc.
                - Configure components/ProtectedRoute.tsx so it validates the token on every page using
                    the functions from AuthContext.tsx.
                - To components/DashboardLayout.tsx add user and email to display the logged-in user's name.
                    Also a button for logout. These 3 functions use AuthContext.tsx
                - Configure lib/api.ts since this will help us make calls to the backend.
                - Configure services/authService.ts so it has the calls to the backend. the getUrl function
                    for the cognito login or the callback function to validate the code that cognito passed us.
                - Configure pages/Login.tsx. Here we can configure demo credentials, in addition to using the functions
                    from AuthService.ts and AuthContext.tsx to perform the login.
                - Configure pages/AuthCallback.tsx so it validates the code that cognito passed us.
                - The rest of the pages don't need to be configured, just add the <ProtectedRoute> to protect them.
