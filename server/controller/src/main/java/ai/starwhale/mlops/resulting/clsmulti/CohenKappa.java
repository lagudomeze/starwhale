/*
 * Copyright 2022.1-2022
 * StarWhale.ai All right reserved. This software is the confidential and proprietary information of
 * StarWhale.ai ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with StarWhale.ai.
 */

package ai.starwhale.mlops.resulting.clsmulti;

import ai.starwhale.mlops.resulting.Indicator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * refer to https://en.wikipedia.org/wiki/Cohen%27s_kappa
 */
public class CohenKappa extends Indicator<Double> {

    public static final String NAME = "CohenKappa";

    final AtomicInteger totalSamples = new AtomicInteger(0);
    final AtomicInteger rightSamples = new AtomicInteger(0);
    //hold the amount of samples for each label
    final Map<String, AtomicInteger> labelAmountReal = new HashMap<>();
    //hold the amount of samples for each prediction
    final Map<String, AtomicInteger> labelAmountPrediction = new HashMap<>();

    public CohenKappa(MCConfusionMetrics confusionMetrics) {
        this.key = NAME;
        for (MCIndicator indicator : confusionMetrics.getValue()) {
            update(indicator);
        }
        calculate();
    }

    public void calculate() {
        double p0 = rightSamples.doubleValue() / totalSamples.doubleValue();
        final Set<Entry<String, AtomicInteger>> entrySet = labelAmountReal.entrySet();
        final double pe = entrySet.stream().map(entry -> {
            final String key = entry.getKey();
            final int predictionAmount = labelAmountPrediction.getOrDefault(key,new AtomicInteger(0)).intValue();
            return predictionAmount * entry.getValue().intValue();
        }).mapToDouble(Integer::doubleValue).sum() / (totalSamples.doubleValue() * totalSamples.doubleValue());

        this.value = (p0 - pe) / (1 - pe);
    }

    private void update(MCIndicator indicator) {
        totalSamples.addAndGet(indicator.getValue().intValue());
        if (indicator.right()) {
            rightSamples.addAndGet(indicator.getValue().intValue());
        }
        final String label = indicator.getLabel();
        final String prediction = indicator.getPrediction();
        addValue(label, labelAmountReal, indicator.getValue().intValue());
        addValue(prediction, labelAmountPrediction, indicator.getValue().intValue());
    }

    /**
     * @param map only map in the fields of the class could be passed, so that it could be synchronized
     */
    private void addValue(String key, Map<String, AtomicInteger> map, Integer value) {
        AtomicInteger labelRealAmount = map.get(key);
        if (null == labelRealAmount) {
            synchronized (map){
                labelRealAmount = map.get(key);
                if(null == labelRealAmount){
                    labelRealAmount = new AtomicInteger(0);
                    map.put(key, labelRealAmount);
                }
            }

        }
        labelRealAmount.addAndGet(value);
    }

    public void feed(MCIndicator indicator){
        update(indicator);
    }
}
