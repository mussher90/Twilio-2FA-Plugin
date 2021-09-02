package matuss;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
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
    private static final String SERVICE_NAME = "Service Name";
    private static final Logger LOG = Logger.getLogger(SMSAuthenticator.class);
    private static final String _2faTemplate = "TwilioSMS.ftl";

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        String phoneNumber = context.getUser().getFirstAttribute("phoneNumber");
        LoginFormsProvider form = context.form();
        Map<String,String> config = context.getAuthenticatorConfig().getConfig();

        String accountSid = config.get(ACCOUNT_SID);
        String authToken = config.get(AUTH_TOKEN);
        String serviceName = config.get(SERVICE_NAME);


        Twilio.init(accountSid, authToken);

        Service service = Service.creator(serviceName ).create();

        String serviceSid = service.getSid();

        Verification verification = Verification.creator(serviceSid, phoneNumber, "sms")
                .create();

        context.getAuthenticationSession().setAuthNote("serviceSid", serviceSid);
        LOG.info(verification.getStatus() + "\n" + serviceSid);

        context.challenge(form.createForm(_2faTemplate));

    }

    @Override
    public void action(AuthenticationFlowContext context) {

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        String code = formData.getFirst("code");
        String phoneNumber = context.getUser().getFirstAttribute("phoneNumber");
        String serviceSid = context.getAuthenticationSession().getAuthNote("serviceSid");

        VerificationCheck checkCode = VerificationCheck.creator(serviceSid, code)
                .setTo(phoneNumber)
                .create();

        LOG.info(code + "\n");

        LOG.info("It seems to have worked alright...\n");

        if(checkCode.getValid()){
            context.success();
        }
        else{
            LOG.info(String.format("Code is not valid: %s", checkCode.getValid()));
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, context.form()
                    .setError("smsAuthCodeInvalid", null)
                    .createForm(_2faTemplate));
            return;
        }
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
