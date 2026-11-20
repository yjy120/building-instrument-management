package com.example.device.service;

import com.example.device.dto.PurchaseApplyRequest;
import com.example.device.entity.PurchaseApply;
import com.example.device.entity.PurchaseTotal;
import com.example.device.entity.SysUser;
import com.example.device.repository.PurchaseApplyRepository;
import com.example.device.repository.PurchaseTotalRepository;
import com.example.device.repository.SysUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseApplyRepository purchaseApplyRepository;
    private final PurchaseTotalRepository purchaseTotalRepository;
    private final SysUserRepository userRepository;
    
    public PurchaseService(PurchaseApplyRepository purchaseApplyRepository,
                          PurchaseTotalRepository purchaseTotalRepository,
                          SysUserRepository userRepository) {
        this.purchaseApplyRepository = purchaseApplyRepository;
        this.purchaseTotalRepository = purchaseTotalRepository;
        this.userRepository = userRepository;
    }
    
    public PurchaseApply createApply(PurchaseApplyRequest request, Long applicantId) {
        PurchaseApply apply = new PurchaseApply();
        apply.setDeviceName(request.getDeviceName());
        apply.setQuantity(request.getQuantity());
        apply.setProjectUsage(request.getProjectUsage());
        apply.setApplicantId(applicantId);
        apply.setStatus("PENDING");
        return purchaseApplyRepository.save(apply);
    }
    
    public PurchaseApply updateApplyStatus(Long id, String status, String comment) {
        PurchaseApply apply = purchaseApplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购申请不存在"));
        apply.setStatus(status);
        apply.setReviewComment(comment);
        return purchaseApplyRepository.save(apply);
    }
    
    public List<PurchaseApply> getAppliesByApplicant(Long applicantId) {
        return purchaseApplyRepository.findByApplicantId(applicantId);
    }
    
    public List<PurchaseApply> getPendingApplies() {
        return purchaseApplyRepository.findByStatus("PENDING");
    }
    
    public List<PurchaseApply> getAllApplies() {
        return purchaseApplyRepository.findAll();
    }
    
    public PurchaseTotal createTotal(List<Long> applyIds) {
        List<PurchaseApply> applies = purchaseApplyRepository.findAllById(applyIds);
        PurchaseTotal total = new PurchaseTotal();
        total.setTotalList(applies.toString());
        total.setStatus("PENDING");
        return purchaseTotalRepository.save(total);
    }
    
    public PurchaseTotal reviewTotal(Long id, String status, String comment, Long reviewerId) {
        PurchaseTotal total = purchaseTotalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购汇总不存在"));
        total.setStatus(status);
        total.setReviewComment(comment);
        total.setReviewerId(reviewerId);
        
        if ("APPROVED".equals(status)) {
            purchaseApplyRepository.findAll().forEach(apply -> {
                if ("PENDING".equals(apply.getStatus())) {
                    apply.setStatus("APPROVED");
                    purchaseApplyRepository.save(apply);
                }
            });
        }
        
        return purchaseTotalRepository.save(total);
    }
    
    public List<PurchaseTotal> getAllTotals() {
        return purchaseTotalRepository.findAll();
    }
}
