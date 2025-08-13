package com.s_giken.training.webapp.model.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.s_giken.training.webapp.model.PaymentMethod;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Nullable
    private Long memberId;

    @NotBlank
    private String mail;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Nullable
    private LocalDate endDate;

    @NotNull
    private PaymentMethod paymentMethod;

    @Nullable
    private Timestamp createdAt;

    @Nullable
    private Timestamp modifiedAt;
}
