package za.co.jointinvest.account.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class JointAccount {
    private final UUID id;
    private final UUID firstInvestorId;
    private UUID secondInvestorId;
    private final UUID approverId;
    private final WithdrawalApprovalPolicy approvalPolicy;
    private final BigDecimal agreedContribution;
    private final String invitationPin;
    private JointAccountStatus status;

    private JointAccount(
            UUID id,
            UUID firstInvestorId,
            UUID secondInvestorId,
            UUID approverId,
            WithdrawalApprovalPolicy approvalPolicy,
            BigDecimal agreedContribution,
            String invitationPin,
            JointAccountStatus status) {
        this.id = Objects.requireNonNull(id);
        this.firstInvestorId = Objects.requireNonNull(firstInvestorId);
        this.secondInvestorId = secondInvestorId;
        this.approverId = approverId;
        this.approvalPolicy = Objects.requireNonNull(approvalPolicy);
        this.agreedContribution = requirePositive(agreedContribution);
        this.invitationPin = Objects.requireNonNull(invitationPin);
        this.status = Objects.requireNonNull(status);
        validatePolicy();
    }

    public static JointAccount awaitingInvestor(
            UUID id,
            UUID creatorId,
            UUID approverId,
            WithdrawalApprovalPolicy approvalPolicy,
            BigDecimal contribution,
            String invitationPin) {
        return new JointAccount(
                id,
                creatorId,
                null,
                approverId,
                approvalPolicy,
                contribution,
                invitationPin,
                JointAccountStatus.AWAITING_SECOND_INVESTOR);
    }

    public void join(UUID investorId) {
        Objects.requireNonNull(investorId);
        if (status != JointAccountStatus.AWAITING_SECOND_INVESTOR) {
            throw new IllegalStateException("Account is not accepting another investor");
        }
        if (firstInvestorId.equals(investorId)) {
            throw new IllegalArgumentException("Creator cannot join as the second investor");
        }
        secondInvestorId = investorId;
        status = JointAccountStatus.AWAITING_FUNDING;
    }

    public boolean canApproveWithdrawal(UUID userId) {
        if (approvalPolicy == WithdrawalApprovalPolicy.THIRD_PARTY_APPROVER) {
            return Objects.equals(approverId, userId);
        }
        return firstInvestorId.equals(userId) || Objects.equals(secondInvestorId, userId);
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
    public UUID firstInvestorId() { return firstInvestorId; }
    public UUID secondInvestorId() { return secondInvestorId; }
    public BigDecimal agreedContribution() { return agreedContribution; }
    public WithdrawalApprovalPolicy approvalPolicy() { return approvalPolicy; }
    public JointAccountStatus status() { return status; }
    public String invitationPin() { return invitationPin; }
}
