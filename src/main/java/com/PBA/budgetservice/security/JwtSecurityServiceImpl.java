package com.PBA.budgetservice.security;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.persistance.model.GroupLoginInfo;
import com.PBA.budgetservice.persistance.model.GroupRole;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class JwtSecurityServiceImpl implements JwtSecurityService {
    private final Map<Permission, Runnable> permissionValidator = Map.of(
            Permission.CREATE_EXPENSE, this::validateIsNotGuest,
            Permission.CREATE_INCOME, this::validateIsNotGuest,
            Permission.CREATE_ACCOUNT, this::validateIsNotGuest,
            Permission.UPDATE_EXPENSE, this::validateIsNotGuest,
            Permission.UPDATE_INCOME, this::validateIsNotGuest,
            Permission.DELETE_EXPENSE, this::validateIsNotGuest,
            Permission.DELETE_INCOME, this::validateIsNotGuest,
            Permission.GET_EXPENSES, () -> {}, // any logged in entity has permission
            Permission.GET_INCOMES, () -> {} // any logged in entity has permission
    );
    private final SecurityContextHolder contextHolder;

    public JwtSecurityServiceImpl(SecurityContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    @Override
    public UUID getCurrentUserUid() {
        return contextHolder.getCurrentUserUid();
    }

    @Override
    public void setCurrentUserUid(UUID uid) {
        contextHolder.setCurrentUserUid(uid);
    }

    @Override
    public GroupLoginInfo getCurrentGroupLoginInfo() {
        return contextHolder.getGroupLoginInfo();
    }

    @Override
    public String getCurrentTokenType() {
        return contextHolder.getCurrentTokenType();
    }

    @Override
    public UUID getCurrentAccountOwnerUid() {
        return this.getCurrentTokenType().equals(TokenType.USER)
                ? this.getCurrentUserUid()
                : this.getCurrentGroupLoginInfo().getGroupUid();
    }

    @Override
    public void validateHasPermission(Permission permission) {
        permissionValidator.get(permission).run();
    }

    private void validateIsNotGuest() {
        if (this.getCurrentTokenType().equals(TokenType.USER)) {
            return;
        }
        if (this.getCurrentGroupLoginInfo().getRole().equals(GroupRole.GUEST)) {
            throw new AuthorizationException();
        }
    }
}
