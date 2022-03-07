package ibf2.FinalAssessment.services;

import static ibf2.FinalAssessment.config.Constants.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2.FinalAssessment.models.Order;
import ibf2.FinalAssessment.models.Shares;
import ibf2.FinalAssessment.models.User;
import ibf2.FinalAssessment.repositories.OrderRepository;
import ibf2.FinalAssessment.repositories.SharesRepository;
import ibf2.FinalAssessment.repositories.UserRepository;

@Service(USER_SERVICE)
public class UserService {
  @Autowired
  private OrderRepository orderRepo;

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private SharesRepository sharesRepo;

  public Optional<Integer> createUser(User user) {
    if (!userRepo.hasUser(user.getEmail())) {
      if (userRepo.createUser(user.getEmail()))
        return Optional.empty();
    }
    // user exists
    return Optional.of(401);
  }

  public List<Order> getOrderHistory(String email) {
    return orderRepo.getOrders(email);
  }

  public List<Shares> getShares(String email) {
    return sharesRepo.getShares(email);

  }

  public Optional<User> getUser(String email) {
    return userRepo.findUserByEmail(email);
  }

}
