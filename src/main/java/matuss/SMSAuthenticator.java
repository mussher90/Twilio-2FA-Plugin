package matuss;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.jboss.logging.Logger;
import org.keycloak.forms.login.LoginFormsProvider;

public class SMSAuthenticator implements Authenticator {
    public static final String ACCOUNT_SID = "AC2a65008ad7318c594677c3c6b072a0f7";
    public static final String AUTH_TOKEN = "57609665014083fef773a8df89b33d20";
    private static final Logger LOG = Logger.getLogger(SMSAuthenticator.class);
    private static final String _2faTemplate = "TwilioSMS.ftl";

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        String phoneNumber = context.getUser().getFirstAttribute("phoneNumber");
        LoginFormsProvider form = context.form();

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),
                        "MG7ae148c41bf46baebeb4ebff99acc4cc ",
                        "Phantom Menace was clearly the best of the prequel trilogy.")
                .create();

        LOG.info(message.getSid());

        context.challenge(form.createForm(_2faTemplate));

    }

    @Override
    public void action(AuthenticationFlowContext context) {
        LOG.info("It seems to have worked alright...");
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
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
