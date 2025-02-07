package java.com.msy.wallet.service;

import com.msy.wallet.exception.ErrorCode;
import com.msy.wallet.exception.WalletServiceException;
import com.msy.wallet.model.Transaction;
import com.msy.wallet.model.User;
import com.msy.wallet.repository.TransactionRepository;
import com.msy.wallet.repository.UserRepository;
import com.msy.wallet.service.WalletServiceImpl;
import com.msy.wallet.util.ReferenceIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ReferenceIdGenerator referenceIdGenerator;

    @InjectMocks
    private WalletServiceImpl walletService;

    private User user;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        user = new User()
                .setId(1L)
                .setBalance(1000L);

        transaction = new Transaction()
                .setId(1L)
                .setUser(user)
                .setAmount(500L)
                .setReferenceId("REF123");
    }

    @Test
    void testGetBalance_UserFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = walletService.getBalance(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getBalance());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBalance_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        WalletServiceException exception = assertThrows(WalletServiceException.class, () -> {
            walletService.getBalance(1L);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        assertEquals(1L, 1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testAddMoney_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(referenceIdGenerator.generateReferenceId()).thenReturn("REF123");
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction result = walletService.addMoney(1L, 500L);

        // Assert
        assertNotNull(result);
        assertEquals("REF123", result.getReferenceId());
        assertEquals(500L, result.getAmount());
        assertEquals(1500L, user.getBalance()); // Verify balance is updated
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testAddMoney_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        WalletServiceException exception = assertThrows(WalletServiceException.class, () -> {
            walletService.addMoney(1L, 500L);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        assertEquals(1L, 1);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}