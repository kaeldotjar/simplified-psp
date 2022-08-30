package io.github.zam0k.simplifiedpsp.domain;

import java.math.BigDecimal;

public interface IPayer {
    void removeValue(BigDecimal value);
    BigDecimal getBalance();
}
