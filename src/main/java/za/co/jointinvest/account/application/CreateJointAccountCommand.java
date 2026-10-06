package za.co.jointinvest.account.application;

import java.math.BigDecimal;
import java.util.UUID;
import za.co.jointinvest.account.domain.WithdrawalApprovalPolicy;

public record CreateJointAccountCommand(
        UUID creatorId,
        BigDecimal contributionAmount,
        WithdrawalApprovalPolicy approvalPolicy,
        UUID approverId) {
}
