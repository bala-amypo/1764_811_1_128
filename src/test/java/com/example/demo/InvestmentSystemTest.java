package com.example.demo;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.AlertSeverity;
import com.example.demo.entity.enums.AssetClassType;
import com.example.demo.entity.enums.RoleType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class InvestmentSystemTest {

    @Mock
    private InvestorProfileRepository investorProfileRepository;
    @Mock
    private AssetClassAllocationRuleRepository allocationRuleRepository;
    @Mock
    private HoldingRecordRepository holdingRecordRepository;
    @Mock
    private AllocationSnapshotRecordRepository snapshotRecordRepository;
    @Mock
    private RebalancingAlertRecordRepository alertRecordRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    private InvestorProfileServiceImpl investorProfileService;
    private AllocationRuleServiceImpl allocationRuleService;
    private HoldingRecordServiceImpl holdingRecordService;
    private AllocationSnapshotServiceImpl allocationSnapshotService;
    private RebalancingAlertServiceImpl rebalancingAlertService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        investorProfileService = new InvestorProfileServiceImpl(investorProfileRepository);
        allocationRuleService = new AllocationRuleServiceImpl(allocationRuleRepository);
        holdingRecordService = new HoldingRecordServiceImpl(holdingRecordRepository);
        allocationSnapshotService = new AllocationSnapshotServiceImpl(
                snapshotRecordRepository, holdingRecordRepository,
                allocationRuleRepository, alertRecordRepository
        );
        rebalancingAlertService = new RebalancingAlertServiceImpl(alertRecordRepository);
        jwtTokenProvider = new JwtTokenProvider(
        "thisIsA32ByteMinimumSecureJwtTestKey!", 
        3600000L
);

    }

    // ---------------------------------------------------------
    // Topic 1: Develop and deploy a simple servlet using Tomcat
    // ---------------------------------------------------------

    @Test(groups = "servlet", priority = 1)
    public void testServletLikeEndpointCreation() {
        InvestorProfile investor = new InvestorProfile("INV001", "John Doe", "john@example.com", true);
        when(investorProfileRepository.save(any())).thenReturn(investor);
        InvestorProfile saved = investorProfileService.createInvestor(investor);
        Assert.assertEquals(saved.getInvestorId(), "INV001");
        verify(investorProfileRepository, times(1)).save(any());
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletLikeEndpointGetById() {
        InvestorProfile investor = new InvestorProfile("INV002", "Jane Doe", "jane@example.com", true);
        investor.setId(2L);
        when(investorProfileRepository.findById(2L)).thenReturn(Optional.of(investor));
        InvestorProfile fetched = investorProfileService.getInvestorById(2L);
        Assert.assertEquals(fetched.getFullName(), "Jane Doe");
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletLikeEndpointGetByIdNotFound() {
        when(investorProfileRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            investorProfileService.getInvestorById(99L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("99"));
        }
    }

    @Test(groups = "servlet", priority = 4)
    public void testServletLikeEndpointListAll() {
        when(investorProfileRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(investorProfileService.getAllInvestors().isEmpty());
    }

    @Test(groups = "servlet", priority = 5)
    public void testServletLikeEndpointStatusUpdate() {
        InvestorProfile investor = new InvestorProfile("INV003", "Mark Smith", "mark@example.com", true);
        investor.setId(3L);
        when(investorProfileRepository.findById(3L)).thenReturn(Optional.of(investor));
        when(investorProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        InvestorProfile updated = investorProfileService.updateInvestorStatus(3L, false);
        Assert.assertFalse(updated.getActive());
    }

    @Test(groups = "servlet", priority = 6)
    public void testServletLikeEndpointLookupByInvestorId() {
        InvestorProfile investor = new InvestorProfile("INV004", "Lucy", "lucy@example.com", true);
        when(investorProfileRepository.findByInvestorId("INV004")).thenReturn(Optional.of(investor));
        Optional<InvestorProfile> opt = investorProfileService.findByInvestorId("INV004");
        Assert.assertTrue(opt.isPresent());
    }

    @Test(groups = "servlet", priority = 7)
    public void testServletLikeEndpointLookupByInvestorIdNotFound() {
        when(investorProfileRepository.findByInvestorId("INVALID")).thenReturn(Optional.empty());
        Optional<InvestorProfile> opt = investorProfileService.findByInvestorId("INVALID");
        Assert.assertTrue(opt.isEmpty());
    }

    @Test(groups = "servlet", priority = 8)
    public void testServletLikeEndpointActiveInvestorsFlag() {
        InvestorProfile investor = new InvestorProfile("INV005", "Active Investor", "active@example.com", true);
        when(investorProfileRepository.save(any())).thenReturn(investor);
        InvestorProfile saved = investorProfileService.createInvestor(investor);
        Assert.assertTrue(saved.getActive());
    }

    // ---------------------------------------------------------
    // Topic 2: CRUD operations using Spring Boot and REST APIs
    // ---------------------------------------------------------

    @Test(groups = "crud", priority = 9)
    public void testCreateAllocationRuleValid() {
        AssetClassAllocationRule rule = new AssetClassAllocationRule(
                1L, AssetClassType.STOCKS, 60.0, true
        );
        when(allocationRuleRepository.save(any())).thenReturn(rule);
        AssetClassAllocationRule saved = allocationRuleService.createRule(rule);
        Assert.assertEquals(saved.getTargetPercentage(), 60.0);
    }

    @Test(groups = "crud", priority = 10)
    public void testCreateAllocationRuleInvalidPercentageLow() {
        AssetClassAllocationRule rule = new AssetClassAllocationRule(
                1L, AssetClassType.BONDS, -1.0, true
        );
        try {
            allocationRuleService.createRule(rule);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("between 0 and 100"));
        }
    }

    @Test(groups = "crud", priority = 11)
    public void testCreateAllocationRuleInvalidPercentageHigh() {
        AssetClassAllocationRule rule = new AssetClassAllocationRule(
                1L, AssetClassType.CASH, 120.0, true
        );
        try {
            allocationRuleService.createRule(rule);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("between 0 and 100"));
        }
    }

    @Test(groups = "crud", priority = 12)
    public void testUpdateAllocationRuleSuccess() {
        AssetClassAllocationRule rule = new AssetClassAllocationRule(
                1L, AssetClassType.BONDS, 40.0, true
        );
        rule.setId(10L);
        when(allocationRuleRepository.findById(10L)).thenReturn(Optional.of(rule));
        when(allocationRuleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AssetClassAllocationRule updatedData = new AssetClassAllocationRule(
                1L, AssetClassType.BONDS, 45.0, true
        );
        AssetClassAllocationRule updated = allocationRuleService.updateRule(10L, updatedData);
        Assert.assertEquals(updated.getTargetPercentage(), 45.0);
    }

    @Test(groups = "crud", priority = 13)
    public void testUpdateAllocationRuleNotFound() {
        when(allocationRuleRepository.findById(200L)).thenReturn(Optional.empty());
        try {
            allocationRuleService.updateRule(200L,
                    new AssetClassAllocationRule(1L, AssetClassType.CRYPTO, 10.0, true));
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("200"));
        }
    }

    @Test(groups = "crud", priority = 14)
    public void testRecordHoldingValid() {
        HoldingRecord holding = new HoldingRecord(1L, AssetClassType.STOCKS, 1000.0, LocalDateTime.now());
        when(holdingRecordRepository.save(any())).thenReturn(holding);
        HoldingRecord saved = holdingRecordService.recordHolding(holding);
        Assert.assertTrue(saved.getCurrentValue() > 0);
    }

    @Test(groups = "crud", priority = 15)
    public void testRecordHoldingInvalidValue() {
        HoldingRecord holding = new HoldingRecord(1L, AssetClassType.STOCKS, 0.0, LocalDateTime.now());
        try {
            holdingRecordService.recordHolding(holding);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("must be > 0"));
        }
    }

    @Test(groups = "crud", priority = 16)
    public void testGetHoldingsByInvestorEmpty() {
        when(holdingRecordRepository.findByInvestorId(1L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(holdingRecordService.getHoldingsByInvestor(1L).isEmpty());
    }

    @Test(groups = "crud", priority = 17)
    public void testGetHoldingByIdPresent() {
        HoldingRecord holding = new HoldingRecord(1L, AssetClassType.BONDS, 500.0, LocalDateTime.now());
        holding.setId(5L);
        when(holdingRecordRepository.findById(5L)).thenReturn(Optional.of(holding));
        Optional<HoldingRecord> opt = holdingRecordService.getHoldingById(5L);
        Assert.assertTrue(opt.isPresent());
    }

    @Test(groups = "crud", priority = 18)
    public void testGetHoldingByIdAbsent() {
        when(holdingRecordRepository.findById(6L)).thenReturn(Optional.empty());
        Assert.assertTrue(holdingRecordService.getHoldingById(6L).isEmpty());
    }

    // ---------------------------------------------------------
    // Topic 3: Dependency Injection and IoC using Spring Framework
    // ---------------------------------------------------------

    @Test(groups = "di", priority = 19)
    public void testInvestorProfileServiceInjected() {
        Assert.assertNotNull(investorProfileService);
    }

    @Test(groups = "di", priority = 20)
    public void testAllocationRuleServiceInjected() {
        Assert.assertNotNull(allocationRuleService);
    }

    @Test(groups = "di", priority = 21)
    public void testHoldingRecordServiceInjected() {
        Assert.assertNotNull(holdingRecordService);
    }

    @Test(groups = "di", priority = 22)
    public void testSnapshotServiceInjected() {
        Assert.assertNotNull(allocationSnapshotService);
    }

    @Test(groups = "di", priority = 23)
    public void testAlertServiceInjected() {
        Assert.assertNotNull(rebalancingAlertService);
    }

    @Test(groups = "di", priority = 24)
    public void testJwtTokenProviderInjected() {
        Assert.assertNotNull(jwtTokenProvider);
    }

    @Test(groups = "di", priority = 25)
    public void testRepositoryInjectionMocks() {
        Assert.assertNotNull(investorProfileRepository);
        Assert.assertNotNull(allocationRuleRepository);
    }

    @Test(groups = "di", priority = 26)
    public void testUserDetailsServiceMockInjection() {
        Assert.assertNotNull(customUserDetailsService);
    }

    // ---------------------------------------------------------
    // Topic 4: Hibernate configs, generator, annotations, CRUD
    // ---------------------------------------------------------

    @Test(groups = "hibernate", priority = 27)
    public void testInvestorProfileIdGeneration() {
        InvestorProfile investor = new InvestorProfile("INV100", "Gen Test", "gen@example.com", true);
        investor.setId(100L);
        Assert.assertEquals(investor.getId(), Long.valueOf(100L));
    }

    @Test(groups = "hibernate", priority = 28)
    public void testAllocationRuleUniqueConstraintBehavior() {
        AssetClassAllocationRule rule = new AssetClassAllocationRule(2L, AssetClassType.CRYPTO, 10.0, true);
        rule.setId(20L);
        Assert.assertEquals(rule.getInvestorId(), Long.valueOf(2L));
    }

    @Test(groups = "hibernate", priority = 29)
    public void testHoldingRecordValueAnnotation() {
        HoldingRecord holding = new HoldingRecord(2L, AssetClassType.REAL_ESTATE, 2500.0, LocalDateTime.now());
        Assert.assertTrue(holding.getCurrentValue() > 0);
    }

    @Test(groups = "hibernate", priority = 30)
    public void testAllocationSnapshotRecordMinValue() {
        AllocationSnapshotRecord snap = new AllocationSnapshotRecord(1L, LocalDateTime.now(), 2000.0, "{}");
        Assert.assertTrue(snap.getTotalPortfolioValue() >= 1);
    }

    @Test(groups = "hibernate", priority = 31)
    public void testRebalancingAlertDefaultResolvedFalse() {
        RebalancingAlertRecord alert = new RebalancingAlertRecord(
                1L, AssetClassType.STOCKS, 70.0, 60.0,
                AlertSeverity.MEDIUM, "test", LocalDateTime.now(), false);
        Assert.assertFalse(alert.getResolved());
    }

    @Test(groups = "hibernate", priority = 32)
    public void testAlertCreationConstraintCurrentGreaterThanTarget() {
        RebalancingAlertRecord alert = new RebalancingAlertRecord(
                1L, AssetClassType.STOCKS, 70.0, 60.0,
                AlertSeverity.HIGH, "test", LocalDateTime.now(), false
        );
        when(alertRecordRepository.save(any())).thenReturn(alert);
        RebalancingAlertRecord saved = rebalancingAlertService.createAlert(alert);
        Assert.assertEquals(saved.getCurrentPercentage(), 70.0);
    }

    @Test(groups = "hibernate", priority = 33)
    public void testAlertCreationConstraintViolation() {
        RebalancingAlertRecord alert = new RebalancingAlertRecord(
                1L, AssetClassType.STOCKS, 50.0, 60.0,
                AlertSeverity.LOW, "no alert", LocalDateTime.now(), false
        );
        try {
            rebalancingAlertService.createAlert(alert);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("currentPercentage > targetPercentage"));
        }
    }

    @Test(groups = "hibernate", priority = 34)
    public void testResolveAlertSuccess() {
        RebalancingAlertRecord alert = new RebalancingAlertRecord(
                1L, AssetClassType.BONDS, 70.0, 60.0,
                AlertSeverity.LOW, "resolve", LocalDateTime.now(), false
        );
        alert.setId(10L);
        when(alertRecordRepository.findById(10L)).thenReturn(Optional.of(alert));
        when(alertRecordRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        RebalancingAlertRecord resolved = rebalancingAlertService.resolveAlert(10L);
        Assert.assertTrue(resolved.getResolved());
    }

    @Test(groups = "hibernate", priority = 35)
    public void testResolveAlertNotFound() {
        when(alertRecordRepository.findById(300L)).thenReturn(Optional.empty());
        try {
            rebalancingAlertService.resolveAlert(300L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("300"));
        }
    }

    @Test(groups = "hibernate", priority = 36)
    public void testGetAlertsByInvestor() {
        when(alertRecordRepository.findByInvestorId(1L))
                .thenReturn(Collections.emptyList());
        Assert.assertTrue(rebalancingAlertService.getAlertsByInvestor(1L).isEmpty());
    }

    // ---------------------------------------------------------
    // Topic 5: JPA mapping with normalization (1NF, 2NF, 3NF)
    // ---------------------------------------------------------

    @Test(groups = "jpa", priority = 37)
    public void testInvestorProfileNormalization1NF() {
        InvestorProfile investor = new InvestorProfile("INV200", "Normalized", "norm@example.com", true);
        Assert.assertNotNull(investor.getEmail());
        Assert.assertFalse(investor.getEmail().contains(","));
    }

    @Test(groups = "jpa", priority = 38)
    public void testHoldingRecordNormalization2NF() {
        HoldingRecord record = new HoldingRecord(3L, AssetClassType.STOCKS, 100.0, LocalDateTime.now());
        Assert.assertEquals(record.getInvestorId(), Long.valueOf(3L));
    }

    @Test(groups = "jpa", priority = 39)
    public void testAllocationSnapshotNormalization3NF() {
        AllocationSnapshotRecord snap = new AllocationSnapshotRecord(3L, LocalDateTime.now(), 5000.0, "{}");
        Assert.assertEquals(snap.getInvestorId(), Long.valueOf(3L));
    }

    @Test(groups = "jpa", priority = 40)
    public void testInvestorProfileUniqueEmail() {
        InvestorProfile investor1 = new InvestorProfile("INV300", "User1", "unique@example.com", true);
        investor1.setId(1L);
        InvestorProfile investor2 = new InvestorProfile("INV301", "User2", "unique@example.com", true);
        investor2.setId(2L);
        Assert.assertNotEquals(investor1.getId(), investor2.getId());
    }

    @Test(groups = "jpa", priority = 41)
    public void testRulePerAssetClassPerInvestorLogical() {
        AssetClassAllocationRule r1 = new AssetClassAllocationRule(5L, AssetClassType.CASH, 5.0, true);
        AssetClassAllocationRule r2 = new AssetClassAllocationRule(5L, AssetClassType.BONDS, 15.0, true);
        Assert.assertNotEquals(r1.getAssetClass(), r2.getAssetClass());
    }

    @Test(groups = "jpa", priority = 42)
    public void testOneSnapshotPerInvestorPerTimestampLogical() {
        LocalDateTime now = LocalDateTime.now();
        AllocationSnapshotRecord s1 = new AllocationSnapshotRecord(7L, now, 1000.0, "{}");
        AllocationSnapshotRecord s2 = new AllocationSnapshotRecord(7L, now, 2000.0, "{}");
        Assert.assertEquals(s1.getSnapshotDate(), s2.getSnapshotDate());
    }

    @Test(groups = "jpa", priority = 43)
    public void testTotalPortfolioValuePositive() {
        AllocationSnapshotRecord snap = new AllocationSnapshotRecord(2L, LocalDateTime.now(), 1000.0, "{}");
        Assert.assertTrue(snap.getTotalPortfolioValue() > 0);
    }

    @Test(groups = "jpa", priority = 44)
    public void testRebalancingAlertSeverityEnum() {
        RebalancingAlertRecord alert = new RebalancingAlertRecord(
                1L, AssetClassType.CASH, 15.0, 5.0,
                AlertSeverity.HIGH, "High", LocalDateTime.now(), false
        );
        Assert.assertEquals(alert.getSeverity(), AlertSeverity.HIGH);
    }

    // ---------------------------------------------------------
    // Topic 6: Many-to-Many relationships and associations
    // (logical multi-association tests using lists)
    // ---------------------------------------------------------

    @Test(groups = "manyToMany", priority = 45)
    public void testInvestorToMultipleHoldingsAssociation() {
        List<HoldingRecord> holdings = Arrays.asList(
                new HoldingRecord(10L, AssetClassType.STOCKS, 100.0, LocalDateTime.now()),
                new HoldingRecord(10L, AssetClassType.BONDS, 200.0, LocalDateTime.now())
        );
        when(holdingRecordRepository.findByInvestorId(10L)).thenReturn(holdings);
        List<HoldingRecord> result = holdingRecordService.getHoldingsByInvestor(10L);
        Assert.assertEquals(result.size(), 2);
    }

    @Test(groups = "manyToMany", priority = 46)
    public void testInvestorRulesAssociation() {
        List<AssetClassAllocationRule> rules = Arrays.asList(
                new AssetClassAllocationRule(10L, AssetClassType.STOCKS, 60.0, true),
                new AssetClassAllocationRule(10L, AssetClassType.BONDS, 20.0, true)
        );
        when(allocationRuleRepository.findByInvestorId(10L)).thenReturn(rules);
        Assert.assertEquals(allocationRuleService.getRulesByInvestor(10L).size(), 2);
    }

    @Test(groups = "manyToMany", priority = 47)
    public void testInvestorMultipleAlertsAssociation() {
        List<RebalancingAlertRecord> alerts = Arrays.asList(
                new RebalancingAlertRecord(10L, AssetClassType.STOCKS, 70.0, 60.0,
                        AlertSeverity.LOW, "msg1", LocalDateTime.now(), false),
                new RebalancingAlertRecord(10L, AssetClassType.BONDS, 40.0, 30.0,
                        AlertSeverity.MEDIUM, "msg2", LocalDateTime.now(), false)
        );
        when(alertRecordRepository.findByInvestorId(10L)).thenReturn(alerts);
        Assert.assertEquals(rebalancingAlertService.getAlertsByInvestor(10L).size(), 2);
    }

    @Test(groups = "manyToMany", priority = 48)
    public void testAssociationHoldingsAndRulesSizes() {
        List<HoldingRecord> holdings = Arrays.asList(
                new HoldingRecord(11L, AssetClassType.STOCKS, 100.0, LocalDateTime.now()),
                new HoldingRecord(11L, AssetClassType.STOCKS, 200.0, LocalDateTime.now())
        );
        when(holdingRecordRepository.findByInvestorId(11L)).thenReturn(holdings);
        Assert.assertEquals(holdingRecordService.getHoldingsByInvestor(11L).size(), 2);
    }

    @Test(groups = "manyToMany", priority = 49)
    public void testAssociationNoHoldings() {
        when(holdingRecordRepository.findByInvestorId(12L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(holdingRecordService.getHoldingsByInvestor(12L).isEmpty());
    }

    @Test(groups = "manyToMany", priority = 50)
    public void testAssociationNoRules() {
        when(allocationRuleRepository.findByInvestorId(12L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(allocationRuleService.getRulesByInvestor(12L).isEmpty());
    }

    @Test(groups = "manyToMany", priority = 51)
    public void testAssociationAlertsEmpty() {
        when(alertRecordRepository.findByInvestorId(12L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(rebalancingAlertService.getAlertsByInvestor(12L).isEmpty());
    }

    @Test(groups = "manyToMany", priority = 52)
    public void testAssociationAlertsNonEmpty() {
        List<RebalancingAlertRecord> alerts = Collections.singletonList(
                new RebalancingAlertRecord(13L, AssetClassType.CRYPTO, 90.0, 50.0,
                        AlertSeverity.HIGH, "crypto high", LocalDateTime.now(), false)
        );
        when(alertRecordRepository.findByInvestorId(13L)).thenReturn(alerts);
        Assert.assertEquals(rebalancingAlertService.getAlertsByInvestor(13L).size(), 1);
    }

    // ---------------------------------------------------------
    // Topic 7: Basic security controls and JWT token-based auth
    // ---------------------------------------------------------

    @Test(groups = "security", priority = 53)
    public void testJwtTokenGenerationContainsUserId() {
        UserAccount user = new UserAccount("user1", "user1@example.com", "pwd", RoleType.INVESTOR);
        user.setId(1L);
        when(authentication.getName()).thenReturn("user1");
        String token = jwtTokenProvider.generateToken(authentication, user);
        Assert.assertNotNull(token);
    }

    @Test(groups = "security", priority = 54)
    public void testJwtTokenValidateSuccess() {
        UserAccount user = new UserAccount("user2", "user2@example.com", "pwd", RoleType.ANALYST);
        user.setId(2L);
        when(authentication.getName()).thenReturn("user2");
        String token = jwtTokenProvider.generateToken(authentication, user);
        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test(groups = "security", priority = 55)
    public void testJwtTokenValidateFailure() {
        Assert.assertFalse(jwtTokenProvider.validateToken("invalid.token.value"));
    }

    @Test(groups = "security", priority = 56)
    public void testJwtTokenGetUsername() {
        UserAccount user = new UserAccount("user3", "user3@example.com", "pwd", RoleType.ADMIN);
        user.setId(3L);
        when(authentication.getName()).thenReturn("user3");
        String token = jwtTokenProvider.generateToken(authentication, user);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Assert.assertEquals(username, "user3");
    }

    @Test(groups = "security", priority = 57)
    public void testAuthenticationManagerInvocation() {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("user4", "pwd");
        when(authenticationManager.authenticate(any())).thenReturn(authToken);
        Authentication result = authenticationManager.authenticate(authToken);
        Assert.assertNotNull(result);
    }

    @Test(groups = "security", priority = 58)
    public void testRoleInvestorAuthorityNaming() {
        UserAccount user = new UserAccount("invUser", "inv@example.com", "pwd", RoleType.INVESTOR);
        Assert.assertEquals(user.getRole(), RoleType.INVESTOR);
    }

    @Test(groups = "security", priority = 59)
    public void testRoleAnalystAuthorityNaming() {
        UserAccount user = new UserAccount("analystUser", "analyst@example.com", "pwd", RoleType.ANALYST);
        Assert.assertEquals(user.getRole(), RoleType.ANALYST);
    }

    @Test(groups = "security", priority = 60)
    public void testRoleAdminAuthorityNaming() {
        UserAccount user = new UserAccount("adminUser", "admin@example.com", "pwd", RoleType.ADMIN);
        Assert.assertEquals(user.getRole(), RoleType.ADMIN);
    }

    @Test(groups = "security", priority = 61)
    public void testPasswordEncodingMock() {
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        String encoded = passwordEncoder.encode("plain");
        Assert.assertEquals(encoded, "encoded");
    }

    @Test(groups = "security", priority = 62)
    public void testSecurityContextNotSetForInvalidToken() {
        Assert.assertFalse(jwtTokenProvider.validateToken("bad-token"));
    }

    // ---------------------------------------------------------
    // Topic 8: HQL and HCQL to perform advanced data querying
    // ---------------------------------------------------------

    @Test(groups = "hql", priority = 63)
    public void testHqlActiveRulesQuery() {
        List<AssetClassAllocationRule> rules = Collections.singletonList(
                new AssetClassAllocationRule(20L, AssetClassType.STOCKS, 50.0, true)
        );
        when(allocationRuleRepository.findActiveRulesHql(20L)).thenReturn(rules);
        Assert.assertEquals(allocationRuleRepository.findActiveRulesHql(20L).size(), 1);
    }

    @Test(groups = "hql", priority = 64)
    public void testHqlHoldingsByValueQuery() {
        List<HoldingRecord> list = Arrays.asList(
                new HoldingRecord(1L, AssetClassType.STOCKS, 1000.0, LocalDateTime.now()),
                new HoldingRecord(1L, AssetClassType.BONDS, 2000.0, LocalDateTime.now())
        );
        when(holdingRecordRepository.findByValueGreaterThan(500.0)).thenReturn(list);
        Assert.assertEquals(holdingRecordRepository.findByValueGreaterThan(500.0).size(), 2);
    }

    @Test(groups = "hql", priority = 65)
    public void testHqlHoldingsByValueEmptyResult() {
        when(holdingRecordRepository.findByValueGreaterThan(10000.0)).thenReturn(Collections.emptyList());
        Assert.assertTrue(holdingRecordRepository.findByValueGreaterThan(10000.0).isEmpty());
    }

    @Test(groups = "hql", priority = 66)
    public void testHqlHoldingsByAssetClassQuery() {
        List<HoldingRecord> list = Collections.singletonList(
                new HoldingRecord(2L, AssetClassType.CRYPTO, 5000.0, LocalDateTime.now())
        );
        when(holdingRecordRepository.findByInvestorAndAssetClass(2L, AssetClassType.CRYPTO)).thenReturn(list);
        Assert.assertEquals(holdingRecordRepository.findByInvestorAndAssetClass(2L, AssetClassType.CRYPTO).size(), 1);
    }

    @Test(groups = "hql", priority = 67)
    public void testSnapshotComputationWithAlerts() {
        List<HoldingRecord> holdings = Arrays.asList(
                new HoldingRecord(30L, AssetClassType.STOCKS, 800.0, LocalDateTime.now()),
                new HoldingRecord(30L, AssetClassType.BONDS, 200.0, LocalDateTime.now())
        );
        when(holdingRecordRepository.findByInvestorId(30L)).thenReturn(holdings);
        AssetClassAllocationRule rule = new AssetClassAllocationRule(30L, AssetClassType.STOCKS, 50.0, true);
        when(allocationRuleRepository.findByInvestorIdAndActiveTrue(30L))
                .thenReturn(Collections.singletonList(rule));
        when(snapshotRecordRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(alertRecordRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AllocationSnapshotRecord snapshot = allocationSnapshotService.computeSnapshot(30L);
        Assert.assertTrue(snapshot.getTotalPortfolioValue() > 0);
    }

    @Test(groups = "hql", priority = 68)
    public void testSnapshotComputationNoHoldingsThrows() {
        when(holdingRecordRepository.findByInvestorId(40L)).thenReturn(Collections.emptyList());
        try {
            allocationSnapshotService.computeSnapshot(40L);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("No holdings"));
        }
    }

    @Test(groups = "hql", priority = 69)
    public void testGetSnapshotByIdNotFound() {
        when(snapshotRecordRepository.findById(1000L)).thenReturn(Optional.empty());
        try {
            allocationSnapshotService.getSnapshotById(1000L);
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("1000"));
        }
    }

    @Test(groups = "hql", priority = 70)
    public void testGetAllSnapshotsEmpty() {
        when(snapshotRecordRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(allocationSnapshotService.getAllSnapshots().isEmpty());
    }
}
