package com.achiever.menschenfahren;

import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.BooleanTypeManufacturerImpl;

public class CustomBooleanStrategy extends BooleanTypeManufacturerImpl {

    public Boolean getType(final DataProviderStrategy strategy, final AttributeMetadata attributeMetadata) {
        // always returns true, as false already might be default value and it will be unclear if mapping actually happened or it was default value.
        return true;
    }

    @Override
    public Boolean getBoolean(final AttributeMetadata attributeMetadata) {
        // always returns true, as false already might be default value and it will be unclear if mapping actually happened or it was default value.
        return true;
    }

}
