package za.co.jointinvest.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class JointAccount {
    private final UUID id;
    private final UUID firstInvestorId;
    private final UUID secondInvestorId;
    private final UUID approverId;
    private final WithdrawalApprovalPolicy approvalPolicy;
    private final BigDecimal agreedContribution;

    public JointAccount(
            UUID id,
            UUID firstInvestorId,
            UUID secondInvestorId,
            UUID approverId,
            WithdrawalApprovalPolicy approvalPolicy,
            BigDecimal agreedContribution) {
        this.id = Objects.requireNonNull(id);
        this.firstInvestorId = Objects.requireNonNull(firstInvestorId);
        this.secondInvestorId = Objects.requireNonNull(secondInvestorId);
        this.approvalPolicy = Objects.requireNonNull(approvalPolicy);
        this.agreedContribution = requirePositive(agreedContribution);
        this.approverId = approverId;
        validatePolicy();
    }

    private BigDecimal requirePositive(BigDecimal amount) {
        Objects.requireNonNull(amount);
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Contribution must be greater than zero");
        }
        return amount;
    }

    private void validatePolicy() {
        if (approvalPolicy == WithdrawalApprovalPolicy.THIRD_PARTY_APPROVER && approverId == null) {
            throw new IllegalArgumentException("Third-party approval requires an approver");
        }
    }

    public UUID id() { return id; }
    public BigDecimal agreedContribution() { return agreedContribution; }
    public WithdrawalApprovalPolicy approvalPolicy() { return approvalPolicy; }
}
