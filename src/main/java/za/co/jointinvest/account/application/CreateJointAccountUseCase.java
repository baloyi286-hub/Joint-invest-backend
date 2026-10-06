package za.co.jointinvest.account.application;

import java.security.SecureRandom;
import java.util.UUID;
import za.co.jointinvest.account.application.port.JointAccountRepository;
import za.co.jointinvest.account.domain.JointAccount;

public final class CreateJointAccountUseCase {
    private final JointAccountRepository repository;
    private final SecureRandom secureRandom;

    public CreateJointAccountUseCase(JointAccountRepository repository, SecureRandom secureRandom) {
        this.repository = repository;
        this.secureRandom = secureRandom;
    }

    public CreatedJointAccount execute(CreateJointAccountCommand command) {
        String invitationPin = "%06d".formatted(secureRandom.nextInt(1_000_000));
        JointAccount account = JointAccount.awaitingInvestor(
                UUID.randomUUID(),
                command.creatorId(),
                command.approverId(),
                command.approvalPolicy(),
                command.contributionAmount(),
                invitationPin);
        repository.save(account);
        return new CreatedJointAccount(account.id(), invitationPin);
    }

    public record CreatedJointAccount(UUID accountId, String invitationPin) {}
}
