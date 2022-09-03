package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;

public interface CommonUserService {
    CommonUser save(CommonUserDTO entity);
}
