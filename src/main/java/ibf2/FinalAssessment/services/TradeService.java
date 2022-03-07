package ibf2.FinalAssessment.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ibf2.FinalAssessment.config.Constants.*;
import ibf2.FinalAssessment.models.Order;
import ibf2.FinalAssessment.models.Shares;
import ibf2.FinalAssessment.repositories.OrderRepository;
import ibf2.FinalAssessment.repositories.SharesRepository;
import ibf2.FinalAssessment.repositories.UserRepository;

@Service(TRADE_SERVICE)
public class TradeService {

  @Autowired
  private OrderRepository orderRepo;

  @Autowired
  private SharesRepository sharesRepo;

  @Autowired
  private UserRepository userRepo;

  public Optional<Integer> buy(Order order) {
    // email not in database
    if (!userRepo.hasUser(order.getEmail()))
      return Optional.of(401);

    // balance less than order value
    if (userRepo.getCashBalance(order.getEmail()).get().compareTo(
        (order.getPrice().multiply(BigDecimal.valueOf(order.getShares())))) < 0)
      return Optional.of(402);

    // if enough balance to conduct purchase
    if (userRepo.getCashBalance(order.getEmail()).get().compareTo(
        (order.getPrice().multiply(BigDecimal.valueOf(order.getShares())))) > 0) {
      userRepo.updateBalance(orderRepo.createOrder(order), order.getEmail());
      return Optional.empty();
    }

    return Optional.of(400);
  }

  public Optional<Integer> sell(Order order) {
    // email not in database
    if (!userRepo.hasUser(order.getEmail()))
      return Optional.of(401);

    // quantity less than order
    Shares currentShares = sharesRepo.findShareBySymbol(order.getEmail(), order.getSymbol()).get();
    if (currentShares.getQuantity() < Math.abs(order.getShares())) {
      return Optional.of(402);
    }

    // if enough shares to conduct sale
    if (currentShares.getQuantity() > Math.abs(order.getShares())) {
      userRepo.updateBalance(orderRepo.createOrder(order), order.getEmail());
      return Optional.empty();
    }

    return Optional.of(400);
  }

}
