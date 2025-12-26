package com.example.demo.util;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllocationUtil {

    // 🔴 CORRECTED: Use getCurrentValue() instead of getValue()
    public static double calculateTotalValue(List<HoldingRecord> holdings) {
        return holdings.stream()
                .mapToDouble(HoldingRecord::getCurrentValue) // ✅ corrected line
                .sum();
    }

    // 🔴 CORRECTED: Use getCurrentValue() instead of getValue()
    public static Map<AssetClassType, Double> calculateAllocationPercentages(
            List<HoldingRecord> holdings,
            double totalValue
    ) {
        Map<AssetClassType, Double> percentages = new HashMap<>();
        for (HoldingRecord h : holdings) {
            percentages.put(
                    h.getAssetClass(),
                    h.getCurrentValue() / totalValue * 100 // ✅ corrected line
            );
        }
        return percentages;
    }

    public static String toJson(Map<AssetClassType, Double> percentages) {
        StringBuilder sb = new StringBuilder("{");
        percentages.forEach((k, v) -> sb.append("\"")
                .append(k.name())
                .append("\":")
                .append(String.format("%.2f", v))
                .append(","));
        if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1); // remove last comma
        sb.append("}");
        return sb.toString();
    }
}
