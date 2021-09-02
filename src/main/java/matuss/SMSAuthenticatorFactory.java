package matuss;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

public class SMSAuthenticatorFactory implements AuthenticatorFactory {

    private static final String DISPLAY_TYPE = "Twilio 2FA";
    private static final String REFERENCE_CATEGORY = "SMS 2FA";
    private static final String HELP_TEXT = "Enable Twilio 2FA for user authentication";
    private static final String PROVIDER_ID = "sms 2fa";
    private static final String AUTH_TOKEN = "Authentication Token";
    private static final String ACCOUNT_SID = "Account SID";
    private static final String SERVICE_NAME = "Service Name";

    @Override
    public String getDisplayType() {
        return DISPLAY_TYPE;
    }

    @Override
    public String getReferenceCategory() {
        return REFERENCE_CATEGORY;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[]{
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.CONDITIONAL,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return HELP_TEXT;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Arrays.asList(
                new ProviderConfigProperty(AUTH_TOKEN, AUTH_TOKEN, "Twilio auth token", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty(ACCOUNT_SID, ACCOUNT_SID, "Twilio account sid", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty(SERVICE_NAME, SERVICE_NAME, "Twilio service name to appear in texts", ProviderConfigProperty.STRING_TYPE, null),
                new ProviderConfigProperty("Twilio Phone Number", "Twilio Phone Number", "Twilio sender phone number", ProviderConfigProperty.STRING_TYPE, null)
        );
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return new SMSAuthenticator();
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
