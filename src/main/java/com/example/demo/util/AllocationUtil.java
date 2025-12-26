package com.example.demo.util;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllocationUtil {

    // Calculate total portfolio value
    public static double calculateTotalValue(List<HoldingRecord> holdings) {
        return holdings.stream()
                .mapToDouble(HoldingRecord::getValue)
                .sum();
    }

    // Calculate percentage allocation by asset class
    public static Map<AssetClassType, Double> calculateAllocationPercentages(List<HoldingRecord> holdings, double totalValue) {
        Map<AssetClassType, Double> percentages = new HashMap<>();
        for (HoldingRecord h : holdings) {
            percentages.put(h.getAssetClass(), h.getValue() / totalValue * 100);
        }
        return percentages;
    }

    // Convert map to JSON string (simple placeholder)
    public static String toJson(Map<AssetClassType, Double> percentages) {
        return percentages.toString();
    }
}
