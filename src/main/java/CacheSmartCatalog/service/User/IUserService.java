package CacheSmartCatalog.service.User;


import CacheSmartCatalog.model.User;

import java.util.Optional;

public interface IUserService {

    User getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    User saveUser(User user);



}
