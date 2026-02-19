package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.repository.CompleteDataRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class CompleteDataServiceImpl implements CompleteDataService {

    private final CompleteDataRepository completeDataRepository;

    public CompleteDataServiceImpl(CompleteDataRepository completeDataRepository) {
        this.completeDataRepository = completeDataRepository;
    }

    @Override
    @Cacheable(value = "completeDataAll", key = "#country == null ? 'ALL' : #country")
    public List<CompleteDataDTO> getAllData(String country) {
        return runCompleteDataQuery(null, null, country);
    }

    @Override
    @Cacheable(
        value = "completeDataByUser",
        key = "#userId + ':' + #isAdmin + ':' + (#country == null ? 'ALL' : #country)"
    )
    public List<CompleteDataDTO> getDataByUser(Long userId, boolean isAdmin, String country) {
        if (isAdmin) {
            return getAllData(country);
        }

        List<Object[]> scopeRows = completeDataRepository.findUserScope(userId);
        if (CollectionUtils.isEmpty(scopeRows)) {
            return Collections.emptyList();
        }
        Object[] scope = scopeRows.get(0);

        Long resolvedUserId = toLong(scope[0]);
        Long parentCompanyId = toLong(scope[1]);

        if (resolvedUserId == null) {
            return Collections.emptyList();
        }
        if (parentCompanyId != null) {
            return runCompleteDataQuery(parentCompanyId, resolvedUserId, country);
        }

        return runCompleteDataQuery(resolvedUserId, null, country);
    }

    private List<CompleteDataDTO> runCompleteDataQuery(
            Long companyIdFilter,
            Long branchIdFilter,
            String countryFilter
    ) {
        String normalizedCountry = (countryFilter == null || countryFilter.isBlank())
                ? null
                : countryFilter.toLowerCase();

        List<Object[]> rows = completeDataRepository.findCompleteBranchRows(
                companyIdFilter,
                branchIdFilter,
                normalizedCountry
        );

        List<CompleteDataDTO> companyRows = branchIdFilter == null
                ? fetchCompanyRows(companyIdFilter, normalizedCountry)
                : Collections.emptyList();

        List<CompleteDataDTO> branchRows = rows.stream()
                .map(this::mapBranchRow)
                .toList();

        List<CompleteDataDTO> result = Stream.concat(companyRows.stream(), branchRows.stream())
                .collect(Collectors.toCollection(ArrayList::new));

        IntStream.range(0, result.size())
                .forEach(i -> result.get(i).setHierarchyOrder((long) i + 1));
        return result;
    }

    private List<CompleteDataDTO> fetchCompanyRows(Long companyIdFilter, String normalizedCountry) {
        List<Object[]> rows = completeDataRepository.findCompleteCompanyRows(companyIdFilter, normalizedCountry);

        return rows.stream()
                .map(this::mapCompanyRow)
                .toList();
    }

    private CompleteDataDTO mapBranchRow(Object[] row) {
        Long companyId = toLong(row[0]);
        String companyName = toString(row[1]);
        String gstNo = toString(row[2]);
        String phoneNo = toString(row[3]);
        String addressType = toString(row[4]);
        String addressLine1 = toString(row[5]);
        String addressLine2 = toString(row[6]);
        String city = toString(row[7]);
        String state = toString(row[8]);
        String country = toString(row[9]);
        Long branchId = toLong(row[10]);
        String branchName = toString(row[11]);
        Double totalPurchaseAmount = toDouble(row[12]);
        Double totalSalesAmount = toDouble(row[13]);
        Long totalProducts = toLong(row[14]);

        CompleteDataDTO dto = new CompleteDataDTO();
        dto.setId(branchId != null ? branchId : companyId);
        dto.setType(branchId != null ? "Branch" : "Company");
        dto.setCompanyName(companyName);
        dto.setBranchName(branchName);
        dto.setParentCompany(branchId != null ? companyName : null);
        dto.setGstNo(gstNo);
        dto.setPhoneNo(phoneNo);
        dto.setAddressType(addressType);
        dto.setAddressLine1(addressLine1);
        dto.setAddressLine2(addressLine2);
        dto.setCity(city);
        dto.setState(state);
        dto.setCountry(country);
        dto.setUserFrontId(branchId != null ? branchId : companyId);
        dto.setProductCount(totalProducts == null ? 0L : totalProducts);
        dto.setTotalPurchaseAmount(zeroIfNull(totalPurchaseAmount));
        dto.setTotalSalesAmount(zeroIfNull(totalSalesAmount));
        dto.setTotalProducts(totalProducts == null ? 0L : totalProducts);
        return dto;
    }

    private CompleteDataDTO mapCompanyRow(Object[] row) {
        Long companyId = toLong(row[0]);
        String companyName = toString(row[1]);
        String gstNo = toString(row[2]);
        String phoneNo = toString(row[3]);
        String addressType = toString(row[4]);
        String addressLine1 = toString(row[5]);
        String addressLine2 = toString(row[6]);
        String city = toString(row[7]);
        String state = toString(row[8]);
        String country = toString(row[9]);
        Double totalPurchaseAmount = toDouble(row[10]);
        Double totalSalesAmount = toDouble(row[11]);
        Long totalProducts = toLong(row[12]);

        CompleteDataDTO dto = new CompleteDataDTO();
        dto.setId(companyId);
        dto.setType("Company");
        dto.setCompanyName(companyName);
        dto.setBranchName(null);
        dto.setParentCompany(null);
        dto.setGstNo(gstNo);
        dto.setPhoneNo(phoneNo);
        dto.setAddressType(addressType);
        dto.setAddressLine1(addressLine1);
        dto.setAddressLine2(addressLine2);
        dto.setCity(city);
        dto.setState(state);
        dto.setCountry(country);
        dto.setUserFrontId(companyId);
        dto.setProductCount(totalProducts == null ? 0L : totalProducts);
        dto.setTotalPurchaseAmount(zeroIfNull(totalPurchaseAmount));
        dto.setTotalSalesAmount(zeroIfNull(totalSalesAmount));
        dto.setTotalProducts(totalProducts == null ? 0L : totalProducts);
        return dto;
    }

    private Long toLong(Object value) {
        return value == null ? null : ((Number) value).longValue();
    }

    private Double toDouble(Object value) {
        return value == null ? null : ((Number) value).doubleValue();
    }

    private String toString(Object value) {
        return value == null ? null : value.toString();
    }

    private Double zeroIfNull(Double value) {
        return value == null ? 0.0 : value;
    }
}
