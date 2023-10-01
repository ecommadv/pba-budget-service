package com.PBA.budgetservice.security;

import com.PBA.budgetservice.persistance.model.GroupLoginInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@RequestScope
public class SecurityContextHolder {
    private UUID currentUserUid;
    private GroupLoginInfo groupLoginInfo;
    private String currentTokenType;
}
