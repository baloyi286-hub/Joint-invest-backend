package za.co.jointinvest.withdrawal.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class WithdrawalRequest {
    private final UUID id;
    private final UUID accountId;
    private final UUID beneficiaryId;
    private final BigDecimal amount;
    private final Set<UUID> approvals = new HashSet<>();

    public WithdrawalRequest(UUID id, UUID accountId, UUID beneficiaryId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        this.id = id;
        this.accountId = accountId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
    }

    public void approve(UUID approverId) {
        approvals.add(approverId);
    }

    public boolean hasApprovalFrom(UUID userId) {
        return approvals.contains(userId);
    }

    public UUID id() { return id; }
    public UUID accountId() { return accountId; }
    public UUID beneficiaryId() { return beneficiaryId; }
    public BigDecimal amount() { return amount; }
}
