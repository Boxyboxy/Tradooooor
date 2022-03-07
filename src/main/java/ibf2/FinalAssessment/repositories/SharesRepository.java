package ibf2.FinalAssessment.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf2.FinalAssessment.models.Shares;

@Repository
public class SharesRepository {
  private static final String SQL_SELECT_SHARES_BY_SYMBOL = "SELECT symbol, SUM(shares) as quantity, SUM(shares * price) AS total_cost FROM orders WHERE email = ? AND symbol = ?";

  private static final String SQL_SELECT_SHARES_BY_EMAIL_SUM_ON_QUANTITY = "SELECT symbol, SUM(shares) AS quantity, SUM( shares * price) AS total_cost FROM orders WHERE email = ? GROUP BY symbol";
  @Autowired
  private JdbcTemplate template;

  public Optional<Shares> findShareBySymbol(String email, String symbol) {
    final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_SHARES_BY_SYMBOL, email, symbol);
    if (rs.next())
      return Optional.of(Shares.populate(rs));
    return Optional.empty();
  }

  public List<Shares> getShares(String email) {
    final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_SHARES_BY_EMAIL_SUM_ON_QUANTITY, email);
    List<Shares> result = new LinkedList<>();
    while (rs.next())
      result.add(Shares.populate(rs));
    return result;
  }

}
