package matuss;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;

public class SMSAuthenticator implements Authenticator {
    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {

    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {

    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession keycloakSession, org.keycloak.models.RealmModel realmModel, org.keycloak.models.UserModel userModel) {
        return false;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession keycloakSession, org.keycloak.models.RealmModel realmModel, org.keycloak.models.UserModel userModel) {

    }

    @Override
    public void close() {

    }
}
