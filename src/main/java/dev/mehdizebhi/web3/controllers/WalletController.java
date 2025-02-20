package dev.mehdizebhi.web3.controllers;

import dev.mehdizebhi.web3.constants.Cryptocurrency;
import dev.mehdizebhi.web3.controllers.dto.TransactionDto;
import dev.mehdizebhi.web3.controllers.dto.WalletDto;
import dev.mehdizebhi.web3.controllers.request.CreateWalletRequest;
import dev.mehdizebhi.web3.controllers.request.SendTransactionRequest;
import dev.mehdizebhi.web3.controllers.response.ApiResponse;
import dev.mehdizebhi.web3.controllers.response.Pagination;
import dev.mehdizebhi.web3.entities.TransactionEntity;
import dev.mehdizebhi.web3.entities.WalletEntity;
import dev.mehdizebhi.web3.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController extends BaseController {

    private final WalletService walletService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<WalletDto>>> getWallets(@PageableDefault Pageable pageable) {
        var walletPage = walletService.wallets(user(), pageable);
        var wallets = walletPage.getContent().stream().map(this::toDto).toList();
        return ResponseEntity.ok(ApiResponse.<List<WalletDto>>builder()
                .data(wallets)
                .pagination(Pagination.of(walletPage))
                .message("success")
                .build());
    }

    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse<WalletDto>> createWallet(@RequestBody CreateWalletRequest request) {
        var result = walletService.createWallet(user(), Cryptocurrency.fromString(request.currency()));
        return ResponseEntity.ok(ApiResponse.<WalletDto>builder()
                .data(toDto(result))
                .message("success")
                .build());
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<ApiResponse<WalletDto>> wallet(@PathVariable Integer walletId) {
        var result = walletService.wallet(user(), walletId);
        return ResponseEntity.ok(ApiResponse.<WalletDto>builder()
                .data(toDto(result))
                .message("success")
                .build());
    }

    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> walletTransactions(
            @PathVariable Integer walletId,
            @PageableDefault Pageable pageable) {
        var result = walletService.transactions(user(), walletId, pageable);
        return ResponseEntity.ok(ApiResponse.<List<TransactionDto>>builder()
                .data(result.getContent().stream().map(this::toTransactionDto).toList())
                .pagination(Pagination.of(result))
                .message("success")
                .build());
    }

    @PostMapping("/{walletId}/transactions")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse<TransactionDto>> sendTransaction(
            @PathVariable Integer walletId,
            @RequestBody SendTransactionRequest request) {
        var result = walletService.sendTransaction(user(), walletId, request.recipientAddress(), request.amount());
        return ResponseEntity.ok(ApiResponse.<TransactionDto>builder()
                .data(toTransactionDto(result))
                .message("success")
                .build());
    }

    // --------------------------------
    // Private Helper
    // --------------------------------

    private WalletDto toDto(WalletEntity wallet) {
        return WalletDto.builder()
                .id(wallet.getId())
                .currency(wallet.getCryptocurrency().name())
                .publicAddress(wallet.getPublicAddress())
                .availableBalance(wallet.getAvailableBalance())
                .unconfirmedBalance(wallet.getUnconfirmedBalance())
                .build();
    }

    private TransactionDto toTransactionDto(TransactionEntity transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .txHash(transaction.getTxHash())
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .type(transaction.getTransactionType().name())
                .status(transaction.getStatus().name())
                .build();
    }
}