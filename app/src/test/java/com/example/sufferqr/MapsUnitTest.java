package com.example.sufferqr;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.android.gms.maps.model.LatLng;

/**
 * 1km distance true/false test
 */
@RunWith(MockitoJUnitRunner.class)
public class MapsUnitTest {

    @Mock
    private LatLng location1 ;

    @Mock
    private LatLng location2 ;

    /**
     * same locating
     */
    @Test
    public void testIsWithinOneKilometer_sameLocation_returnsTrue() {
        // Given
        location1 = new LatLng(12.9715987,77.5945627);
        location2 = new LatLng(12.9715987,77.5945627);

        // When
        boolean result = MapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertTrue(result);
    }

    /**
     * with in 1 KM
     */
    @Test
    public void testIsWithinOneKilometer_withinOneKilometer_returnsTrue() {
        // Given
        location1 = new LatLng(12.9715987,77.5945627);
        location2 = new LatLng(12.977396,77.590063);

        // When
        boolean result = MapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertTrue(result);
    }

    /**
     * out of 1 km
     */
    @Test
    public void testIsWithinOneKilometer_outsideOneKilometer_returnsFalse() {
        // Given
        location1 = new LatLng(12.9715987,77.5945627);
        location2 = new LatLng(12.989643,77.577956);

        // When
        boolean result = MapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertFalse(result);
    }
}
