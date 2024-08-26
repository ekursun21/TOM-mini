package com.TOM.tom_mini.money.controller;

import com.TOM.tom_mini.money.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/fees")
public class FeeController {

    private final FeeService feeService;

    @Autowired
    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/{feeType}/{accountType}")
    public ResponseEntity<BigDecimal> getFee(@PathVariable("feeType") String feeType,
                                             @PathVariable("accountType") String accountType) {
        try {
            BigDecimal fee = feeService.getFee(feeType, accountType);
            return new ResponseEntity<>(fee, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
