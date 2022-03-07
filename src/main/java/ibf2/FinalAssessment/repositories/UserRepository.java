package ibf2.FinalAssessment.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2.FinalAssessment.models.User;

@Repository
public class UserRepository {
  private static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email = ?";

  private static final String SQL_CREATE_NEW_USER_ACCOUNT = "INSERT INTO users (email) VALUES (?)";

  private static final String SQL_UPDATE_CASH = "UPDATE users SET cash = ? WHERE email = ?";

  @Autowired
  private JdbcTemplate template;

  public boolean createUser(String email) {
    int added = template.update(SQL_CREATE_NEW_USER_ACCOUNT, email);
    return added > 0;
  }

  public boolean hasUser(String email) {
    return findUserByEmail(email).isPresent();
  }

  public Optional<User> findUserByEmail(String email) {
    final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
    if (rs.next())
      return Optional.of(User.populate(rs));
    return Optional.empty();
  }

  public Optional<BigDecimal> getCashBalance(String email) {
    if (findUserByEmail(email).isPresent()) {
      return Optional.of(findUserByEmail(email).get().getCash());
    }
    return Optional.empty();
  }

  public boolean updateBalance(BigDecimal trade, String email) {

    BigDecimal previousBalance = getCashBalance(email).get();
    BigDecimal updatedBalance = previousBalance.subtract(trade);

    int updated = template.update(SQL_UPDATE_CASH, updatedBalance, email);
    return updated > 0;
  }

}
