```java
wrapper.and(s -> s.like(CustomerLabelMapping::getDrugId, drugId)
                    .or().like(CustomerLabelMapping::getDrugCode, drugId)
                    .or().like(CustomerLabelMapping::getDrugName, drugId));
                        
```