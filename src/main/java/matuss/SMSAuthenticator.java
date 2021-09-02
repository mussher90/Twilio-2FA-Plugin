package matuss;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.jboss.logging.Logger;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

public class SMSAuthenticator implements Authenticator {

    private static final String AUTH_TOKEN = "Authentication Token";
    private static final String ACCOUNT_SID = "Account SID";
    private static final String SERVICE_SID = "Service SID";
    private static final Logger LOG = Logger.getLogger(SMSAuthenticator.class);
    private static final String _2faTemplate = "TwilioSMS.ftl";

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        String phoneNumber = context.getUser().getFirstAttribute("phoneNumber");
        LoginFormsProvider form = context.form();
        Map<String,String> config = context.getAuthenticatorConfig().getConfig();

        String accountSid = config.get(ACCOUNT_SID);
        String authToken = config.get(AUTH_TOKEN);
        String serviceSid = config.get(SERVICE_SID);

        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),
                        serviceSid,
                        "Twilio 2FA authenticator working as desired - just need to add correct authentication logic.")
                .create();

        LOG.info(message.getSid());

        context.challenge(form.createForm(_2faTemplate));

    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        LOG.info(formData.getFirst("code") + "\n");

        LOG.info("It seems to have worked alright...");
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        return true;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession keycloakSession, org.keycloak.models.RealmModel realmModel, org.keycloak.models.UserModel userModel) {

    }

    @Override
    public void close() {

    }
}
