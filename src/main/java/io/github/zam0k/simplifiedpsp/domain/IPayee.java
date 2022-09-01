package io.github.zam0k.simplifiedpsp.domain;

import java.math.BigDecimal;

public interface IPayee {
    void receiveValue(BigDecimal value);
    String getEmail();
}
