在使用 MySQL 数据库和 JPA 的情况下，DAO 层（即数据访问层）通常由 **Repository** 接口组成，使用 Spring Data JPA 来简化与数据库交互的操作。Spring Data JPA 提供了一个简洁的方式来定义数据访问层，它会自动实现基本的 CRUD 操作，同时也支持复杂查询。

### 1. **DAO 层设计基本原则**
- **Repository 接口**：每个实体类对应一个 Repository 接口，继承 `JpaRepository` 或 `CrudRepository`。
- **自定义查询**：如果需要进行复杂的查询，Spring Data JPA 支持使用 JPQL 或原生 SQL 查询，甚至可以通过注解定义查询方法。
- **事务管理**：所有的数据库操作默认是事务性的，Spring 会自动为 Repository 方法添加事务控制。

### 2. **DAO 层设计步骤**

#### 2.1 创建 Repository 接口

每个实体类通常有一个对应的 Repository 接口。这个接口继承自 `JpaRepository` 或 `CrudRepository`，即可自动获得 CRUD 操作，进一步可以扩展方法实现复杂的查询操作。

### 示例：

#### 用户 (User) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 通过用户名查找用户
    User findByUsername(String username);

    // 通过用户名和角色查找用户
    List<User> findByUsernameAndRole(String username, String role);

    // 可以根据需求，添加更多自定义查询方法
}
```

#### 账户 (Account) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 根据账户号码查找账户
    Account findByAccountNumber(String accountNumber);

    // 查找某个用户的所有账户
    List<Account> findByUser(User user);

    // 根据账户类型查找账户
    List<Account> findByAccountType(AccountType accountType);
}
```

#### 交易 (Transaction) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // 根据账户查找所有交易
    List<Transaction> findByAccount(Account account);

    // 根据交易类型查找交易记录
    List<Transaction> findByTransactionType(TransactionType transactionType);

    // 根据账户和时间查找交易
    List<Transaction> findByAccountAndTransactionDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
}
```

#### 贷款 (Loan) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    // 查找某个用户的所有贷款
    List<Loan> findByUser(User user);

    // 查找贷款状态为某一状态的所有贷款
    List<Loan> findByLoanStatus(LoanStatus loanStatus);
}
```

#### 还款记录 (LoanRepayment) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    // 查找某个贷款的所有还款记录
    List<LoanRepayment> findByLoan(Loan loan);

    // 查找某个用户的所有还款记录
    List<LoanRepayment> findByLoanUser(User user);
}
```

#### 审计日志 (AuditLog) Repository

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // 根据操作名称查找审计日志
    List<AuditLog> findByAction(String action);

    // 根据时间范围查找审计日志
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
```

### 2.2 使用 `@Query` 注解进行自定义查询

对于一些复杂的查询，可以使用 `@Query` 注解来编写 JPQL 或原生 SQL 查询。下面是一些例子：

#### 示例：通过账户号查找交易记录并排序

```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // JPQL 查询：通过账户号和日期范围查找交易记录
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByAccountNumberAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
```

#### 示例：查询某个用户的贷款及其还款记录

```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // 查询某个用户的所有贷款，并联结查询还款记录
    @Query("SELECT l FROM Loan l LEFT JOIN FETCH l.repayments WHERE l.user = :user")
    List<Loan> findLoansWithRepaymentsByUser(User user);
}
```

### 2.3 分页和排序

Spring Data JPA 提供了对分页和排序的支持。在查询方法中使用 `Pageable` 和 `Sort` 参数来实现分页和排序功能。

#### 示例：分页查询交易记录

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 分页查询：根据账户查找交易记录
    Page<Transaction> findByAccount(Account account, Pageable pageable);
}
```

### 2.4 使用自定义实现（如果需要）

如果 Repository 接口中无法满足需求，也可以通过自定义实现来扩展复杂的查询操作。Spring Data JPA 提供了自定义实现的机制，可以通过创建自定义的 DAO 层来实现特定的业务逻辑。

#### 示例：自定义 Repository 实现

```java
public interface CustomAccountRepository {
    List<Account> findAccountsWithBalanceGreaterThan(BigDecimal amount);
}

public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> findAccountsWithBalanceGreaterThan(BigDecimal amount) {
        String query = "SELECT a FROM Account a WHERE a.balance > :amount";
        return entityManager.createQuery(query, Account.class)
                            .setParameter("amount", amount)
                            .getResultList();
    }
}
```

然后将自定义实现和 Spring Data JPA 的 `JpaRepository` 结合：

```java
public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
    // 继承自定义接口，可以使用自定义的查询方法
}
```

### 3. **总结**

DAO 层的设计主要由 Repository 接口组成，继承自 Spring Data JPA 提供的接口 `JpaRepository` 或 `CrudRepository`，系统会自动实现常见的 CRUD 操作。对于复杂的查询，可以通过自定义查询方法或 `@Query` 注解来实现。在大多数情况下，Spring Data JPA 会自动管理事务并简化操作，使开发者能够专注于业务逻辑实现。

- **`JpaRepository`** 提供了大量现成的 CRUD 功能，适用于大部分情况。
- **`@Query` 注解** 允许编写 JPQL 或原生 SQL 查询，适用于复杂查询。
- **分页和排序** 可以通过 `Pageable` 和 `Sort` 来灵活处理。
- **自定义实现** 可以通过自定义 Repository 实现来扩展功能。