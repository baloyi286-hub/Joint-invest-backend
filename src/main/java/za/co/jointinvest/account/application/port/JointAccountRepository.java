package za.co.jointinvest.account.application.port;

import java.util.Optional;
import java.util.UUID;
import za.co.jointinvest.account.domain.JointAccount;

public interface JointAccountRepository {
    JointAccount save(JointAccount account);
    Optional<JointAccount> findById(UUID id);
}
