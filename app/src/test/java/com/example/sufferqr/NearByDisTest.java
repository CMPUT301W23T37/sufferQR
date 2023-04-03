package com.example.sufferqr;

import static com.example.sufferqr.nearbyQrCodeList.isWithinOneKilometer;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * distance test
 */
public class NearByDisTest {
    /**
     * same distance check
     */
    @Test
    public void testIsWithinOneKilometer_sameLocation_returnsTrue() {
        // Given


        // When
        double result = nearbyQrCodeList.isWithinOneKilometer(0.0, 0.0,0.0,0.0);

        // Then
        assertEquals(0.0, result, 0.001);
    }

    /**
     * with in 1 km
     */
    @Test
    public void testIsWithinOneKilometer_withinOneKilometer_returnsTrue() {

        // When
        double result = nearbyQrCodeList.isWithinOneKilometer(12.9715987,77.5945627, 12.977396,77.590063);

        // Then
        assertEquals(808.252, result, 1);
    }

    /**
     * out of 1 km
     */
    @Test
    public void testIsWithinOneKilometer_outsideOneKilometer_returnsFalse() {
        // When
        double result =  nearbyQrCodeList.isWithinOneKilometer(12.9715987, 77.5945627,12.989643,77.577956);

        // Then
        assertEquals(2695.106, result, 1);
    }
}
